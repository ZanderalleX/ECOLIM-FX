/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.ConexionSupabase;
import modelo.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {

    public List<Rol> listarRoles() {

        List<Rol> lista = new ArrayList<>();

        String sql = "SELECT * FROM roles ORDER BY id";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Rol r = new Rol();

                r.setId(rs.getLong("id"));
                r.setName(rs.getString("name"));

                Timestamp created = rs.getTimestamp("created_at");

                if (created != null) {
                    r.setCreatedAt(created.toLocalDateTime());
                }

                lista.add(r);
            }

        } catch (Exception e) {
            System.out.println("Error listarRoles: " + e.getMessage());
        }

        return lista;
    }

    public boolean insertarRol(Rol r) {

        String sql = "INSERT INTO roles(name) VALUES(?)";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getName());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error insertarRol: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarRol(Rol r) {

        String sql = "UPDATE roles SET name=? WHERE id=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getName());
            ps.setLong(2, r.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error actualizarRol: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarRol(long id) {

        String sql = "DELETE FROM roles WHERE id=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error eliminarRol: " + e.getMessage());
            return false;
        }
    }

    public Rol buscarPorId(long id) {

        String sql = "SELECT * FROM roles WHERE id=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Rol r = new Rol();

                    r.setId(rs.getLong("id"));
                    r.setName(rs.getString("name"));

                    Timestamp created = rs.getTimestamp("created_at");

                    if (created != null) {
                        r.setCreatedAt(created.toLocalDateTime());
                    }

                    return r;
                }
            }

        } catch (Exception e) {
            System.out.println("Error buscarPorId Rol: " + e.getMessage());
        }

        return null;
    }
}