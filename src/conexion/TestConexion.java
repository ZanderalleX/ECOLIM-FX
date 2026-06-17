
package conexion;

import conexion.ConexionSupabase;
import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {

        try {

            Connection con = ConexionSupabase.conectar();

            if (con != null) {
                System.out.println("Conexion exitosa con Supabase");
            }

        } catch (Exception e) {

            System.out.println("Error de conexion");
            e.printStackTrace();

        }

    }
}