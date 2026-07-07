package com.galeria.api;

import com.galeria.Conexion;
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

@WebServlet("/api/favoritos")
public class FavoritosServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer usuarioId = obtenerUsuarioId(request);
    if (usuarioId == null) {
      JsonUtils.writeError(response, HttpServletResponse.SC_UNAUTHORIZED,
          "Debes iniciar sesión para ver tus favoritos");
      return;
    }

    try (Connection con = Conexion.conectar();
        PreparedStatement ps = con.prepareStatement(
            "SELECT o.id, o.titulo, o.descripcion, o.precio, o.url_imagen, u.nombre AS artista " +
                "FROM favoritos f " +
                "JOIN obras o ON f.obra_id = o.id " +
                "JOIN usuarios u ON o.artista_id = u.id " +
                "WHERE f.usuario_id = ? AND o.estado = 'aprobada' " +
                "ORDER BY f.creado_en DESC")) {

      ps.setInt(1, usuarioId);
      StringBuilder json = new StringBuilder("[");
      try (ResultSet rs = ps.executeQuery()) {
        boolean primero = true;
        while (rs.next()) {
          if (!primero) {
            json.append(",");
          }
          primero = false;
          json.append("{")
              .append("\"id\":").append(rs.getInt("id")).append(",")
              .append("\"titulo\":\"").append(JsonUtils.escape(rs.getString("titulo"))).append("\",")
              .append("\"descripcion\":\"").append(JsonUtils.escape(rs.getString("descripcion")))
              .append("\",")
              .append("\"precio\":").append(rs.getLong("precio")).append(",")
              .append("\"imagenUrl\":\"").append(JsonUtils.escape(rs.getString("url_imagen")))
              .append("\",")
              .append("\"artista\":\"").append(JsonUtils.escape(rs.getString("artista"))).append("\"")
              .append("}");
        }
      }
      json.append("]");
      JsonUtils.writeJson(response, json.toString());

    } catch (SQLException e) {
      getServletContext().log("Error en FavoritosServlet (GET)", e);
      JsonUtils.writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error de servidor");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer usuarioId = obtenerUsuarioId(request);
    if (usuarioId == null) {
      JsonUtils.writeError(response, HttpServletResponse.SC_UNAUTHORIZED,
          "Debes iniciar sesión para guardar favoritos");
      return;
    }

    Integer obraId = parseObraId(request);
    if (obraId == null) {
      JsonUtils.writeError(response, HttpServletResponse.SC_BAD_REQUEST, "obraId requerido");
      return;
    }

    try (Connection con = Conexion.conectar();
        PreparedStatement ps = con.prepareStatement(
            "INSERT IGNORE INTO favoritos (usuario_id, obra_id) VALUES (?, ?)")) {
      ps.setInt(1, usuarioId);
      ps.setInt(2, obraId);
      ps.executeUpdate();
      JsonUtils.writeJson(response, "{\"ok\":true,\"obraId\":" + obraId + "}");
    } catch (SQLException e) {
      getServletContext().log("Error en FavoritosServlet (POST)", e);
      JsonUtils.writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error de servidor");
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer usuarioId = obtenerUsuarioId(request);
    if (usuarioId == null) {
      JsonUtils.writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Debes iniciar sesión");
      return;
    }

    Integer obraId = parseObraId(request);
    if (obraId == null) {
      JsonUtils.writeError(response, HttpServletResponse.SC_BAD_REQUEST, "obraId requerido");
      return;
    }

    try (Connection con = Conexion.conectar();
        PreparedStatement ps = con.prepareStatement(
            "DELETE FROM favoritos WHERE usuario_id = ? AND obra_id = ?")) {
      ps.setInt(1, usuarioId);
      ps.setInt(2, obraId);
      ps.executeUpdate();
      JsonUtils.writeJson(response, "{\"ok\":true,\"obraId\":" + obraId + "}");
    } catch (SQLException e) {
      getServletContext().log("Error en FavoritosServlet (DELETE)", e);
      JsonUtils.writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error de servidor");
    }
  }

  private Integer obtenerUsuarioId(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return null;
    }
    Object valor = session.getAttribute("usuarioId");
    return (valor instanceof Integer) ? (Integer) valor : null;
  }

  private Integer parseObraId(HttpServletRequest request) {
    String param = request.getParameter("obraId");
    if (param == null || param.isBlank()) {
      return null;
    }
    try {
      return Integer.parseInt(param.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }
}