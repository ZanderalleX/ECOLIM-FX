package dao;

import conexion.ConexionSupabase;
import modelo.TipoResiduo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoResiduoDAO {

    public List<TipoResiduo> listarTiposResiduo() {
        List<TipoResiduo> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipo_residuo ORDER BY id_residuo";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoResiduo t = new TipoResiduo();
                t.setIdResiduo(rs.getInt("id_residuo"));
                t.setNombreResiduo(rs.getString("nombre_residuo"));
                t.setCategoria(rs.getString("categoria"));
                lista.add(t);
            }

        } catch (Exception e) {
            System.out.println("Error listarTiposResiduo: " + e.getMessage());
        }

        return lista;
    }

    public TipoResiduo buscarPorId(int idResiduo) {
        String sql = "SELECT * FROM tipo_residuo WHERE id_residuo=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idResiduo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TipoResiduo t = new TipoResiduo();
                    t.setIdResiduo(rs.getInt("id_residuo"));
                    t.setNombreResiduo(rs.getString("nombre_residuo"));
                    t.setCategoria(rs.getString("categoria"));
                    return t;
                }
            }

        } catch (Exception e) {
            System.out.println("Error buscarPorId TipoResiduo: " + e.getMessage());
        }

        return null;
    }
}