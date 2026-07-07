package com.galeria.servlets;

import com.galeria.Conexion;
import com.galeria.api.JsonUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Devuelve datos de una obra como JSON.
 * GET /api/obra?id=X
 */
@WebServlet("/api/obra")
public class ObraDetalleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

        try (Connection con = Conexion.conectar();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT o.id, o.titulo, o.descripcion, o.precio, o.url_imagen, o.estado, " +
                                "u.nombre AS artista, u.id AS artista_id " +
                                "FROM obras o JOIN usuarios u ON o.artista_id = u.id WHERE o.id = ?")) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    JsonUtils.writeError(response, HttpServletResponse.SC_NOT_FOUND, "obra no encontrada");
                    return;
                }
                String json = "{"
                        + "\"id\":" + rs.getInt("id") + ","
                        + "\"titulo\":\"" + JsonUtils.escape(rs.getString("titulo")) + "\","
                        + "\"descripcion\":\"" + JsonUtils.escape(rs.getString("descripcion")) + "\","
                        + "\"precio\":" + rs.getLong("precio") + ","
                        + "\"imagenUrl\":\"" + JsonUtils.escape(rs.getString("url_imagen")) + "\","
                        + "\"estado\":\"" + JsonUtils.escape(rs.getString("estado")) + "\","
                        + "\"artista\":\"" + JsonUtils.escape(rs.getString("artista")) + "\","
                        + "\"artistaId\":" + rs.getInt("artista_id")
                        + "}";
                JsonUtils.writeJson(response, json);
            }

        } catch (SQLException e) {
            getServletContext().log("Error en ObraDetalleServlet", e);
            JsonUtils.writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error servidor");
        }
    }
}
