package com.galeria.servlets;

import com.galeria.Conexion;
import com.galeria.api.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Panel de moderación para el administrador.
 *
 * GET /api/admin/obras → lista obras por estado
 * (?estado=pendiente|aprobada|rechazada)
 * POST /api/admin/obra/aprobar → aprueba obra (?id=X)
 * POST /api/admin/obra/rechazar → rechaza obra (?id=X&motivo=...)
 *
 * SQL necesario (ejecutar una vez):
 * ALTER TABLE obras ADD COLUMN estado VARCHAR(20) NOT NULL DEFAULT 'pendiente';
 * ALTER TABLE obras ADD COLUMN motivo_rechazo TEXT;
 */
@WebServlet(urlPatterns = { "/api/admin/obras", "/api/admin/obra/aprobar", "/api/admin/obra/rechazar" })
public class AdminServlet extends HttpServlet {

    // ── GET /api/admin/obras?estado=pendiente ──────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdmin(request, response))
            return;

        String estado = request.getParameter("estado");
        if (estado == null || estado.isBlank())
            estado = "pendiente";

        StringBuilder sb = new StringBuilder("[");
        try (Connection con = Conexion.conectar();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT o.id, o.titulo, o.descripcion, o.precio, o.url_imagen, o.estado, " +
                                "u.nombre AS artista, u.email AS artista_email " +
                                "FROM obras o JOIN usuarios u ON o.artista_id = u.id " +
                                "WHERE o.estado = ? ORDER BY o.id DESC")) {

            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = true;
                while (rs.next()) {
                    if (!first)
                        sb.append(",");
                    first = false;
                    sb.append("{")
                            .append("\"id\":").append(rs.getInt("id")).append(",")
                            .append("\"titulo\":\"").append(JsonUtils.escape(rs.getString("titulo"))).append("\",")
                            .append("\"descripcion\":\"").append(JsonUtils.escape(rs.getString("descripcion")))
                            .append("\",")
                            .append("\"precio\":").append(rs.getLong("precio")).append(",")
                            .append("\"imagenUrl\":\"").append(JsonUtils.escape(rs.getString("url_imagen")))
                            .append("\",")
                            .append("\"estado\":\"").append(JsonUtils.escape(rs.getString("estado"))).append("\",")
                            .append("\"artista\":\"").append(JsonUtils.escape(rs.getString("artista"))).append("\",")
                            .append("\"artistaEmail\":\"").append(JsonUtils.escape(rs.getString("artista_email")))
                            .append("\"")
                            .append("}");
                }
            }
        } catch (SQLException e) {
            getServletContext().log("Error en AdminServlet.doGet", e);
            JsonUtils.writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error servidor");
            return;
        }

        sb.append("]");
        JsonUtils.writeJson(response, sb.toString());
    }

    // ── POST aprobar/rechazar ──────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdmin(request, response))
            return;

        String path = request.getServletPath();
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            JsonUtils.writeError(response, HttpServletResponse.SC_BAD_REQUEST, "id requerido");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            JsonUtils.writeError(response, HttpServletResponse.SC_BAD_REQUEST, "id invalido");
            return;
        }

        if (path.endsWith("/aprobar")) {
            cambiarEstado(id, "aprobada", null, response);
        } else if (path.endsWith("/rechazar")) {
            String motivo = request.getParameter("motivo");
            cambiarEstado(id, "rechazada", motivo, response);
        } else {
            JsonUtils.writeError(response, HttpServletResponse.SC_NOT_FOUND, "accion desconocida");
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private void cambiarEstado(int id, String nuevoEstado, String motivo,
            HttpServletResponse response) throws IOException {
        try (Connection con = Conexion.conectar()) {
            String sql = "aprobada".equals(nuevoEstado)
                    ? "UPDATE obras SET estado = ? WHERE id = ?"
                    : "UPDATE obras SET estado = ?, motivo_rechazo = ? WHERE id = ?";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nuevoEstado);
                if ("rechazada".equals(nuevoEstado)) {
                    ps.setString(2, motivo != null ? motivo : "");
                    ps.setInt(3, id);
                } else {
                    ps.setInt(2, id);
                }
                int filas = ps.executeUpdate();
                if (filas == 0) {
                    JsonUtils.writeError(response, HttpServletResponse.SC_NOT_FOUND, "obra no encontrada");
                    return;
                }
            }
        } catch (SQLException e) {
            getServletContext().log("Error en AdminServlet.cambiarEstado", e);
            JsonUtils.writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error servidor");
            return;
        }
        JsonUtils.writeJson(response, "{\"ok\":true,\"estado\":\"" + nuevoEstado + "\"}");
    }

    private boolean esAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("rol"))) {
            JsonUtils.writeError(response, HttpServletResponse.SC_FORBIDDEN, "acceso denegado");
            return false;
        }
        return true;
    }
}
