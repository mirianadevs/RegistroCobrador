package unison.registrocobrador;

import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AsignarCobrador {
    private String idCliente;
    private String nombreCliente;
    private String idCobrador;
    private String nombreCobrador;
    private java.util.Date fecha;
    private JDateChooser dateChooser;

    // Constructor
    public AsignarCobrador(String idCliente, String nombreCliente, String idCobrador, String nombreCobrador, java.util.Date fecha) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.idCobrador = idCobrador;
        this.nombreCobrador = nombreCobrador;
        this.fecha = fecha;
        dateChooser = new JDateChooser();
        Date hoy = new Date();  // Obtener la fecha actual
        dateChooser.setSelectableDateRange(hoy, null);
    }
    public String getNombreCliente() {
    return obtenerNombreCliente(this.idCliente);
}

public String getNombreCobrador() {
    return obtenerNombreCobrador(this.idCobrador);
}

private String obtenerNombreCliente(String idCliente) {
    String sql = "SELECT nombre FROM clientes WHERE id = ?";
    try (Connection conexion = Conection.getConexion(); 
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setString(1, idCliente);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nombre"); // Retorna el nombre del cliente
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Muestra el error en consola para depuración
    }
    return null; // Si no se encuentra el cliente o hay un error
}

private String obtenerNombreCobrador(String idCobrador) {
    String sql = "SELECT nombre FROM cobradores WHERE id = ?";
    try (Connection conexion = Conection.getConexion(); 
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setString(1, idCobrador);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nombre"); // Retorna el nombre del cobrador
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Muestra el error en consola para depuración
    }
    return null; // Si no se encuentra el cobrador o hay un error
}



    // Métodos getter
    public String getIdCliente() {
        return idCliente;
    }



    public String getIdCobrador() {
        return idCobrador;
    }


    public java.util.Date getFecha() {
        return fecha;
    }
}

