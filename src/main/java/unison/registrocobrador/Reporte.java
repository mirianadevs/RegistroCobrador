package unison.registrocobrador;
import java.sql.*;

public class Reporte {

    public double obtenerTotalSaldado() {
        double totalSaldado = 0.0;
        String sql = "SELECT SUM(montoPagado) AS totalSaldado FROM Corte";
        
        try (Connection conexion = Conection.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                totalSaldado = rs.getDouble("totalSaldado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalSaldado;
    }
    public double obtenerTotalDeuda() {
    double totalDeuda = 0.0;
    String sql = "SELECT SUM(saldoActual) AS totalDeuda FROM clientes WHERE presentaDeuda = 1";
    
    try (Connection conexion = Conection.getConexion();
         PreparedStatement ps = conexion.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            totalDeuda = rs.getDouble("totalDeuda");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return totalDeuda;
}
    
    
    
}

