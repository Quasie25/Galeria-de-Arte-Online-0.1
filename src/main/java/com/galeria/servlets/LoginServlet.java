package com.galeria.servlets;

import com.galeria.Conexion;
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

/**
 * Maneja el inicio de sesión.
 * POST /api/login → valida credenciales, guarda rol en sesión, redirige.
 *
 * Tabla esperada:
 * CREATE TABLE usuarios (
 * id INT AUTO_INCREMENT PRIMARY KEY,
 * nombre VARCHAR(100) NOT NULL,
 * email VARCHAR(150) NOT NULL UNIQUE,
 * password VARCHAR(255) NOT NULL, -- bcrypt hash recomendado
 * tipo_usuario ENUM('artista','comprador') NOT NULL DEFAULT 'comprador',
 * created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 * );
 */
@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/HTML/login.html?error=campos");
            return;
        }

        try (Connection con = Conexion.conectar();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT id, nombre, rol FROM usuarios WHERE email = ? AND password = ? AND estado = 'activo'")) {

            ps.setString(1, email.trim().toLowerCase());
            ps.setString(2, password); // TODO:comparar con hash bcrypt cuando se implemente

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("usuarioId", rs.getInt("id"));
                    session.setAttribute("nombre", rs.getString("nombre"));
                    session.setAttribute("rol", rs.getString("rol"));
                    response.sendRedirect(request.getContextPath() + "/HTML/explorar-obras.html");
                } else {
                    response.sendRedirect(request.getContextPath() + "/HTML/login.html?error=credenciales");
                }
            }

        } catch (SQLException e) {
            getServletContext().log("Error en LoginServlet", e);
            response.sendRedirect(request.getContextPath() + "/HTML/login.html?error=servidor");
        }
    }
}
