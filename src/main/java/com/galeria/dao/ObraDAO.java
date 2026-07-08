package com.galeria.dao;

import com.galeria.Conexion;
import com.galeria.modelo.Obra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObraDAO {

    // INSERTAR
    public boolean insertar(Obra obra) {
        String sql = "INSERT INTO obras (titulo, descripcion, precio, url_imagen, artista_id, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, obra.getTitulo());
            ps.setString(2, obra.getDescripcion());
            ps.setBigDecimal(3, obra.getPrecio());
            ps.setString(4, obra.getUrlImagen());
            ps.setInt(5, obra.getArtistaId());
            ps.setString(6, obra.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar obra: " + e.getMessage());
            return false;
        }
    }

    // CONSULTAR por ID
    public Obra buscarPorId(int id) {
        String sql = "SELECT * FROM obras WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error al buscar obra: " + e.getMessage());
        }
        return null;
    }

    // CONSULTAR todas
    public List<Obra> listarTodos() {
        List<Obra> lista = new ArrayList<>();
        String sql = "SELECT * FROM obras";
        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar obras: " + e.getMessage());
        }
        return lista;
    }

    // ACTUALIZAR
    public boolean actualizar(Obra obra) {
        String sql = "UPDATE obras SET titulo=?, descripcion=?, precio=?, url_imagen=?, artista_id=?, estado=?, motivo_rechazo=? WHERE id=?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, obra.getTitulo());
            ps.setString(2, obra.getDescripcion());
            ps.setBigDecimal(3, obra.getPrecio());
            ps.setString(4, obra.getUrlImagen());
            ps.setInt(5, obra.getArtistaId());
            ps.setString(6, obra.getEstado());
            ps.setString(7, obra.getMotivoRechazo());
            ps.setInt(8, obra.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar obra: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR
    public boolean eliminar(int id) {
        String sql = "DELETE FROM obras WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar obra: " + e.getMessage());
            return false;
        }
    }

    private Obra mapear(ResultSet rs) throws SQLException {
        return new Obra(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("descripcion"),
            rs.getBigDecimal("precio"),
            rs.getString("url_imagen"),
            rs.getInt("artista_id"),
            rs.getString("estado"),
            rs.getString("motivo_rechazo")
        );
    }
}
