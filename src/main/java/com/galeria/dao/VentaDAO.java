package com.galeria.dao;

import com.galeria.Conexion;
import com.galeria.modelo.Venta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    // INSERTAR
    public boolean insertar(Venta venta) {
        String sql = "INSERT INTO ventas (usuario_id, obra_id) VALUES (?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, venta.getUsuarioId());
            ps.setInt(2, venta.getObraId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar venta: " + e.getMessage());
            return false;
        }
    }

    // CONSULTAR por ID
    public Venta buscarPorId(int id) {
        String sql = "SELECT * FROM ventas WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error al buscar venta: " + e.getMessage());
        }
        return null;
    }

    // CONSULTAR todas
    public List<Venta> listarTodos() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY fecha_venta DESC";
        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar ventas: " + e.getMessage());
        }
        return lista;
    }

    // ACTUALIZAR
    public boolean actualizar(Venta venta) {
        String sql = "UPDATE ventas SET usuario_id=?, obra_id=? WHERE id=?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, venta.getUsuarioId());
            ps.setInt(2, venta.getObraId());
            ps.setInt(3, venta.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar venta: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR
    public boolean eliminar(int id) {
        String sql = "DELETE FROM ventas WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar venta: " + e.getMessage());
            return false;
        }
    }

    private Venta mapear(ResultSet rs) throws SQLException {
        return new Venta(
            rs.getInt("id"),
            rs.getInt("usuario_id"),
            rs.getInt("obra_id"),
            rs.getTimestamp("fecha_venta")
        );
    }
}
