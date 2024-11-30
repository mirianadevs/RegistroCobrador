package unison.registrocobrador;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conection {

    private static final String URL = "jdbc:mysql://localhost:3306/registroCobrador"; // nombre de la base de datos
    private static final String USUARIO = "admin";
    private static final String PASSWORD = "sasa";

    public static Connection getConexion() {
    Connection conexion = null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        //System.out.println("Conexión exitosa");
    } catch (ClassNotFoundException e) {
        //System.out.println("Error al cargar el controlador de MySQL");
        e.printStackTrace();
    } catch (SQLException e) {
        //System.out.println("Error al establecer la conexión");
        e.printStackTrace();
    }
    return conexion;
}


    public static void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                //System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            //System.out.println("Error al cerrar la conexión");
            e.printStackTrace();
        }
    }
}