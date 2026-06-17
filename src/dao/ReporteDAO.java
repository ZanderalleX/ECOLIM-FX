package dao;

import conexion.ConexionSupabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    public List<Object[]> listarReporteGeneral() {
        List<Object[]> lista = new ArrayList<>();

        String sql = """
                SELECT r.id_registro,
                       u.id_usuario,
                       u.nombre || ' ' || u.apellido AS trabajador,
                       ub.id_ubicacion,
                       ub.nombre_lugar AS ubicacion,
                       tr.nombre_residuo AS residuo,
                       r.cantidad,
                       r.unidad,
                       r.fecha,
                       r.observaciones
                FROM registros_recoleccion r
                JOIN usuarios u ON r.id_usuario = u.id_usuario
                JOIN ubicaciones ub ON r.id_ubicacion = ub.id_ubicacion
                JOIN tipo_residuo tr ON r.id_residuo = tr.id_residuo
                ORDER BY r.id_registro DESC
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[10];
                fila[0] = rs.getInt("id_registro");
                fila[1] = rs.getInt("id_usuario");
                fila[2] = rs.getString("trabajador");
                fila[3] = rs.getInt("id_ubicacion");
                fila[4] = rs.getString("ubicacion");
                fila[5] = rs.getString("residuo");
                fila[6] = rs.getDouble("cantidad");
                fila[7] = rs.getString("unidad");
                fila[8] = rs.getTimestamp("fecha");
                fila[9] = rs.getString("observaciones");
                lista.add(fila);
            }

        } catch (Exception e) {
            System.out.println("Error listarReporteGeneral: " + e.getMessage());
        }

        return lista;
    }

    public List<Object[]> listarReportePorUsuario(int idUsuario) {
        List<Object[]> lista = new ArrayList<>();

        String sql = """
                SELECT r.id_registro,
                       u.id_usuario,
                       u.nombre || ' ' || u.apellido AS trabajador,
                       ub.id_ubicacion,
                       ub.nombre_lugar AS ubicacion,
                       tr.nombre_residuo AS residuo,
                       r.cantidad,
                       r.unidad,
                       r.fecha,
                       r.observaciones
                FROM registros_recoleccion r
                JOIN usuarios u ON r.id_usuario = u.id_usuario
                JOIN ubicaciones ub ON r.id_ubicacion = ub.id_ubicacion
                JOIN tipo_residuo tr ON r.id_residuo = tr.id_residuo
                WHERE u.id_usuario = ?
                ORDER BY r.id_registro DESC
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[10];
                    fila[0] = rs.getInt("id_registro");
                    fila[1] = rs.getInt("id_usuario");
                    fila[2] = rs.getString("trabajador");
                    fila[3] = rs.getInt("id_ubicacion");
                    fila[4] = rs.getString("ubicacion");
                    fila[5] = rs.getString("residuo");
                    fila[6] = rs.getDouble("cantidad");
                    fila[7] = rs.getString("unidad");
                    fila[8] = rs.getTimestamp("fecha");
                    fila[9] = rs.getString("observaciones");
                    lista.add(fila);
                }
            }

        } catch (Exception e) {
            System.out.println("Error listarReportePorUsuario: " + e.getMessage());
        }

        return lista;
    }

    public List<Object[]> listarReportePorUbicacion(int idUbicacion) {
        List<Object[]> lista = new ArrayList<>();

        String sql = """
                SELECT r.id_registro,
                       u.id_usuario,
                       u.nombre || ' ' || u.apellido AS trabajador,
                       ub.id_ubicacion,
                       ub.nombre_lugar AS ubicacion,
                       tr.nombre_residuo AS residuo,
                       r.cantidad,
                       r.unidad,
                       r.fecha,
                       r.observaciones
                FROM registros_recoleccion r
                JOIN usuarios u ON r.id_usuario = u.id_usuario
                JOIN ubicaciones ub ON r.id_ubicacion = ub.id_ubicacion
                JOIN tipo_residuo tr ON r.id_residuo = tr.id_residuo
                WHERE ub.id_ubicacion = ?
                ORDER BY r.id_registro DESC
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUbicacion);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[10];
                    fila[0] = rs.getInt("id_registro");
                    fila[1] = rs.getInt("id_usuario");
                    fila[2] = rs.getString("trabajador");
                    fila[3] = rs.getInt("id_ubicacion");
                    fila[4] = rs.getString("ubicacion");
                    fila[5] = rs.getString("residuo");
                    fila[6] = rs.getDouble("cantidad");
                    fila[7] = rs.getString("unidad");
                    fila[8] = rs.getTimestamp("fecha");
                    fila[9] = rs.getString("observaciones");
                    lista.add(fila);
                }
            }

        } catch (Exception e) {
            System.out.println("Error listarReportePorUbicacion: " + e.getMessage());
        }

        return lista;
    }

    public List<Object[]> listarUsuariosTrabajadores() {
        List<Object[]> lista = new ArrayList<>();

        String sql = """
                SELECT id_usuario, nombre || ' ' || apellido AS trabajador
                FROM usuarios
                WHERE rol = 'trabajador'
                ORDER BY trabajador
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getInt("id_usuario");
                fila[1] = rs.getString("trabajador");
                lista.add(fila);
            }

        } catch (Exception e) {
            System.out.println("Error listarUsuariosTrabajadores: " + e.getMessage());
        }

        return lista;
    }

    public List<Object[]> listarUbicaciones() {
        List<Object[]> lista = new ArrayList<>();

        String sql = """
                SELECT id_ubicacion, nombre_lugar
                FROM ubicaciones
                ORDER BY nombre_lugar
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getInt("id_ubicacion");
                fila[1] = rs.getString("nombre_lugar");
                lista.add(fila);
            }

        } catch (Exception e) {
            System.out.println("Error listarUbicaciones: " + e.getMessage());
        }

        return lista;
    }
}