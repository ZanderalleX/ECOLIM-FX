package dao;

import conexion.ConexionSupabase;
import modelo.RegistroRecoleccion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroDAO {

    public List<RegistroRecoleccion> listarRegistros() {
        List<RegistroRecoleccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM registros_recoleccion ORDER BY id_registro DESC";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RegistroRecoleccion r = new RegistroRecoleccion();
                r.setIdRegistro(rs.getInt("id_registro"));
                r.setIdUsuario(rs.getInt("id_usuario"));
                r.setIdUbicacion(rs.getInt("id_ubicacion"));
                r.setIdResiduo(rs.getInt("id_residuo"));
                r.setCantidad(rs.getDouble("cantidad"));
                r.setUnidad(rs.getString("unidad"));

                Timestamp ts = rs.getTimestamp("fecha");
                if (ts != null) {
                    r.setFecha(ts.toLocalDateTime());
                }

                r.setObservaciones(rs.getString("observaciones"));
                lista.add(r);
            }

        } catch (Exception e) {
            System.out.println("Error listarRegistros: " + e.getMessage());
        }

        return lista;
    }

    public RegistroRecoleccion buscarPorId(int idRegistro) {
        String sql = "SELECT * FROM registros_recoleccion WHERE id_registro=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idRegistro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RegistroRecoleccion r = new RegistroRecoleccion();
                    r.setIdRegistro(rs.getInt("id_registro"));
                    r.setIdUsuario(rs.getInt("id_usuario"));
                    r.setIdUbicacion(rs.getInt("id_ubicacion"));
                    r.setIdResiduo(rs.getInt("id_residuo"));
                    r.setCantidad(rs.getDouble("cantidad"));
                    r.setUnidad(rs.getString("unidad"));

                    Timestamp ts = rs.getTimestamp("fecha");
                    if (ts != null) {
                        r.setFecha(ts.toLocalDateTime());
                    }

                    r.setObservaciones(rs.getString("observaciones"));
                    return r;
                }
            }

        } catch (Exception e) {
            System.out.println("Error buscarPorId Registro: " + e.getMessage());
        }

        return null;
    }

    public boolean insertarRegistro(RegistroRecoleccion r) {
        String sql = "INSERT INTO registros_recoleccion(id_usuario, id_ubicacion, id_residuo, cantidad, unidad, observaciones) VALUES(?,?,?,?,?,?)";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getIdUsuario());
            ps.setInt(2, r.getIdUbicacion());
            ps.setInt(3, r.getIdResiduo());
            ps.setDouble(4, r.getCantidad());
            ps.setString(5, r.getUnidad());
            ps.setString(6, r.getObservaciones());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error insertarRegistro: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarRegistro(RegistroRecoleccion r) {
        String sql = "UPDATE registros_recoleccion SET id_usuario=?, id_ubicacion=?, id_residuo=?, cantidad=?, unidad=?, observaciones=? WHERE id_registro=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getIdUsuario());
            ps.setInt(2, r.getIdUbicacion());
            ps.setInt(3, r.getIdResiduo());
            ps.setDouble(4, r.getCantidad());
            ps.setString(5, r.getUnidad());
            ps.setString(6, r.getObservaciones());
            ps.setInt(7, r.getIdRegistro());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error actualizarRegistro: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarRegistro(int idRegistro) {
        String sql = "DELETE FROM registros_recoleccion WHERE id_registro=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idRegistro);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error eliminarRegistro: " + e.getMessage());
            return false;
        }
    }

    public int contarRegistros() {
        String sql = "SELECT COUNT(*) AS total FROM registros_recoleccion";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            System.out.println("Error contarRegistros: " + e.getMessage());
        }

        return 0;
    }

    public double totalKgRecolectados() {
        String sql = "SELECT COALESCE(SUM(cantidad),0) AS total_kg FROM registros_recoleccion";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total_kg");
            }

        } catch (Exception e) {
            System.out.println("Error totalKgRecolectados: " + e.getMessage());
        }

        return 0;
    }
}