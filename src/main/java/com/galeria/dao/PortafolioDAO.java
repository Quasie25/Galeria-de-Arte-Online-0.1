package com.galeria.dao;

import com.galeria.Conexion;
import com.galeria.modelo.Portafolio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PortafolioDAO {

    // INSERTAR
    public boolean insertar(Portafolio p) {
        String sql = "INSERT INTO portafolios (artista_id, biografia, link_portafolio) VALUES (?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getArtistaId());
            ps.setString(2, p.getBiografia());
            ps.setString(3, p.getLinkPortafolio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar portafolio: " + e.getMessage());
            return false;
        }
    }

    // CONSULTAR por ID
    public Portafolio buscarPorId(int id) {
        String sql = "SELECT * FROM portafolios WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error al buscar portafolio: " + e.getMessage());
        }
        return null;
    }

    // CONSULTAR todos
    public List<Portafolio> listarTodos() {
        List<Portafolio> lista = new ArrayList<>();
        String sql = "SELECT * FROM portafolios";
        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar portafolios: " + e.getMessage());
        }
        return lista;
    }

    // ACTUALIZAR
    public boolean actualizar(Portafolio p) {
        String sql = "UPDATE portafolios SET artista_id=?, biografia=?, link_portafolio=? WHERE id=?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getArtistaId());
            ps.setString(2, p.getBiografia());
            ps.setString(3, p.getLinkPortafolio());
            ps.setInt(4, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar portafolio: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR
    public boolean eliminar(int id) {
        String sql = "DELETE FROM portafolios WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar portafolio: " + e.getMessage());
            return false;
        }
    }

    private Portafolio mapear(ResultSet rs) throws SQLException {
        return new Portafolio(
            rs.getInt("id"),
            rs.getInt("artista_id"),
            rs.getString("biografia"),
            rs.getString("link_portafolio")
        );
    }
}
