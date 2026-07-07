package com.galeria;

import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        Connection con = Conexion.conectar();
        if (con != null) {
            System.out.println("----------------------------------------");
            System.out.println("¡ÉXITO! La base de datos está conectada.");
            System.out.println("----------------------------------------");
        }
    }
}