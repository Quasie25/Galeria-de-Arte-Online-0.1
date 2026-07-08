package com.galeria.dao;

import com.galeria.Conexion;
import com.galeria.modelo.Favorito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoritoDAO {

    // INSERTAR
    public boolean insertar(Favorito favorito) {
        String sql = "INSERT INTO favoritos (usuario_id, obra_id) VALUES (?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, favorito.getUsuarioId());
            ps.setInt(2, favorito.getObraId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar favorito: " + e.getMessage());
            return false;
        }
    }

    // CONSULTAR por ID
    public Favorito buscarPorId(int id) {
        String sql = "SELECT * FROM favoritos WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error al buscar favorito: " + e.getMessage());
        }
        return null;
    }

    // CONSULTAR todos
    public List<Favorito> listarTodos() {
        List<Favorito> lista = new ArrayList<>();
        String sql = "SELECT * FROM favoritos";
        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar favoritos: " + e.getMessage());
        }
        return lista;
    }

    // ACTUALIZAR
    public boolean actualizar(Favorito favorito) {
        String sql = "UPDATE favoritos SET usuario_id=?, obra_id=? WHERE id=?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, favorito.getUsuarioId());
            ps.setInt(2, favorito.getObraId());
            ps.setInt(3, favorito.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar favorito: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR
    public boolean eliminar(int id) {
        String sql = "DELETE FROM favoritos WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar favorito: " + e.getMessage());
            return false;
        }
    }

    private Favorito mapear(ResultSet rs) throws SQLException {
        return new Favorito(
            rs.getInt("id"),
            rs.getInt("usuario_id"),
            rs.getInt("obra_id"),
            rs.getTimestamp("creado_en")
        );
    }
}
