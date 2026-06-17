package dao;

import conexion.ConexionSupabase;
import modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario loginAdmin(String correo, String password) {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND password = ? AND rol = 'admin'";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setDni(rs.getString("dni"));
                    u.setCorreo(rs.getString("correo"));
                    u.setPassword(rs.getString("password"));
                    u.setRol(rs.getString("rol"));

                    Timestamp ts = rs.getTimestamp("fecha_registro");
                    if (ts != null) {
                        u.setFechaRegistro(ts.toLocalDateTime());
                    }
                    return u;
                }
            }

        } catch (Exception e) {
            System.out.println("Error loginAdmin: " + e.getMessage());
        }

        return null;
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id_usuario";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setDni(rs.getString("dni"));
                u.setCorreo(rs.getString("correo"));
                u.setPassword(rs.getString("password"));
                u.setRol(rs.getString("rol"));

                Timestamp ts = rs.getTimestamp("fecha_registro");
                if (ts != null) {
                    u.setFechaRegistro(ts.toLocalDateTime());
                }

                lista.add(u);
            }

        } catch (Exception e) {
            System.out.println("Error listarUsuarios: " + e.getMessage());
        }

        return lista;
    }

    public List<Usuario> buscarUsuarios(String texto) {
        List<Usuario> lista = new ArrayList<>();

        String sql = """
                SELECT * FROM usuarios
                WHERE CAST(id_usuario AS TEXT) ILIKE ?
                   OR nombre ILIKE ?
                   OR apellido ILIKE ?
                   OR dni ILIKE ?
                   OR correo ILIKE ?
                   OR rol ILIKE ?
                ORDER BY id_usuario
                """;

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String filtro = "%" + texto + "%";

            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);
            ps.setString(4, filtro);
            ps.setString(5, filtro);
            ps.setString(6, filtro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setDni(rs.getString("dni"));
                    u.setCorreo(rs.getString("correo"));
                    u.setPassword(rs.getString("password"));
                    u.setRol(rs.getString("rol"));

                    Timestamp ts = rs.getTimestamp("fecha_registro");
                    if (ts != null) {
                        u.setFechaRegistro(ts.toLocalDateTime());
                    }

                    lista.add(u);
                }
            }

        } catch (Exception e) {
            System.out.println("Error buscarUsuarios: " + e.getMessage());
        }

        return lista;
    }

    public boolean insertarUsuario(Usuario u) {
        String sql = "INSERT INTO usuarios(nombre, apellido, dni, correo, password, rol) VALUES(?,?,?,?,?,?)";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getDni());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getPassword());
            ps.setString(6, u.getRol());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error insertarUsuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, dni=?, correo=?, password=?, rol=? WHERE id_usuario=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getDni());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getPassword());
            ps.setString(6, u.getRol());
            ps.setInt(7, u.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error actualizarUsuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error eliminarUsuario: " + e.getMessage());
            return false;
        }
    }

    public Usuario buscarPorId(int idUsuario) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario=?";

        try (Connection con = ConexionSupabase.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setDni(rs.getString("dni"));
                    u.setCorreo(rs.getString("correo"));
                    u.setPassword(rs.getString("password"));
                    u.setRol(rs.getString("rol"));
                    return u;
                }
            }

        } catch (Exception e) {
            System.out.println("Error buscarPorId Usuario: " + e.getMessage());
        }

        return null;
    }
}