package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSupabase {

    // REEMPLAZA ESTOS DATOS CON LOS TUYOS
    private static final String HOST = "aws-1-us-east-1.pooler.supabase.com";
    private static final String PORT = "5432";
    private static final String DATABASE = "postgres";
    private static final String USER = "postgres.fcvafhdrvcxggixngevu";
    private static final String PASSWORD = "unodostrescuatro";

    private static final String URL =
            "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE +
            "?sslmode=require";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}