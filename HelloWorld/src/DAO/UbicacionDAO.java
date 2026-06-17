package dao;

import conexion.ConexionSupabase;
import modelo.Ubicacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UbicacionDAO {

    public List<Ubicacion> listarUbicaciones() {
        List<Ubicacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM ubicaciones ORDER BY id_ubicacion";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ubicacion u = new Ubicacion();
                u.setIdUbicacion(rs.getInt("id_ubicacion"));
                u.setNombreLugar(rs.getString("nombre_lugar"));
                u.setDireccion(rs.getString("direccion"));
                lista.add(u);
            }

        } catch (Exception e) {
            System.out.println("Error listarUbicaciones: " + e.getMessage());
        }

        return lista;
    }

    public List<Ubicacion> buscarUbicaciones(String texto) {
        List<Ubicacion> lista = new ArrayList<>();

        String sql = """
                SELECT * FROM ubicaciones
                WHERE CAST(id_ubicacion AS TEXT) ILIKE ?
                   OR nombre_lugar ILIKE ?
                   OR direccion ILIKE ?
                ORDER BY id_ubicacion
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String filtro = "%" + texto + "%";

            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ubicacion u = new Ubicacion();
                    u.setIdUbicacion(rs.getInt("id_ubicacion"));
                    u.setNombreLugar(rs.getString("nombre_lugar"));
                    u.setDireccion(rs.getString("direccion"));
                    lista.add(u);
                }
            }

        } catch (Exception e) {
            System.out.println("Error buscarUbicaciones: " + e.getMessage());
        }

        return lista;
    }

    public boolean insertarUbicacion(Ubicacion u) {
        String sql = "INSERT INTO ubicaciones(nombre_lugar, direccion) VALUES(?,?)";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombreLugar());
            ps.setString(2, u.getDireccion());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error insertarUbicacion: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarUbicacion(Ubicacion u) {
        String sql = "UPDATE ubicaciones SET nombre_lugar=?, direccion=? WHERE id_ubicacion=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombreLugar());
            ps.setString(2, u.getDireccion());
            ps.setInt(3, u.getIdUbicacion());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error actualizarUbicacion: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarUbicacion(int idUbicacion) {
        String sql = "DELETE FROM ubicaciones WHERE id_ubicacion=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUbicacion);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error eliminarUbicacion: " + e.getMessage());
            return false;
        }
    }

    public Ubicacion buscarPorId(int idUbicacion) {
        String sql = "SELECT * FROM ubicaciones WHERE id_ubicacion=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUbicacion);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ubicacion u = new Ubicacion();
                    u.setIdUbicacion(rs.getInt("id_ubicacion"));
                    u.setNombreLugar(rs.getString("nombre_lugar"));
                    u.setDireccion(rs.getString("direccion"));
                    return u;
                }
            }

        } catch (Exception e) {
            System.out.println("Error buscarPorId Ubicacion: " + e.getMessage());
        }

        return null;
    }
}