package unison.registrocobrador;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import static unison.registrocobrador.Conection.getConexion;

    public class RegistroCobrador {
    private final ArrayList<Cliente> listaClientes = new ArrayList<>();
    private static int siguienteId = 1;
    private static ArrayList<Usuario> listaUsuario = new ArrayList<>();

    private final ArrayList<Cobrador> listaCobradores = new ArrayList<>();
    
    public boolean agregarClienteBD(Cliente nuevoCliente) {
    String verificarTelefonoQuery = "SELECT COUNT(*) FROM clientes WHERE telefono = ?";
    String query = "INSERT INTO clientes (nombre, apellidoPaterno, apellidoMaterno, saldoActual, telefono, presentaDeuda) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conexion = getConexion();
         PreparedStatement verificarStmt = conexion.prepareStatement(verificarTelefonoQuery);
         PreparedStatement stmt = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

        // Verificar si el teléfono ya existe en la base de datos
        verificarStmt.setString(1, nuevoCliente.getTelefono());
        ResultSet rs = verificarStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {            
            return false;
        }

        // Insertar el nuevo cliente si el teléfono no existe
        stmt.setString(1, nuevoCliente.getNombre());
        stmt.setString(2, nuevoCliente.getApellidoPaterno());
        stmt.setString(3, nuevoCliente.getApellidoMaterno());
        stmt.setFloat(4, nuevoCliente.getSaldoActual());
        stmt.setString(5, nuevoCliente.getTelefono());
        stmt.setBoolean(6, nuevoCliente.isDeuda());

        stmt.executeUpdate();
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            nuevoCliente.setId(generatedKeys.getString(1));
        }

        
        listaClientes.add(nuevoCliente);
    } catch (SQLException e) {
        e.printStackTrace(); // Manejo de errores
    }
    return true;
}

    public void editarCliente(String id, Cliente clienteActualizado) throws SQLException {
    Connection conexion = null;
    PreparedStatement ps = null;

    try {
        conexion = Conection.getConexion(); // Obtener la conexión a la base de datos
        String sql = "UPDATE clientes SET nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?, saldoActual = ?, telefono = ?, presentaDeuda = ? WHERE id = ?";
        ps = conexion.prepareStatement(sql);
        ps.setString(1, clienteActualizado.getNombre());
        ps.setString(2, clienteActualizado.getApellidoPaterno());
        ps.setString(3, clienteActualizado.getApellidoMaterno());
        ps.setFloat(4, clienteActualizado.getSaldoActual());
        ps.setString(5, clienteActualizado.getTelefono());
        ps.setBoolean(6, clienteActualizado.isDeuda());
        ps.setString(7, id);
        ps.executeUpdate();  // Ejecutar la consulta UPDATE

    } catch (SQLException e) {
        throw new SQLException("Error al actualizar el cliente en la base de datos.", e);
    } finally {
        if (ps != null) ps.close();
        if (conexion != null) conexion.close();
    }
}

    public void eliminarCliente(String id) throws SQLException {
    Connection conexion = null;
    PreparedStatement psMover = null;
    PreparedStatement psEliminar = null;

    try {
        conexion = Conection.getConexion(); // Obtener la conexión a la base de datos
        
        // Mover los datos del cliente a la tabla 'clientes_eliminados'
        String sqlMover = "INSERT INTO clientes_eliminados (id, nombre, apellidoPaterno, apellidoMaterno, saldoActual, telefono, presentaDeuda) "
                        + "SELECT id, nombre, apellidoPaterno, apellidoMaterno, saldoActual, telefono, presentaDeuda FROM clientes WHERE id = ?";
        psMover = conexion.prepareStatement(sqlMover);
        psMover.setString(1, id);       
        psMover.executeUpdate(); // Ejecutar la consulta INSERT

        // Luego, eliminar el cliente de la tabla original
        String sqlEliminar = "DELETE FROM clientes WHERE id = ?";
        psEliminar = conexion.prepareStatement(sqlEliminar);
        psEliminar.setString(1, id);
        psEliminar.executeUpdate();  // Ejecutar la consulta DELETE


    } catch (SQLException e) {
        throw new SQLException("Error al mover/eliminar el cliente.", e);
    } finally {
        if (psMover != null) psMover.close();
        if (psEliminar != null) psEliminar.close();
        if (conexion != null) conexion.close();
    }
}
    
    public ArrayList<Cliente> obtenerClientes() {
    listaClientes.clear(); 
    String query = "SELECT * FROM clientes"; 

    try (Connection conexion = getConexion(); 
         PreparedStatement stmt = conexion.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            String id = rs.getString("id");
            String nombre = rs.getString("nombre");
            String apellidoPaterno = rs.getString("apellidoPaterno");
            String apellidoMaterno = rs.getString("apellidoMaterno");
            float saldoActual = rs.getFloat("saldoActual");
            String telefono = rs.getString("telefono");
            boolean presentaDeuda = rs.getBoolean("presentaDeuda");

            Cliente cliente = new Cliente(id, nombre, apellidoPaterno, apellidoMaterno, saldoActual, telefono, presentaDeuda);
            listaClientes.add(cliente);
        }
    } catch (SQLException e) {
        e.printStackTrace(); 
    }

    return listaClientes; 
}

    
    public ArrayList<Usuario> obtenerUsuario() {        
        listaUsuario.clear(); 
    String query = "SELECT * FROM usuarios"; 
     try (Connection conexion = getConexion(); 
         PreparedStatement stmt = conexion.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            String id = rs.getString("id");
            String usuario = rs.getString("usuario");
            String contraseña = rs.getString("contraseña");
            boolean admin = rs.getBoolean("admin");
            Usuario nuevoUsuario = new Usuario(id,usuario, contraseña, admin);
            listaUsuario.add(nuevoUsuario);
        }
    } catch (SQLException e) {
        e.printStackTrace(); 
    }
        return listaUsuario;
    }
    
   public static ArrayList<Usuario> obtenerListaUsuario(String usuarioIngresado) {        
        listaUsuario.clear(); 
    String query = "SELECT * FROM usuarios WHERE usuario = '"+usuarioIngresado+"'"; 
     try (Connection conexion = getConexion(); 
         PreparedStatement stmt = conexion.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            String id = rs.getString("id");
            String usuario = rs.getString("usuario");
            String contraseña = rs.getString("contraseña");
            boolean admin = rs.getBoolean("admin");
            Usuario nuevoUsuario = new Usuario(id,usuario, contraseña, admin);
            listaUsuario.add(nuevoUsuario);
        }
    } catch (SQLException e) {
        e.printStackTrace(); 
    }
        return listaUsuario;
    }
    
    
    
   public void agregarUsuario(Usuario usuario) {
    Connection conexion = Conection.getConexion();
    String sql = "INSERT INTO usuarios (usuario, contraseña, Admin) VALUES (?, ?, ?)";

    try (PreparedStatement statement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        // Asignar valores a los parámetros de la consulta
        statement.setString(1, usuario.getUsuario());
        statement.setString(2, usuario.getContraseña());
        statement.setBoolean(3, usuario.esAdmin());
        
        // Ejecutar la consulta de inserción
        int filasAfectadas = statement.executeUpdate();

        // Comprobar si la inserción fue exitosa
        if (filasAfectadas > 0) {
            // Obtener el ID generado automáticamente
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                // Asigna el ID generado a la instancia de Usuario
                usuario.setId(generatedKeys.getString(1)); // Cambié a getInt(1) asumiendo que es un entero
               
            }
        }
    } catch (SQLException e) {
        
        e.printStackTrace();
    } finally {
        Conection.cerrarConexion(conexion); // Asegúrate de cerrar la conexión
    }
}


   public void eliminarUsuario(String id) throws SQLException {
    Connection conexion = null;
    PreparedStatement psEliminar = null;

    try {
        conexion = Conection.getConexion(); // Obtener la conexión a la base de datos
        

        // Luego, eliminar el cliente de la tabla original
        String sqlEliminar = "DELETE FROM usuarios WHERE id = ?";
        psEliminar = conexion.prepareStatement(sqlEliminar);
        psEliminar.setString(1, id);
        psEliminar.executeUpdate(); 


    } catch (SQLException e) {
        throw new SQLException("Error al eliminar.", e);
    } finally {
        if (psEliminar != null) psEliminar.close();
        if (conexion != null) conexion.close();
    }
}
    
   public static Usuario obtenerUsuario(String nombreUsuario) {
    Usuario usuario = null;
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        conexion = Conection.getConexion();
        String sql = "SELECT * FROM usuarios WHERE Usuario = ?";
        ps = conexion.prepareStatement(sql);
        ps.setString(1, nombreUsuario);
        rs = ps.executeQuery();

        if (rs.next()) {
             String id = rs.getString("id"); 
            String usuarioNombre = rs.getString("Usuario");
            String contraseña = rs.getString("Contraseña");
            boolean esAdmin = rs.getBoolean("Admin"); 
            usuario = new Usuario(id, usuarioNombre, contraseña, esAdmin);      
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Cerrar recursos
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            Conection.cerrarConexion(conexion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return usuario;
}

 
   public boolean agregarCobradorBD(Cobrador nuevoCobrador) {
    String verificarTelefonoQuery = "SELECT COUNT(*) FROM cobradores WHERE telefono = ?";
    String query = "INSERT INTO cobradores (nombre, apellido_paterno, apellido_materno, telefono) VALUES (?, ?, ?, ?)";

    try (Connection conexion = getConexion();
         PreparedStatement verificarStmt = conexion.prepareStatement(verificarTelefonoQuery);
         PreparedStatement stmt = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

        // Verificar si el teléfono ya existe en la base de datos
        verificarStmt.setString(1, nuevoCobrador.getTelefono());
        ResultSet rs = verificarStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {            
            return false; // Retorna false si el teléfono ya existe
        }

        // Insertar el nuevo cobrador si el teléfono no existe
        stmt.setString(1, nuevoCobrador.getNombre());
        stmt.setString(2, nuevoCobrador.getApellidoPaterno());
        stmt.setString(3, nuevoCobrador.getApellidoMaterno());
        stmt.setString(4, nuevoCobrador.getTelefono());

        stmt.executeUpdate();

        // Obtener el ID generado automáticamente
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            nuevoCobrador.setId(generatedKeys.getString(1)); // Establece el ID en el objeto Cobrador
        }

        // Agregar el cobrador a la lista local
        listaCobradores.add(nuevoCobrador);
    } catch (SQLException e) {
        e.printStackTrace(); // Manejo de errores
        return false;
    }
    return true;
}
  
   public ArrayList<Cobrador> obtenerCobradores() {
    listaCobradores.clear(); // Asegúrate de que listaCobradores esté definida como un ArrayList<Cobrador>
    String query = "SELECT * FROM cobradores";

    try (Connection conexion = getConexion();
         PreparedStatement stmt = conexion.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            String id = rs.getString("id");
            String nombre = rs.getString("nombre");
            String apCobrador = rs.getString("apellido_paterno");
            String amCobrador = rs.getString("apellido_materno");
            String telefono = rs.getString("telefono");

            Cobrador cobrador = new Cobrador(id, nombre, apCobrador, amCobrador, telefono);
            listaCobradores.add(cobrador);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return listaCobradores;
}

   public void editarCobrador(String id, Cobrador cobradorActualizado) throws SQLException  {
    Connection conexion = null;
    PreparedStatement ps = null;

    try {
        conexion = Conection.getConexion(); // Obtener la conexión a la base de datos
        String sql = "UPDATE cobradores SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, telefono = ? WHERE id = ?";
        ps = conexion.prepareStatement(sql);
        ps.setString(1, cobradorActualizado.getNombre());
        ps.setString(2, cobradorActualizado.getApellidoPaterno());
        ps.setString(3, cobradorActualizado.getApellidoMaterno());
        ps.setString(4, cobradorActualizado.getTelefono());
        ps.setString(5, id);
        ps.executeUpdate();  // Ejecutar la consulta UPDATE

    } catch (SQLException e) {
        throw new SQLException("Error al actualizar el cobrador en la base de datos.", e);
    } finally {
        if (ps != null) ps.close();
        if (conexion != null) conexion.close();
    }
}

   public void eliminarCobrador(String id) throws SQLException {
    Connection conexion = null;
    PreparedStatement psMover = null;
    PreparedStatement psEliminar = null;

    try {
        conexion = Conection.getConexion(); 
        
        String sqlEliminar = "DELETE FROM cobradores WHERE id = ?";
        psEliminar = conexion.prepareStatement(sqlEliminar);
        psEliminar.setString(1, id);
        psEliminar.executeUpdate(); 

    } catch (SQLException e) {
        throw new SQLException("Error al mover/eliminar el cobrador.", e);
    } finally {
        if (psEliminar != null) psEliminar.close();
        if (conexion != null) conexion.close();
    }
}
   
   public ArrayList<Cliente> buscarClientePorNombre(String nombre) {
    ArrayList<Cliente> listaClientes = new ArrayList<>();
    String query = "SELECT * FROM clientes WHERE nombre LIKE ?";

    try (Connection conexion = Conection.getConexion();
         PreparedStatement stmt = conexion.prepareStatement(query)) {

        stmt.setString(1, "%" + nombre + "%"); // Buscar por nombre que contenga el criterio
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String id = rs.getString("id");
            String nombreCliente = rs.getString("nombre");
            String apellidoPaterno = rs.getString("apellidoPaterno");
            String apellidoMaterno = rs.getString("apellidoMaterno");
            String telefono = rs.getString("telefono");
            float saldoActual = rs.getFloat("saldoActual");
            boolean presentaDeuda = rs.getBoolean("presentaDeuda");

            Cliente cliente = new Cliente(id, nombreCliente, apellidoPaterno, apellidoMaterno, saldoActual, telefono, presentaDeuda);
            listaClientes.add(cliente);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return listaClientes;
}

   public ArrayList<Cobrador> buscarCobradorPorNombre(String nombre) {
    ArrayList<Cobrador> listaCobradores = new ArrayList<>();
    String query = "SELECT * FROM cobradores WHERE nombre LIKE ?";

    try (Connection conexion = Conection.getConexion();
         PreparedStatement stmt = conexion.prepareStatement(query)) {

        stmt.setString(1, "%" + nombre + "%"); // Buscar por nombre que contenga el criterio
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String id = rs.getString("id");
            String nombreCobrador = rs.getString("nombre");
            String apellidoM = rs.getString("apellido_paterno");
            String apellidoP = rs.getString("apellido_materno");
            String telefono = rs.getString("telefono");

            Cobrador cobrador = new Cobrador(id, nombreCobrador, apellidoM,apellidoP, telefono);
            listaCobradores.add(cobrador);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return listaCobradores;
}

  public void agregarCorte(Corte corte) {
    Connection conexion = Conection.getConexion();
    String sql = "INSERT INTO Corte (idCliente, idCobrador, montoPagado, fecha) VALUES (?, ?, ?, ?)";

    try (PreparedStatement statement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        statement.setString(1, corte.getIdCliente());
        statement.setString(2, corte.getIdCobrador());
        statement.setFloat(3, corte.getMontoPagado());
        statement.setDate(4, new java.sql.Date(corte.getFecha().getTime()));

        int filasAfectadas = statement.executeUpdate();

        if (filasAfectadas > 0) {
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                corte.setId(generatedKeys.getString(1)); // ID generado automáticamente como String
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        Conection.cerrarConexion(conexion);
    }
}


   
   public ArrayList<Corte> obtenerCortes() {
    ArrayList<Corte> listaCortes = new ArrayList<>();
    String query = "SELECT * FROM corte";

    try (Connection conexion = Conection.getConexion();
         PreparedStatement stmt = conexion.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String id = rs.getString("id"); // Cambiado a String
            String idCliente = rs.getString("idCliente"); // Cambiado a String
            String idCobrador = rs.getString("idCobrador"); // Cambiado a String
            int montoPagado = rs.getInt("montoPagado");
            java.sql.Date fecha = rs.getDate("fecha");

            Corte corte = new Corte(id, idCliente, idCobrador, montoPagado, fecha);
            listaCortes.add(corte);
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener los cortes");
        e.printStackTrace();
    }
    return listaCortes;
}


   public boolean existeCliente(String idCliente) {
    for (Cliente cliente : listaClientes) { 
        if (cliente.getId().equals(idCliente)) {
            return true;
        }
    }
    return false;
}

    public boolean existeCobrador(String idCobrador) {
        for (Cobrador cobrador : listaCobradores) { 
            if (cobrador.getId().equals(idCobrador)) {
                return true;
            }
        }
        return false;
    }
    
    public void actualizarSaldoCliente(String idCliente, float montoPagado) {
    Connection conexion = Conection.getConexion();
    String sql = "UPDATE clientes SET saldoActual = ? WHERE id = ?";
    
    try (PreparedStatement statement = conexion.prepareStatement(sql)) {
        float saldoActual = obtenerSaldoCliente(idCliente);
        float nuevoSaldo = saldoActual - montoPagado;
        statement.setFloat(1, nuevoSaldo);
        statement.setString(2, idCliente); 
        
        int filasAfectadas = statement.executeUpdate();
        
        if (filasAfectadas > 0) {
            System.out.println("Saldo actualizado correctamente.");
        }
    } catch (SQLException e) {
        System.out.println("Error al actualizar el saldo.");
        e.printStackTrace();
    } finally {
        Conection.cerrarConexion(conexion);
    }
}

    public boolean registrarPago(String idCliente, float montoPagado) {    
    float saldoActual = obtenerSaldoCliente(idCliente);
    if (montoPagado > saldoActual) {
        JOptionPane.showMessageDialog(null, "El monto pagado no puede ser mayor que la deuda actual.", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    Connection conexion = Conection.getConexion();
    String sql = "UPDATE clientes SET saldoActual = saldoActual - ? WHERE id = ?";

    try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
        stmt.setFloat(1, montoPagado);  // Restar el monto pagado del saldo
        stmt.setString(2, idCliente);   // Establecer el ID del cliente

        int filasAfectadas = stmt.executeUpdate();

        if (filasAfectadas > 0) {
            // Si el saldo se actualizó correctamente, verificar si ya no tiene deuda
            actualizarDeudaCliente(idCliente);
            return true;
        } else {
            return false;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    } finally {
        Conection.cerrarConexion(conexion);
    }
}

// Método para obtener el saldo actual de un cliente
public float obtenerSaldoCliente(String idCliente) {
    float saldoActual = 0;
    String sql = "SELECT saldoActual FROM clientes WHERE id = ?";

    try (Connection conexion = Conection.getConexion();
         PreparedStatement stmt = conexion.prepareStatement(sql)) {
        stmt.setString(1, idCliente);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            saldoActual = rs.getFloat("saldoActual");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return saldoActual;
}

public void actualizarDeudaCliente(String idCliente) {
    Connection conexion = Conection.getConexion();
    String sql = "UPDATE clientes SET presentaDeuda = ? WHERE id = ?";

    try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
        float saldoActual = obtenerSaldoCliente(idCliente);

        // Si el saldo actual es 0, establece presentaDeuda como falso
        boolean tieneDeuda = saldoActual > 0;
        stmt.setBoolean(1, tieneDeuda);
        stmt.setString(2, idCliente);

        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        Conection.cerrarConexion(conexion);
    }
}

    
    
public boolean agregarAsignacion(String idCliente, String idCobrador, java.sql.Date fecha) {
    String sql = "INSERT INTO AsignarCobrador (idCliente, idCobrador, fecha) VALUES (?, ?, ?)";
    try (Connection conexion = Conection.getConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {

        ps.setString(1, idCliente);
        ps.setString(2, idCobrador);
        ps.setDate(3, fecha);

        int filasAfectadas = ps.executeUpdate();
        return filasAfectadas > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


public List<AsignarCobrador> obtenerAsignacionesPorFecha(Date fecha) {
    List<AsignarCobrador> asignaciones = new ArrayList<>();
    String sql = "SELECT a.idCliente, c.nombre AS nombreCliente, " +
             "a.idCobrador, co.nombre AS nombreCobrador, a.fecha " +
             "FROM registrocobrador.asignarcobrador a " +
             "JOIN registrocobrador.clientes c ON a.idCliente = c.id " +
             "JOIN registrocobrador.cobradores co ON a.idCobrador = co.id " +
             "WHERE a.fecha = ?"; // Filtrar por fecha


    try (Connection conexion = Conection.getConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setDate(1, new java.sql.Date(fecha.getTime())); // Establecer la fecha como parámetro

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String idCliente = rs.getString("idCliente");
                String nombreCliente = rs.getString("nombreCliente");
                String idCobrador = rs.getString("idCobrador");
                String nombreCobrador = rs.getString("nombreCobrador");
                Date fechaAsignacion = rs.getDate("fecha");

                // Crear el objeto AsignarCobrador y agregarlo a la lista
                AsignarCobrador asignacion = new AsignarCobrador(idCliente, nombreCliente, idCobrador, nombreCobrador, fechaAsignacion);
                asignaciones.add(asignacion);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return asignaciones; // Retornar la lista de asignaciones
}


public List<AsignarCobrador> obtenerTodasAsignaciones() {
    List<AsignarCobrador> asignaciones = new ArrayList<>();

    String sql = "SELECT a.idCliente, c.nombre AS nombreCliente, " +
             "a.idCobrador, co.nombre AS nombreCobrador, a.fecha " +
             "FROM registrocobrador.asignarcobrador a " +
             "JOIN registrocobrador.clientes c ON a.idCliente = c.id " +
             "JOIN registrocobrador.cobradores co ON a.idCobrador = co.id";

 
    try (Connection conexion = Conection.getConexion();
     PreparedStatement ps = conexion.prepareStatement(sql);
     ResultSet rs = ps.executeQuery()) {

    while (rs.next()) {
        String idCliente = rs.getString("idCliente");
        String nombreCliente = rs.getString("nombreCliente");
        String idCobrador = rs.getString("idCobrador");
        String nombreCobrador = rs.getString("nombreCobrador");
        java.sql.Date sqlFecha = rs.getDate("fecha");
        java.util.Date fecha = new java.util.Date(sqlFecha.getTime()); // Convertir sql.Date a util.Date

        // Crear el objeto AsignarCobrador y agregarlo a la lista
        AsignarCobrador asignacion = new AsignarCobrador(idCliente, nombreCliente, idCobrador, nombreCobrador, fecha);
        asignaciones.add(asignacion);
    }
} catch (SQLException e) {
    e.printStackTrace(); // Manejo de errores
}


    return asignaciones;
}


public List<Transaccion> buscarTransaccionesPorCliente(int clienteId) {
    List<Transaccion> transacciones = new ArrayList<>();
    String sql = "SELECT * FROM Corte WHERE idCliente = ?";

    try (Connection conexion = Conection.getConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {

        ps.setInt(1, clienteId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Transaccion transaccion = new Transaccion();
                transaccion.setId(rs.getInt("id"));
                transaccion.setMontoPagado(rs.getDouble("montoPagado"));
                transaccion.setFecha(rs.getDate("fecha"));
                transaccion.setIdCliente(rs.getInt("idCliente"));
                transaccion.setIdCobrador(rs.getInt("idCobrador"));
                transacciones.add(transaccion);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al buscar transacciones por cliente: " + e.getMessage());
    }

    return transacciones;
}


    public List<Transaccion> buscarTransaccionesPorCobrador(int cobradorId) {
    List<Transaccion> transacciones = new ArrayList<>();
    String sql = "SELECT * FROM Corte WHERE idCobrador = ?";

    try (Connection conexion = Conection.getConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {

        ps.setInt(1, cobradorId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Transaccion transaccion = new Transaccion();
                transaccion.setId(rs.getInt("id"));
                transaccion.setMontoPagado(rs.getDouble("montoPagado"));
                transaccion.setFecha(rs.getDate("fecha"));
                transaccion.setIdCliente(rs.getInt("idCliente"));
                transaccion.setIdCobrador(rs.getInt("idCobrador"));
                transacciones.add(transaccion);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al buscar transacciones por cobrador: " + e.getMessage());
    }

    return transacciones;
}

   public List<Cliente> obtenerClientesPorDeuda(boolean tieneDeuda) {
        List<Cliente> clientesConDeuda = new ArrayList<>();
        String sql = "SELECT * FROM Clientes WHERE presentaDeuda = ?";

        try (Connection conexion = Conection.getConexion(); 
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setBoolean(1, tieneDeuda);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("apellidoPaterno"),
                    rs.getString("apellidoMaterno"),
                    rs.getFloat("saldoActual"),
                    rs.getString("telefono"),
                    rs.getBoolean("presentaDeuda")
                );
                clientesConDeuda.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientesConDeuda;
    }


    
    



   
   
   
    public static void main(String[] args) {
        inicioSesion ventana = new inicioSesion();
        ventana.setVisible(true);
    }
}
