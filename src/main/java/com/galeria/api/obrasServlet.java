package com.galeria.api;

import com.galeria.Conexion;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/api/obras")
public class ObrasServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    String q = request.getParameter("q");
    String precioMax = request.getParameter("precioMax");

    // Construye el SQL según los filtros recibidos
    StringBuilder sql = new StringBuilder(
        "SELECT o.id, o.titulo, o.descripcion, o.precio, o.url_imagen, u.nombre AS artista " +
            "FROM obras o JOIN usuarios u ON o.artista_id = u.id " +
            "WHERE o.estado = 'aprobada'");

    if (q != null && !q.isBlank()) {
      sql.append(" AND (o.titulo LIKE ? OR o.descripcion LIKE ?)");
    }
    if (precioMax != null && !precioMax.isBlank()) {
      sql.append(" AND o.precio <= ?");
    }

    sql.append(" ORDER BY o.id DESC");

    try (Connection con = Conexion.conectar();
        PreparedStatement ps = con.prepareStatement(sql.toString())) {

      int indice = 1;

      if (q != null && !q.isBlank()) {
        String like = "%" + q.trim() + "%";
        ps.setString(indice++, like);
        ps.setString(indice++, like);
      }
      if (precioMax != null && !precioMax.isBlank()) {
        try {
          ps.setLong(indice, Long.parseLong(precioMax.trim()));
        } catch (NumberFormatException e) {
          JsonUtils.writeError(response,
              HttpServletResponse.SC_BAD_REQUEST, "precioMax debe ser un número");
          return;
        }
      }

      StringBuilder json = new StringBuilder("[");
      try (ResultSet rs = ps.executeQuery()) {
        boolean primero = true;
        while (rs.next()) {
          if (!primero)
            json.append(",");
          primero = false;
          json.append("{")
              .append("\"id\":").append(rs.getInt("id")).append(",")
              .append("\"titulo\":\"").append(JsonUtils.escape(rs.getString("titulo"))).append("\",")
              .append("\"descripcion\":\"").append(JsonUtils.escape(rs.getString("descripcion"))).append("\",")
              .append("\"precio\":").append(rs.getLong("precio")).append(",")
              .append("\"imagenUrl\":\"").append(JsonUtils.escape(rs.getString("url_imagen"))).append("\",")
              .append("\"artista\":\"").append(JsonUtils.escape(rs.getString("artista"))).append("\"")
              .append("}");
        }
      }
      json.append("]");
      JsonUtils.writeJson(response, json.toString());

    } catch (SQLException e) {
      getServletContext().log("Error en ObrasServlet", e);
      JsonUtils.writeError(response,
          HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error de servidor");
    }
  }
}