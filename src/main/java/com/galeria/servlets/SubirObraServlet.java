package com.galeria.servlets;

import com.galeria.Conexion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Recibe el formulario de publicar obra (multipart), guarda la imagen en
 * /Imagenes/ y persiste la obra en BD.
 * Redirige a obra-detalle.html?id=X al finalizar.
 *
 * Tabla esperada:
 * CREATE TABLE obras (
 * id INT AUTO_INCREMENT PRIMARY KEY,
 * titulo VARCHAR(200) NOT NULL,
 * descripcion TEXT,
 * precio DECIMAL(12,2) NOT NULL,
 * imagen_url VARCHAR(500),
 * artista_id INT NOT NULL,
 * created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 * FOREIGN KEY (artista_id) REFERENCES usuarios(id)
 * );
 */
@WebServlet("/api/obra/subir")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024, maxRequestSize = 12 * 1024 * 1024)
public class SubirObraServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"artista".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/HTML/login.html?error=acceso");
            return;
        }

        int artistaId = (int) session.getAttribute("usuarioId");

        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String precioStr = request.getParameter("precio");

        if (titulo == null || titulo.isBlank() || precioStr == null) {
            response.sendRedirect(request.getContextPath() + "/HTML/subir-obra.html?error=campos");
            return;
        }

        long precio;
        try {
            precio = Long.parseLong(precioStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/HTML/subir-obra.html?error=precio");
            return;
        }

        // Guardar imagen
        String imagenUrl = null;
        Part filePart = request.getPart("imagen");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String safeName = System.currentTimeMillis() + "_" + fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String uploadDir = getServletContext().getRealPath("/Imagenes");
            new File(uploadDir).mkdirs();
            try (InputStream in = filePart.getInputStream()) {
                Files.copy(in, Paths.get(uploadDir, safeName));
            }
            imagenUrl = "../Imagenes/" + safeName;
        }

        // Persistir obra
        int obraId = -1;
        try (Connection con = Conexion.conectar();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO obras (titulo, descripcion, precio, url_imagen, artista_id, estado) VALUES (?,?,?,?,?,'pendiente')",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, titulo.trim());
            ps.setString(2, descripcion != null ? descripcion.trim() : "");
            ps.setLong(3, precio);
            ps.setString(4, imagenUrl);
            ps.setInt(5, artistaId);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    obraId = keys.getInt(1);
            }

        } catch (SQLException e) {
            getServletContext().log("Error en SubirObraServlet", e);
            response.sendRedirect(request.getContextPath() + "/HTML/subir-obra.html?error=servidor");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/HTML/obra-detalle.html?id=" + obraId);
    }
}
