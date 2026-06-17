/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.ConexionSupabase;
import modelo.Planta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlantaDAO {

    public List<Planta> listarPlantas() {

        List<Planta> lista = new ArrayList<>();

        String sql = "SELECT * FROM plants ORDER BY id";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Planta p = new Planta();

                p.setId(rs.getLong("id"));
                p.setCode(rs.getString("code"));
                p.setName(rs.getString("name"));
                p.setAddress(rs.getString("address"));
                p.setPhone(rs.getString("phone"));
                p.setLatitude(rs.getBigDecimal("latitude"));
                p.setLongitude(rs.getBigDecimal("longitude"));
                p.setStatus(rs.getBoolean("status"));
                p.setCreatedBy(rs.getLong("created_by"));
                p.setUpdatedBy(rs.getLong("updated_by"));
                p.setEntityId(rs.getLong("entity_id"));

                Timestamp created = rs.getTimestamp("created_at");
                if (created != null) {
                    p.setCreatedAt(created.toLocalDateTime());
                }

                Timestamp updated = rs.getTimestamp("updated_at");
                if (updated != null) {
                    p.setUpdatedAt(updated.toLocalDateTime());
                }

                lista.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error listarPlantas: " + e.getMessage());
        }

        return lista;
    }

    public boolean insertarPlanta(Planta p) {

        String sql = """
                INSERT INTO plants
                (code, name, address, phone, latitude, longitude,
                 status, created_by, updated_by, entity_id)
                VALUES (?,?,?,?,?,?,?,?,?,?)
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCode());
            ps.setString(2, p.getName());
            ps.setString(3, p.getAddress());
            ps.setString(4, p.getPhone());
            ps.setBigDecimal(5, p.getLatitude());
            ps.setBigDecimal(6, p.getLongitude());
            ps.setBoolean(7, p.isStatus());
            ps.setLong(8, p.getCreatedBy());
            ps.setLong(9, p.getUpdatedBy());
            ps.setLong(10, p.getEntityId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error insertarPlanta: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarPlanta(Planta p) {

        String sql = """
                UPDATE plants
                SET code=?, name=?, address=?, phone=?,
                    latitude=?, longitude=?, status=?,
                    updated_by=?, entity_id=?
                WHERE id=?
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCode());
            ps.setString(2, p.getName());
            ps.setString(3, p.getAddress());
            ps.setString(4, p.getPhone());
            ps.setBigDecimal(5, p.getLatitude());
            ps.setBigDecimal(6, p.getLongitude());
            ps.setBoolean(7, p.isStatus());
            ps.setLong(8, p.getUpdatedBy());
            ps.setLong(9, p.getEntityId());
            ps.setLong(10, p.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error actualizarPlanta: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPlanta(long id) {

        String sql = "DELETE FROM plants WHERE id=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error eliminarPlanta: " + e.getMessage());
            return false;
        }
    }

    public Planta buscarPorId(long id) {

        String sql = "SELECT * FROM plants WHERE id=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Planta p = new Planta();

                    p.setId(rs.getLong("id"));
                    p.setCode(rs.getString("code"));
                    p.setName(rs.getString("name"));
                    p.setAddress(rs.getString("address"));
                    p.setPhone(rs.getString("phone"));
                    p.setLatitude(rs.getBigDecimal("latitude"));
                    p.setLongitude(rs.getBigDecimal("longitude"));
                    p.setStatus(rs.getBoolean("status"));
                    p.setCreatedBy(rs.getLong("created_by"));
                    p.setUpdatedBy(rs.getLong("updated_by"));
                    p.setEntityId(rs.getLong("entity_id"));

                    Timestamp created = rs.getTimestamp("created_at");
                    if (created != null) {
                        p.setCreatedAt(created.toLocalDateTime());
                    }

                    Timestamp updated = rs.getTimestamp("updated_at");
                    if (updated != null) {
                        p.setUpdatedAt(updated.toLocalDateTime());
                    }

                    return p;
                }
            }

        } catch (Exception e) {
            System.out.println("Error buscarPorId Planta: " + e.getMessage());
        }

        return null;
    }
}