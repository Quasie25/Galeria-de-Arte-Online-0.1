package com.galeria;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    public static Connection conectar() {
        Connection con = null;
        try {
            // Cargar el driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // CONECTAR
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/galeria_arte_0_1?useSSL=false&serverTimezone=UTC", "root",
                    "Root123456789*");
            System.out.println("¡Conexión exitosa!");
        } catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return con;
    }
}