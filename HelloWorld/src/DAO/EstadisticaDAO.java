package dao;

import conexion.ConexionSupabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstadisticaDAO {

    public List<Object[]> totalPorUsuarioGeneral() {
        List<Object[]> lista = new ArrayList<>();

        String sql = """
                SELECT u.nombre || ' ' || u.apellido AS trabajador,
                       COALESCE(SUM(r.cantidad), 0) AS total
                FROM registros_recoleccion r
                JOIN usuarios u ON r.id_usuario = u.id_usuario
                GROUP BY u.nombre, u.apellido
                ORDER BY total DESC
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getString("trabajador");
                fila[1] = rs.getDouble("total");
                lista.add(fila);
            }

        } catch (Exception e) {
            System.out.println("Error totalPorUsuarioGeneral: " + e.getMessage());
        }

        return lista;
    }

    public List<Object[]> totalPorResiduoGeneral() {
        List<Object[]> lista = new ArrayList<>();

        String sql = """
                SELECT tr.nombre_residuo,
                       COALESCE(SUM(r.cantidad), 0) AS total
                FROM registros_recoleccion r
                JOIN tipo_residuo tr ON r.id_residuo = tr.id_residuo
                GROUP BY tr.nombre_residuo
                ORDER BY total DESC
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getString("nombre_residuo");
                fila[1] = rs.getDouble("total");
                lista.add(fila);
            }

        } catch (Exception e) {
            System.out.println("Error totalPorResiduoGeneral: " + e.getMessage());
        }

        return lista;
    }

    public List<Object[]> totalPorUsuarioMensual() {
        List<Object[]> lista = new ArrayList<>();

        LocalDate fechaReferencia = obtenerFechaReferencia();

        String sql = """
                SELECT u.nombre || ' ' || u.apellido AS trabajador,
                       COALESCE(SUM(r.cantidad), 0) AS total
                FROM registros_recoleccion r
                JOIN usuarios u ON r.id_usuario = u.id_usuario
                WHERE EXTRACT(MONTH FROM r.fecha) = ?
                  AND EXTRACT(YEAR FROM r.fecha) = ?
                GROUP BY u.nombre, u.apellido
                ORDER BY total DESC
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, fechaReferencia.getMonthValue());
            ps.setInt(2, fechaReferencia.getYear());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[2];
                    fila[0] = rs.getString("trabajador");
                    fila[1] = rs.getDouble("total");
                    lista.add(fila);
                }
            }

        } catch (Exception e) {
            System.out.println("Error totalPorUsuarioMensual: " + e.getMessage());
        }

        return lista;
    }

    public List<Object[]> totalPorResiduoMensual() {
        List<Object[]> lista = new ArrayList<>();

        LocalDate fechaReferencia = obtenerFechaReferencia();

        String sql = """
                SELECT tr.nombre_residuo,
                       COALESCE(SUM(r.cantidad), 0) AS total
                FROM registros_recoleccion r
                JOIN tipo_residuo tr ON r.id_residuo = tr.id_residuo
                WHERE EXTRACT(MONTH FROM r.fecha) = ?
                  AND EXTRACT(YEAR FROM r.fecha) = ?
                GROUP BY tr.nombre_residuo
                ORDER BY total DESC
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, fechaReferencia.getMonthValue());
            ps.setInt(2, fechaReferencia.getYear());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[2];
                    fila[0] = rs.getString("nombre_residuo");
                    fila[1] = rs.getDouble("total");
                    lista.add(fila);
                }
            }

        } catch (Exception e) {
            System.out.println("Error totalPorResiduoMensual: " + e.getMessage());
        }

        return lista;
    }

    public String obtenerEtiquetaPeriodoMensual() {
        LocalDate fechaReferencia = obtenerFechaReferencia();
        String[] meses = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        return meses[fechaReferencia.getMonthValue() - 1] + " " + fechaReferencia.getYear();
    }

    private LocalDate obtenerFechaReferencia() {
        LocalDate hoy = LocalDate.now();

        if (hoy.getDayOfMonth() >= 15) {
            return hoy;
        } else {
            return hoy.minusMonths(1);
        }
    }
}