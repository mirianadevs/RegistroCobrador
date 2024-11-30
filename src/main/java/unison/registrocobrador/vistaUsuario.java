package unison.registrocobrador;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List; 
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class vistaUsuario extends javax.swing.JFrame {
    DefaultTableModel modelClientes = new DefaultTableModel();
    DefaultTableModel modelCobradores = new DefaultTableModel();
    private RegistroCobrador registroCobrador;
    
    public vistaUsuario() {
        initComponents();
        registroCobrador = new RegistroCobrador();
        configurarCalendario();

        // Configuración de la tabla de clientes
        modelClientes = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que todas las celdas sean no editables
            }
        };
        String[] columnNamesClientes = {"ID", "Nombre", "Ap. Paterno", "Ap. Materno", "Saldo Actual", "Teléfono", "Deuda"};
        modelClientes.setColumnIdentifiers(columnNamesClientes);
        tablaCliente.setModel(modelClientes);

        // Configuración de la tabla de cobradores
        modelCobradores = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que todas las celdas sean no editables
            }
        };
        String[] columnNamesCobradores = {"ID", "Nombre", "Ap. Paterno", "Ap. Materno", "Teléfono"};
        modelCobradores.setColumnIdentifiers(columnNamesCobradores);
        tablaCobrador.setModel(modelCobradores);
        
        // Configuración de la tabla de cortes
        DefaultTableModel modelCortes = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que todas las celdas sean no editables
            }
        };
        String[] columnNamesCortes = {"ID Corte", "ID Cliente", "ID Cobrador", "Monto Pagado", "Fecha"};
        modelCortes.setColumnIdentifiers(columnNamesCortes);
        tablaCorte.setModel(modelCortes);
        
            DefaultTableModel modelAsignarCobrador = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Hace que todas las celdas sean no editables
        }};

        // Nombres de las columnas para AsignarCobradores
        String[] columnNamesAsignarCobrador = {"ID Cliente", "Nombre Cliente", "ID Cobrador", "Nombre Cobrador", "Fecha"};
        modelAsignarCobrador.setColumnIdentifiers(columnNamesAsignarCobrador);

        // Asignamos el modelo de la tabla a la JTable
        tablaAsignarCobrador.setModel(modelAsignarCobrador);

        
        Calendar calendar = Calendar.getInstance();
        fechaCorte.setMaxSelectableDate(calendar.getTime()); //fecha limite para seleccionar, dia actual
        
        llenarCamposClientes();
        llenarCamposCobradores();
        
        actualizarTablaCortes();
        actualizarTablaClientes();
        actualizarTablaCobradores();
        actualizarTablaAsignarCobrador();


    }

    // Método para llenar campos de clientes cuando se selecciona una fila
    private void llenarCamposClientes() {
        tablaCliente.getSelectionModel().addListSelectionListener(e -> {
            if (tablaCliente.getSelectedRow() >= 0) {
                int filaSeleccionada = tablaCliente.getSelectedRow();
                txtIDCliente.setText((String) modelClientes.getValueAt(filaSeleccionada, 0));
                txtNombreCliente.setText((String) modelClientes.getValueAt(filaSeleccionada, 1));
                txtAPCliente.setText((String) modelClientes.getValueAt(filaSeleccionada, 2));
                txtAMCliente.setText((String) modelClientes.getValueAt(filaSeleccionada, 3));
                txtSaldoCliente.setText(String.valueOf(modelClientes.getValueAt(filaSeleccionada, 4)));
                txtTelCliente.setText((String) modelClientes.getValueAt(filaSeleccionada, 5));
                DBDeudaCliente.setSelected("Sí".equals(modelClientes.getValueAt(filaSeleccionada, 6)));
            }
        });
        lblNombreCliente.setText("");
        lblAPCliente.setText("");
        lblAMCliente.setText("");
        lblSaldoCliente.setText("");
        lblTelCliente.setText("");
    }

    // Método para llenar campos de cobradores cuando se selecciona una fila
    private void llenarCamposCobradores() {
        tablaCobrador.getSelectionModel().addListSelectionListener(e -> {
            if (tablaCobrador.getSelectedRow() >= 0) {
                int filaSeleccionada = tablaCobrador.getSelectedRow();
                txtIDCobrador.setText((String) modelCobradores.getValueAt(filaSeleccionada, 0));
                txtNombreCobrador.setText((String) modelCobradores.getValueAt(filaSeleccionada, 1));
                txtAPCobrador.setText((String) modelCobradores.getValueAt(filaSeleccionada, 2));
                txtAMCobrador.setText((String) modelCobradores.getValueAt(filaSeleccionada, 3));
                txtTelCobrador.setText((String) modelCobradores.getValueAt(filaSeleccionada, 4));
            }
        });
        lblNombreCobrador.setText("");
        lblAPCobrador.setText("");
        lblAMCobrador.setText("");
        lblTelCobrador.setText("");
    }

    // Método para actualizar la tabla de clientes con los datos actuales
    private void actualizarTablaClientes() {
        modelClientes.setRowCount(0); // Limpiar la tabla
        for (Cliente cliente : registroCobrador.obtenerClientes()) {
            Object[] row = {
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellidoPaterno(),
                cliente.getApellidoMaterno(),
                cliente.getSaldoActual(),
                cliente.getTelefono(),
                cliente.isDeuda() ? "Sí" : "No"
            };
            modelClientes.addRow(row);
        }
    }
    private void actualizarTablaClientes(List<Cliente> clientes) {
    DefaultTableModel modelo = (DefaultTableModel) tablaCliente.getModel();
    modelo.setRowCount(0);
    for (Cliente cliente : clientes) {
        Object[] fila = {
            cliente.getId(),
            cliente.getNombre(),
            cliente.getApellidoPaterno(),
            cliente.getApellidoMaterno(),
            cliente.getSaldoActual(),
            cliente.getTelefono(),
            cliente.isDeuda()? "Sí" : "No" 
        };
        modelo.addRow(fila);
    }
}
    
   



    // Método para actualizar la tabla de cobradores con los datos actuales
    private void actualizarTablaCobradores() {
        modelCobradores.setRowCount(0); // Limpiar la tabla
        for (Cobrador cobrador : registroCobrador.obtenerCobradores()) {
            Object[] row = {
                cobrador.getId(),
                cobrador.getNombre(),
                cobrador.getApellidoPaterno(),
                cobrador.getApellidoMaterno(),
                cobrador.getTelefono()
            };
            modelCobradores.addRow(row);
        }
    }

    private void actualizarTablaCortes() {
    DefaultTableModel modelCortes = (DefaultTableModel) tablaCorte.getModel(); // Obtener el modelo de la tabla
    modelCortes.setRowCount(0); // Limpiar la tabla

    for (Corte corte : registroCobrador.obtenerCortes()) {
        Object[] row = {
            corte.getId(),
            corte.getIdCliente(),
            corte.getIdCobrador(),
            corte.getMontoPagado(),
            corte.getFecha()
        };
        modelCortes.addRow(row); // Agregar cada fila al modelo
    }
}

    private void actualizarTablaAsignarCobrador() {
    DefaultTableModel modelo = (DefaultTableModel) tablaAsignarCobrador.getModel();
    modelo.setRowCount(0); // Limpiar la tabla

    // Obtener la lista de asignaciones
    List<AsignarCobrador> asignaciones = registroCobrador.obtenerTodasAsignaciones();

    // Formateador de fecha
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    for (AsignarCobrador asignacion : asignaciones) {
        // Formatear la fecha antes de agregarla a la tabla
        String fechaFormateada = sdf.format(asignacion.getFecha());

        // Agregar la fila a la tabla con la fecha formateada
        Object[] fila = {
            asignacion.getIdCliente(),
            asignacion.getNombreCliente(),
            asignacion.getIdCobrador(),
            asignacion.getNombreCobrador(),
            fechaFormateada // Usar la fecha formateada aquí
        };
        modelo.addRow(fila); // Agregar fila a la tabla
    }
}



    
    private void limpiarCamposCliente() {
        txtIDCliente.setText("");
        txtNombreCliente.setText("");
        txtAPCliente.setText("");
        txtAMCliente.setText("");
        txtSaldoCliente.setText("");
        txtTelCliente.setText("");    
    }
    
    private void limpiarCamposCobrador() {
        txtIDCobrador.setText("");
        txtNombreCobrador.setText("");
        txtAPCobrador.setText("");
        txtAMCobrador.setText("");
        txtTelCobrador.setText("");    
    }
    
    private void limpiarCamposCorte() {
    txtIDClienteCorte.setText("");
    txtIDCobradorCorte.setText("");
    txtMontoPagadoCorte.setText("");
}
    
    private void limpiarCamposAsignarCobrador() {
    txtIDClienteAsignarCobrador.setText("");
    txtIDCobradorAsignarCobrador.setText("");
}

    
    private void validarUsuario() {
       String usuario = JOptionPane.showInputDialog(pestañas);
    }
    
    private boolean validarCamposCliente(){
        if (txtNombreCliente.getText().isEmpty()) {
            lblNombreCliente.setText("Campo requerido");
            return false;
        } else if(!txtNombreCliente.getText().matches("[a-zA-Z ]{3,25}")){
            lblNombreCliente.setText("Ingrese un nombre válido");
            return false;
        }
        if (txtAPCliente.getText().isEmpty()) {
            lblAPCliente.setText("Campo requerido");
            return false;
        }else if(!txtAPCliente.getText().matches("[a-zA-Z ]{3,25}")){
            lblAPCliente.setText("Ingrese un nombre válido");
            return false;
        }
        
        if (txtAMCliente.getText().isEmpty()) {
            lblAMCliente.setText("Campo requerido");
            return false;
        }else if(!txtAMCliente.getText().matches("[a-zA-Z ]{3,25}")){
            lblAMCliente.setText("Ingrese un nombre válido");
            return false;
        }
        
        if (txtSaldoCliente.getText().isEmpty()) {
            lblSaldoCliente.setText("Llenar campo requerido");
            return false;
        }else if(!txtSaldoCliente.getText().matches("\\d+(.\\d+)?")){
            lblSaldoCliente.setText("Ingrese una \n cantidad válida");
            return false;
        }
    return true;
    }
    
    private void limpiarLablesCliente(){        
        lblNombreCliente.setText("");        
        lblAPCliente.setText("");                 
        lblAMCliente.setText("");                      
        lblSaldoCliente.setText("");
        lblTelCliente.setText("");                    
    }
        
    private void limpiarLablesCobrador(){        
        lblNombreCobrador.setText("");        
        lblAPCobrador.setText("");                 
        lblAMCobrador.setText("");                      
        lblTelCobrador.setText("");                    
    }
    
    private boolean validarTelefonoCliente() { // método necesario para validar teléfono 
    if (txtTelCliente.getText().isEmpty()) {
        lblTelCliente.setText("Campo requerido");
        return false;
    }
    if (txtTelCliente.getText().length() != 10 || !txtTelCliente.getText().matches("\\d+")) { 
    lblTelCliente.setText("Deben ser 10 dígitos exactos");
    return false;
    } else if (!txtTelCliente.getText().startsWith("662")) {
    lblTelCliente.setText("Teléfono no válido");
    return false;
    }    
    lblTelCliente.setText(""); // Limpiar el mensaje de error si es válido
    return true;
}
    
    private boolean validarTelefonoCobrador() { // método necesario para validar teléfono 
    if (txtTelCobrador.getText().isEmpty()) {
        lblTelCobrador.setText("Campo requerido");
        return false;
    }
    if (txtTelCobrador.getText().length() != 10 || !txtTelCobrador.getText().matches("\\d+")) { 
    lblTelCobrador.setText("Deben ser 10 dígitos exactos");
    return false;
    } else if (!txtTelCobrador.getText().startsWith("662")) {
    lblTelCobrador.setText("Teléfono no válido");
    return false;
    }    
    lblTelCobrador.setText(""); // Limpiar el mensaje de error si es válido
    return true;
}
    
    private boolean validarCamposCobrador(){
        if (txtNombreCobrador.getText().isEmpty()) {
            lblNombreCobrador.setText("Campo requerido");
            return false;
        } else if(!txtNombreCobrador.getText().matches("[a-zA-Z ]{3,25}")){
            lblNombreCobrador.setText("Ingrese un nombre válido");
            return false;
        }
        if (txtAPCobrador.getText().isEmpty()) {
            lblAPCobrador.setText("Campo requerido");
            return false;
        }else if(!txtAPCobrador.getText().matches("[a-zA-Z ]{3,25}")){
            lblAPCobrador.setText("Ingrese un nombre válido");
            return false;
        }
        
        if (txtAMCobrador.getText().isEmpty()) {
            lblAMCobrador.setText("Campo requerido");
            return false;
        }else if(!txtAMCobrador.getText().matches("[a-zA-Z ]{3,25}")){
            lblAMCobrador.setText("Ingrese un nombre válido");
            return false;
        }
    return true;
    }
    
    private boolean validarIDsCorte() {
    String idCliente = txtIDClienteCorte.getText().trim();
    String idCobrador = txtIDCobradorCorte.getText().trim();

    // Verificar si los campos están vacíos
    if (idCliente.isEmpty()) {
        lblIDClienteCorte.setText("ID Cliente es requerido");
        return false;
    }
    if (idCobrador.isEmpty()) {
        lblIDCobradorCorte.setText("ID Cobrador es requerido");
        return false;
    }

    // Verificar si el ID Cliente existe
    if (!registroCobrador.existeCliente(idCliente)) {
        lblIDClienteCorte.setText("ID Cliente no encontrado");
        return false;
    }

    // Verificar si el ID Cobrador existe
    if (!registroCobrador.existeCobrador(idCobrador)) {
        lblIDCobradorCorte.setText("ID Cobrador no encontrado");
        return false;
    }

    // Limpiar mensajes de error si la validación pasa
    lblIDClienteCorte.setText("");
    lblIDCobradorCorte.setText("");
    return true;
}
    
    private boolean validarIDsAsignarCobrador() {
    String idCliente = txtIDClienteAsignarCobrador.getText().trim();
    String idCobrador = txtIDCobradorAsignarCobrador.getText().trim();

    // Verificar si los campos están vacíos
    if (idCliente.isEmpty()) {
        lblIDClienteAsignarCobrador.setText("ID Cliente es requerido");
        return false;
    }
    if (idCobrador.isEmpty()) {
        lblIDCobradorAsignarCobrador.setText("ID Cobrador es requerido");
        return false;
    }

    // Verificar si el ID Cliente existe
    if (!registroCobrador.existeCliente(idCliente)) {
        lblIDClienteAsignarCobrador.setText("ID Cliente no encontrado");
        return false;
    }

    // Verificar si el ID Cobrador existe
    if (!registroCobrador.existeCobrador(idCobrador)) {
        lblIDCobradorAsignarCobrador.setText("ID Cobrador no encontrado");
        return false;
    }

    // Limpiar mensajes de error si la validación pasa
    lblIDClienteAsignarCobrador.setText("");
    lblIDCobradorAsignarCobrador.setText("");
    return true;
}

    private void cerrarSesion(){
    inicioSesion volver = new inicioSesion();
        volver.setVisible(true);
        this.dispose();
    }
    
    public void mostrarTotales() {
        Reporte reporte = new Reporte();
        
        double totalSaldado = reporte.obtenerTotalSaldado();
        double totalDeuda = reporte.obtenerTotalDeuda();

        // Mostrar los totales en los JLabel
        txtTotalSaldado.setText( ""+totalSaldado);
        txtTotalEnDeuda.setText( ""+totalDeuda);
    }
    
    private void configurarCalendario() {
    fechaAsignarCobrador.setMinSelectableDate(new java.util.Date()); // Fecha actual
    }
    
    private String nombreMayus(String campoNombre){        
        return(campoNombre.toUpperCase().charAt(0)+ campoNombre.substring(1));
    }

    
   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        pestañas = new javax.swing.JTabbedPane();
        panelClientes = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCliente = new javax.swing.JTable();
        btnEliminarCliente = new javax.swing.JButton();
        RefrescarCliente = new javax.swing.JButton();
        btnClientesLiquidados = new javax.swing.JButton();
        btnClientesDeudores = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtAPCliente = new javax.swing.JTextField();
        txtAMCliente = new javax.swing.JTextField();
        lblAMCliente = new javax.swing.JLabel();
        txtSaldoCliente = new javax.swing.JTextField();
        lblAPCliente = new javax.swing.JLabel();
        txtTelCliente = new javax.swing.JTextField();
        lblNombreCliente = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblTelCliente = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblSaldoCliente = new javax.swing.JLabel();
        txtIDCliente = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        DBDeudaCliente = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblTelCliente1 = new javax.swing.JLabel();
        btnAgregarCliente = new javax.swing.JButton();
        btnEditarCliente = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();
        btnBuscarCliente = new javax.swing.JButton();
        txtBuscarCliente = new javax.swing.JTextField();
        btnLimpiarCamposClientes = new javax.swing.JButton();
        PanelCobradores = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaCobrador = new javax.swing.JTable();
        btnEliminarCobrador = new javax.swing.JButton();
        RefrescarCobrador = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtAPCobrador = new javax.swing.JTextField();
        txtAMCobrador = new javax.swing.JTextField();
        lblAMCobrador = new javax.swing.JLabel();
        lblAPCobrador = new javax.swing.JLabel();
        txtTelCobrador = new javax.swing.JTextField();
        lblNombreCobrador = new javax.swing.JLabel();
        lblTelCobrador = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtIDCobrador = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtNombreCobrador = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        lblTelCliente3 = new javax.swing.JLabel();
        btnAgregarCobrador = new javax.swing.JButton();
        btnEditarCobrador = new javax.swing.JButton();
        btnCerrarSesion1 = new javax.swing.JButton();
        btnBuscarCobrador = new javax.swing.JButton();
        txtBuscarCobrador = new javax.swing.JTextField();
        btnLimpiarCamposCobrador = new javax.swing.JButton();
        PanelCorte = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaCorte = new javax.swing.JTable();
        RefrescarCobrador1 = new javax.swing.JButton();
        btnBuscarTransaccionesCliente = new javax.swing.JButton();
        txtBuscarTransaccionesCliente = new javax.swing.JTextField();
        panelCorte = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtMontoPagadoCorte = new javax.swing.JTextField();
        lblMontoPagadoCorte = new javax.swing.JLabel();
        lblIDCobradorCorte = new javax.swing.JLabel();
        txtIDClienteCorte = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtIDCobradorCorte = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        lblTelCliente4 = new javax.swing.JLabel();
        lblIDClienteCorte = new javax.swing.JLabel();
        fechaCorte = new com.toedter.calendar.JCalendar();
        btnAgregarCorte = new javax.swing.JButton();
        btnCerrarSesion2 = new javax.swing.JButton();
        btnLimpiarCamposCorte = new javax.swing.JButton();
        btnBuscarTransaccionesCobrador = new javax.swing.JButton();
        txtBuscarTransaccionesCobrador = new javax.swing.JTextField();
        PanelCobros = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tablaAsignarCobrador = new javax.swing.JTable();
        btnMostrarTodosAsignarCobrador = new javax.swing.JButton();
        btnCerrarSesion6 = new javax.swing.JButton();
        btnMostrarAsignarCobrador = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lblIDCobradorAsignarCobrador = new javax.swing.JLabel();
        txtIDClienteAsignarCobrador = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtIDCobradorAsignarCobrador = new javax.swing.JTextField();
        lblTelCliente7 = new javax.swing.JLabel();
        lblIDClienteAsignarCobrador = new javax.swing.JLabel();
        fechaAsignarCobrador = new com.toedter.calendar.JCalendar();
        fechaAsignarCobrador.setMinSelectableDate(new java.util.Date());
        btnAgregarAsignarCobrador = new javax.swing.JButton();
        btnCerrarSesion5 = new javax.swing.JButton();
        btnLimpiarCamposAsignarCobrador = new javax.swing.JButton();
        PanelInformes = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        txtTotalSaldado = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtTotalEnDeuda = new javax.swing.JTextField();
        btnAgregarCorte2 = new javax.swing.JButton();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registro Cobradores");
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Britannic Bold", 0, 10)); // NOI18N
        setResizable(false);

        pestañas.setBackground(new java.awt.Color(115, 115, 229));

        panelClientes.setBackground(new java.awt.Color(130, 163, 198));

        jPanel1.setBackground(new java.awt.Color(123, 143, 165));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tablaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaCliente.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(tablaCliente);

        btnEliminarCliente.setBackground(new java.awt.Color(130, 163, 198));
        btnEliminarCliente.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnEliminarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarCliente.setText("Eliminar");
        btnEliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClienteActionPerformed(evt);
            }
        });

        RefrescarCliente.setBackground(new java.awt.Color(130, 163, 198));
        RefrescarCliente.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        RefrescarCliente.setForeground(new java.awt.Color(255, 255, 255));
        RefrescarCliente.setText("Refrescar");
        RefrescarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefrescarClienteActionPerformed(evt);
            }
        });

        btnClientesLiquidados.setBackground(new java.awt.Color(130, 163, 198));
        btnClientesLiquidados.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnClientesLiquidados.setForeground(new java.awt.Color(255, 255, 255));
        btnClientesLiquidados.setText("Liquidados");
        btnClientesLiquidados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesLiquidadosActionPerformed(evt);
            }
        });

        btnClientesDeudores.setBackground(new java.awt.Color(130, 163, 198));
        btnClientesDeudores.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnClientesDeudores.setForeground(new java.awt.Color(255, 255, 255));
        btnClientesDeudores.setText("Deudores");
        btnClientesDeudores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesDeudoresActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnClientesLiquidados)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClientesDeudores)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminarCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RefrescarCliente)))
                .addGap(113, 113, 113))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarCliente)
                    .addComponent(btnClientesLiquidados)
                    .addComponent(btnClientesDeudores)
                    .addComponent(RefrescarCliente))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(123, 143, 165));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Apellido Materno");

        txtAPCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAPClienteKeyTyped(evt);
            }
        });

        txtAMCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAMClienteKeyTyped(evt);
            }
        });

        lblAMCliente.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblAMCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblAMCliente.setText("Campo requerido");

        txtSaldoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSaldoClienteKeyTyped(evt);
            }
        });

        lblAPCliente.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblAPCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblAPCliente.setText("Campo requerido");

        txtTelCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelClienteActionPerformed(evt);
            }
        });
        txtTelCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelClienteKeyTyped(evt);
            }
        });

        lblNombreCliente.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblNombreCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreCliente.setText("Campo requerido");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Saldo Actual:");

        lblTelCliente.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblTelCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblTelCliente.setText("Campo requerido");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("No. Teléfono:");

        lblSaldoCliente.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblSaldoCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblSaldoCliente.setText("Campo requerido");

        txtIDCliente.setEditable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("ID:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("¿Presenta deudas?");

        DBDeudaCliente.setText("Si");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Nombres");

        txtNombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreClienteKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Apellido Paterno");

        lblTelCliente1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblTelCliente1.setForeground(new java.awt.Color(255, 255, 255));
        lblTelCliente1.setText("Todos los campos son requeridos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(50, 50, 50)
                        .addComponent(DBDeudaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtIDCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblNombreCliente)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtSaldoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                            .addComponent(txtAMCliente))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblAMCliente)
                                            .addComponent(lblSaldoCliente))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtTelCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblTelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(26, 26, 26)
                                .addComponent(txtAPCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblAPCliente))
                            .addComponent(lblTelCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(jLabel1)
                    .addContainerGap(389, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtIDCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(lblNombreCliente))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAPCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAPCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtAMCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblAMCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSaldoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(lblSaldoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(lblTelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DBDeudaCliente)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTelCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addGap(253, 253, 253)))
        );

        btnAgregarCliente.setBackground(new java.awt.Color(111, 130, 150));
        btnAgregarCliente.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAgregarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarCliente.setText("Agregar");
        btnAgregarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarClienteActionPerformed(evt);
            }
        });

        btnEditarCliente.setBackground(new java.awt.Color(111, 130, 150));
        btnEditarCliente.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnEditarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarCliente.setText("Editar");
        btnEditarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarClienteActionPerformed(evt);
            }
        });

        btnCerrarSesion.setBackground(new java.awt.Color(111, 130, 150));
        btnCerrarSesion.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCerrarSesion.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarSesion.setText("Cerrar Sesion");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        btnBuscarCliente.setBackground(new java.awt.Color(111, 130, 150));
        btnBuscarCliente.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnBuscarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarCliente.setText("Buscar");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        txtBuscarCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarClienteKeyTyped(evt);
            }
        });

        btnLimpiarCamposClientes.setBackground(new java.awt.Color(111, 130, 150));
        btnLimpiarCamposClientes.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnLimpiarCamposClientes.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarCamposClientes.setText("Limpiar");
        btnLimpiarCamposClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarCamposClientesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelClientesLayout = new javax.swing.GroupLayout(panelClientes);
        panelClientes.setLayout(panelClientesLayout);
        panelClientesLayout.setHorizontalGroup(
            panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelClientesLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnAgregarCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpiarCamposClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelClientesLayout.createSequentialGroup()
                        .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCerrarSesion))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        panelClientesLayout.setVerticalGroup(
            panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 249, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBuscarCliente)
                        .addComponent(txtBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLimpiarCamposClientes)
                        .addComponent(btnCerrarSesion))
                    .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAgregarCliente)
                        .addComponent(btnEditarCliente)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pestañas.addTab("Clientes", panelClientes);
        panelClientes.getAccessibleContext().setAccessibleName("Cliente");

        PanelCobradores.setBackground(new java.awt.Color(130, 163, 198));

        jPanel9.setBackground(new java.awt.Color(123, 143, 165));
        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaCobrador.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tablaCobrador.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tablaCobrador);

        btnEliminarCobrador.setBackground(new java.awt.Color(130, 163, 198));
        btnEliminarCobrador.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnEliminarCobrador.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarCobrador.setText("Eliminar");
        btnEliminarCobrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarCobradorActionPerformed(evt);
            }
        });

        RefrescarCobrador.setBackground(new java.awt.Color(130, 163, 198));
        RefrescarCobrador.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        RefrescarCobrador.setForeground(new java.awt.Color(255, 255, 255));
        RefrescarCobrador.setText("Refrescar");
        RefrescarCobrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefrescarCobradorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(btnEliminarCobrador)
                        .addGap(107, 107, 107)
                        .addComponent(RefrescarCobrador))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarCobrador)
                    .addComponent(RefrescarCobrador))
                .addGap(12, 12, 12))
        );

        jPanel10.setBackground(new java.awt.Color(123, 143, 165));
        jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Apellido Materno");

        txtAPCobrador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAPCobradorKeyTyped(evt);
            }
        });

        txtAMCobrador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAMCobradorKeyTyped(evt);
            }
        });

        lblAMCobrador.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblAMCobrador.setForeground(new java.awt.Color(255, 255, 255));
        lblAMCobrador.setText("Campo requerido");

        lblAPCobrador.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblAPCobrador.setForeground(new java.awt.Color(255, 255, 255));
        lblAPCobrador.setText("Campo requerido");

        txtTelCobrador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelCobradorKeyTyped(evt);
            }
        });

        lblNombreCobrador.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblNombreCobrador.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreCobrador.setText("Campo requerido");

        lblTelCobrador.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblTelCobrador.setForeground(new java.awt.Color(255, 255, 255));
        lblTelCobrador.setText("Campo requerido");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("No. Teléfono:");

        txtIDCobrador.setEditable(false);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("ID:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setText("Nombres");

        txtNombreCobrador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreCobradorKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setText("Apellido Paterno");

        lblTelCliente3.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblTelCliente3.setForeground(new java.awt.Color(255, 255, 255));
        lblTelCliente3.setText("Todos los campos son requeridos");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(26, 26, 26)
                                .addComponent(txtAPCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblAPCobrador))
                            .addComponent(lblTelCliente3, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 77, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtIDCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(txtNombreCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblNombreCobrador)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtTelCobrador)
                                    .addComponent(txtAMCobrador, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblAMCobrador)
                                    .addComponent(lblTelCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(jLabel11)
                    .addContainerGap(389, Short.MAX_VALUE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtIDCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(lblNombreCobrador))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAPCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAPCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtAMCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblAMCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(lblTelCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTelCliente3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel11)
                    .addGap(253, 253, 253)))
        );

        btnAgregarCobrador.setBackground(new java.awt.Color(111, 130, 150));
        btnAgregarCobrador.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAgregarCobrador.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarCobrador.setText("Agregar");
        btnAgregarCobrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCobradorActionPerformed(evt);
            }
        });

        btnEditarCobrador.setBackground(new java.awt.Color(111, 130, 150));
        btnEditarCobrador.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnEditarCobrador.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarCobrador.setText("Editar");
        btnEditarCobrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarCobradorActionPerformed(evt);
            }
        });

        btnCerrarSesion1.setBackground(new java.awt.Color(111, 130, 150));
        btnCerrarSesion1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCerrarSesion1.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarSesion1.setText("Cerrar Sesion");
        btnCerrarSesion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesion1ActionPerformed(evt);
            }
        });

        btnBuscarCobrador.setBackground(new java.awt.Color(111, 130, 150));
        btnBuscarCobrador.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnBuscarCobrador.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarCobrador.setText("Buscar");
        btnBuscarCobrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCobradorActionPerformed(evt);
            }
        });

        txtBuscarCobrador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarCobradorKeyTyped(evt);
            }
        });

        btnLimpiarCamposCobrador.setBackground(new java.awt.Color(111, 130, 150));
        btnLimpiarCamposCobrador.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnLimpiarCamposCobrador.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarCamposCobrador.setText("Limpiar");
        btnLimpiarCamposCobrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarCamposCobradorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelCobradoresLayout = new javax.swing.GroupLayout(PanelCobradores);
        PanelCobradores.setLayout(PanelCobradoresLayout);
        PanelCobradoresLayout.setHorizontalGroup(
            PanelCobradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCobradoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCobradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelCobradoresLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnAgregarCobrador)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpiarCamposCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelCobradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelCobradoresLayout.createSequentialGroup()
                        .addComponent(btnBuscarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBuscarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCerrarSesion1))
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelCobradoresLayout.setVerticalGroup(
            PanelCobradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCobradoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCobradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelCobradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarCobrador)
                    .addComponent(btnEditarCobrador)
                    .addComponent(btnCerrarSesion1)
                    .addComponent(btnBuscarCobrador)
                    .addComponent(txtBuscarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiarCamposCobrador))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        pestañas.addTab("Cobradores", PanelCobradores);

        PanelCorte.setBackground(new java.awt.Color(130, 163, 198));

        jPanel11.setBackground(new java.awt.Color(123, 143, 165));
        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaCorte.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tablaCorte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tablaCorte);

        RefrescarCobrador1.setBackground(new java.awt.Color(130, 163, 198));
        RefrescarCobrador1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        RefrescarCobrador1.setForeground(new java.awt.Color(255, 255, 255));
        RefrescarCobrador1.setText("Refrescar");
        RefrescarCobrador1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefrescarCobrador1ActionPerformed(evt);
            }
        });

        btnBuscarTransaccionesCliente.setBackground(new java.awt.Color(130, 163, 198));
        btnBuscarTransaccionesCliente.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnBuscarTransaccionesCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarTransaccionesCliente.setText("Transaccion Cliente");
        btnBuscarTransaccionesCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarTransaccionesClienteActionPerformed(evt);
            }
        });

        txtBuscarTransaccionesCliente.setText("ID");
        txtBuscarTransaccionesCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarTransaccionesClienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarTransaccionesClienteFocusLost(evt);
            }
        });
        txtBuscarTransaccionesCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarTransaccionesClienteKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnBuscarTransaccionesCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarTransaccionesCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RefrescarCobrador1)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RefrescarCobrador1)
                    .addComponent(btnBuscarTransaccionesCliente)
                    .addComponent(txtBuscarTransaccionesCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelCorte.setBackground(new java.awt.Color(123, 143, 165));
        panelCorte.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Fecha:");

        txtMontoPagadoCorte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMontoPagadoCorteKeyTyped(evt);
            }
        });

        lblMontoPagadoCorte.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblMontoPagadoCorte.setForeground(new java.awt.Color(255, 255, 255));
        lblMontoPagadoCorte.setText("Campo requerido");

        lblIDCobradorCorte.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblIDCobradorCorte.setForeground(new java.awt.Color(255, 255, 255));
        lblIDCobradorCorte.setText("Campo requerido");

        txtIDClienteCorte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIDClienteCorteKeyTyped(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("ID Cliente:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("ID Cobrador:");

        txtIDCobradorCorte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIDCobradorCorteKeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setText("Monto Pagado:");

        lblTelCliente4.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblTelCliente4.setForeground(new java.awt.Color(255, 255, 255));
        lblTelCliente4.setText("Todos los campos son requeridos");

        lblIDClienteCorte.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblIDClienteCorte.setForeground(new java.awt.Color(255, 255, 255));
        lblIDClienteCorte.setText("Campo requerido");

        javax.swing.GroupLayout panelCorteLayout = new javax.swing.GroupLayout(panelCorte);
        panelCorte.setLayout(panelCorteLayout);
        panelCorteLayout.setHorizontalGroup(
            panelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCorteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCorteLayout.createSequentialGroup()
                        .addGroup(panelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(panelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCorteLayout.createSequentialGroup()
                                .addComponent(txtIDClienteCorte, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblIDClienteCorte))
                            .addGroup(panelCorteLayout.createSequentialGroup()
                                .addComponent(txtIDCobradorCorte, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblIDCobradorCorte))))
                    .addGroup(panelCorteLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(32, 32, 32)
                        .addComponent(txtMontoPagadoCorte, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblMontoPagadoCorte))
                    .addComponent(lblTelCliente4, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCorteLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(fechaCorte, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        panelCorteLayout.setVerticalGroup(
            panelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCorteLayout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(panelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIDClienteCorte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIDClienteCorte, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCorteLayout.createSequentialGroup()
                        .addGroup(panelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIDCobradorCorte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(lblIDCobradorCorte))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMontoPagadoCorte, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMontoPagadoCorte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(fechaCorte, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTelCliente4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnAgregarCorte.setBackground(new java.awt.Color(111, 130, 150));
        btnAgregarCorte.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAgregarCorte.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarCorte.setText("Agregar");
        btnAgregarCorte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCorteActionPerformed(evt);
            }
        });

        btnCerrarSesion2.setBackground(new java.awt.Color(111, 130, 150));
        btnCerrarSesion2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCerrarSesion2.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarSesion2.setText("Cerrar Sesion");
        btnCerrarSesion2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesion2ActionPerformed(evt);
            }
        });

        btnLimpiarCamposCorte.setBackground(new java.awt.Color(111, 130, 150));
        btnLimpiarCamposCorte.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnLimpiarCamposCorte.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarCamposCorte.setText("Limpiar");
        btnLimpiarCamposCorte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarCamposCorteActionPerformed(evt);
            }
        });

        btnBuscarTransaccionesCobrador.setBackground(new java.awt.Color(111, 130, 150));
        btnBuscarTransaccionesCobrador.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnBuscarTransaccionesCobrador.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarTransaccionesCobrador.setText("Transaccion Cobrador");
        btnBuscarTransaccionesCobrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarTransaccionesCobradorActionPerformed(evt);
            }
        });

        txtBuscarTransaccionesCobrador.setText("ID");
        txtBuscarTransaccionesCobrador.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarTransaccionesCobradorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarTransaccionesCobradorFocusLost(evt);
            }
        });
        txtBuscarTransaccionesCobrador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarTransaccionesCobradorKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout PanelCorteLayout = new javax.swing.GroupLayout(PanelCorte);
        PanelCorte.setLayout(PanelCorteLayout);
        PanelCorteLayout.setHorizontalGroup(
            PanelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCorteLayout.createSequentialGroup()
                .addGroup(PanelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelCorteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelCorte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelCorteLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnAgregarCorte)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpiarCamposCorte, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(PanelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCorteLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnBuscarTransaccionesCobrador)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarTransaccionesCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(btnCerrarSesion2))
                    .addGroup(PanelCorteLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(60, 60, 60))
        );
        PanelCorteLayout.setVerticalGroup(
            PanelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCorteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCorte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarCorte)
                    .addComponent(btnCerrarSesion2)
                    .addComponent(btnLimpiarCamposCorte)
                    .addComponent(btnBuscarTransaccionesCobrador)
                    .addComponent(txtBuscarTransaccionesCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pestañas.addTab("Corte", PanelCorte);

        PanelCobros.setBackground(new java.awt.Color(130, 163, 198));

        jPanel17.setBackground(new java.awt.Color(123, 143, 165));
        jPanel17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaAsignarCobrador.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tablaAsignarCobrador.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(tablaAsignarCobrador);

        btnMostrarTodosAsignarCobrador.setBackground(new java.awt.Color(130, 163, 198));
        btnMostrarTodosAsignarCobrador.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnMostrarTodosAsignarCobrador.setForeground(new java.awt.Color(255, 255, 255));
        btnMostrarTodosAsignarCobrador.setText("Mostrar Todo");
        btnMostrarTodosAsignarCobrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarTodosAsignarCobradorActionPerformed(evt);
            }
        });

        btnCerrarSesion6.setBackground(new java.awt.Color(111, 130, 150));
        btnCerrarSesion6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCerrarSesion6.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarSesion6.setText("Cerrar Sesion");

        btnMostrarAsignarCobrador.setBackground(new java.awt.Color(130, 163, 198));
        btnMostrarAsignarCobrador.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnMostrarAsignarCobrador.setForeground(new java.awt.Color(255, 255, 255));
        btnMostrarAsignarCobrador.setText("Cobros De Hoy");
        btnMostrarAsignarCobrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarAsignarCobradorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane8)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnMostrarAsignarCobrador)
                        .addGap(53, 53, 53)
                        .addComponent(btnMostrarTodosAsignarCobrador)))
                .addContainerGap())
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addGap(0, 198, Short.MAX_VALUE)
                    .addComponent(btnCerrarSesion6)
                    .addGap(0, 199, Short.MAX_VALUE)))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMostrarAsignarCobrador)
                    .addComponent(btnMostrarTodosAsignarCobrador))
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnCerrarSesion6)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel18.setBackground(new java.awt.Color(123, 143, 165));
        jPanel18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("Fecha:");

        lblIDCobradorAsignarCobrador.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblIDCobradorAsignarCobrador.setForeground(new java.awt.Color(255, 255, 255));
        lblIDCobradorAsignarCobrador.setText("Campo requerido");

        txtIDClienteAsignarCobrador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIDClienteAsignarCobradorKeyTyped(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("ID Cliente:");

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel31.setText("ID Cobrador:");

        txtIDCobradorAsignarCobrador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIDCobradorAsignarCobradorKeyTyped(evt);
            }
        });

        lblTelCliente7.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblTelCliente7.setForeground(new java.awt.Color(255, 255, 255));
        lblTelCliente7.setText("Pestaña destinada a asignar cobradores a clientes ");

        lblIDClienteAsignarCobrador.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblIDClienteAsignarCobrador.setForeground(new java.awt.Color(255, 255, 255));
        lblIDClienteAsignarCobrador.setText("Campo requerido");

        fechaAsignarCobrador.setMinSelectableDate(null);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(txtIDClienteAsignarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblIDClienteAsignarCobrador))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(txtIDCobradorAsignarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblIDCobradorAsignarCobrador))))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addComponent(fechaAsignarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTelCliente7, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIDClienteAsignarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIDClienteAsignarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIDCobradorAsignarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(lblIDCobradorAsignarCobrador))
                .addGap(10, 10, 10)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(fechaAsignarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(lblTelCliente7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnAgregarAsignarCobrador.setBackground(new java.awt.Color(111, 130, 150));
        btnAgregarAsignarCobrador.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAgregarAsignarCobrador.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarAsignarCobrador.setText("Agregar");
        btnAgregarAsignarCobrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarAsignarCobradorActionPerformed(evt);
            }
        });

        btnCerrarSesion5.setBackground(new java.awt.Color(111, 130, 150));
        btnCerrarSesion5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCerrarSesion5.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarSesion5.setText("Cerrar Sesion");
        btnCerrarSesion5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesion5ActionPerformed(evt);
            }
        });

        btnLimpiarCamposAsignarCobrador.setBackground(new java.awt.Color(111, 130, 150));
        btnLimpiarCamposAsignarCobrador.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnLimpiarCamposAsignarCobrador.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarCamposAsignarCobrador.setText("Limpiar");
        btnLimpiarCamposAsignarCobrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarCamposAsignarCobradorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelCobrosLayout = new javax.swing.GroupLayout(PanelCobros);
        PanelCobros.setLayout(PanelCobrosLayout);
        PanelCobrosLayout.setHorizontalGroup(
            PanelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCobrosLayout.createSequentialGroup()
                .addGroup(PanelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelCobrosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelCobrosLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnAgregarAsignarCobrador)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpiarCamposAsignarCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCobrosLayout.createSequentialGroup()
                        .addGap(0, 460, Short.MAX_VALUE)
                        .addComponent(btnCerrarSesion5))
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9))
        );
        PanelCobrosLayout.setVerticalGroup(
            PanelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCobrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarAsignarCobrador)
                    .addComponent(btnCerrarSesion5)
                    .addComponent(btnLimpiarCamposAsignarCobrador))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pestañas.addTab("Cobros Diarios", PanelCobros);

        PanelInformes.setBackground(new java.awt.Color(130, 163, 198));

        jPanel16.setBackground(new java.awt.Color(123, 143, 165));
        jPanel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtTotalSaldado.setEditable(false);

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel26.setText("Total Saldado:");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setText("Total En Deuda");

        txtTotalEnDeuda.setEditable(false);
        txtTotalEnDeuda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTotalEnDeudaKeyTyped(evt);
            }
        });

        btnAgregarCorte2.setBackground(new java.awt.Color(130, 163, 198));
        btnAgregarCorte2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAgregarCorte2.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarCorte2.setText("Actualizar Totales");
        btnAgregarCorte2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCorte2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotalSaldado, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalEnDeuda, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnAgregarCorte2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 113, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalSaldado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalEnDeuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAgregarCorte2)
                .addGap(183, 183, 183))
        );

        javax.swing.GroupLayout PanelInformesLayout = new javax.swing.GroupLayout(PanelInformes);
        PanelInformes.setLayout(PanelInformesLayout);
        PanelInformesLayout.setHorizontalGroup(
            PanelInformesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInformesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(689, Short.MAX_VALUE))
        );
        PanelInformesLayout.setVerticalGroup(
            PanelInformesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInformesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        pestañas.addTab("Informes", PanelInformes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pestañas)
        );

        pestañas.getAccessibleContext().setAccessibleName("Clientes");
        pestañas.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarCamposCorteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCamposCorteActionPerformed
limpiarCamposCorte();
    }//GEN-LAST:event_btnLimpiarCamposCorteActionPerformed

    private void btnCerrarSesion2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesion2ActionPerformed
        inicioSesion volver = new inicioSesion();
        volver.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCerrarSesion2ActionPerformed

    private void btnAgregarCorteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCorteActionPerformed
    
    if (!validarIDsCorte()) {
        return; 
    }    
    java.util.Date fechaSeleccionada = fechaCorte.getDate();
    String idCliente = txtIDClienteCorte.getText().trim();
    String idCobrador = txtIDCobradorCorte.getText().trim();
    float montoPagado = Float.parseFloat(txtMontoPagadoCorte.getText().trim());
    
    java.sql.Date sqlFecha = new java.sql.Date(fechaSeleccionada.getTime());

    // Crear un nuevo objeto Corte
    Corte nuevoCorte = new Corte(null, idCliente, idCobrador, montoPagado, sqlFecha);    
    
    // Registrar el pago
    if (registroCobrador.registrarPago(idCliente, montoPagado)) {
        // Solo agregar el corte si el pago es exitoso
        registroCobrador.agregarCorte(nuevoCorte);
        actualizarTablaCortes();
        limpiarCamposCorte();
        actualizarTablaClientes();
        JOptionPane.showMessageDialog(this, "Corte y pago registrados correctamente.");
    } else {
        JOptionPane.showMessageDialog(this, "Error al registrar el pago.");
    }

        
    }//GEN-LAST:event_btnAgregarCorteActionPerformed

    private void txtIDCobradorCorteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDCobradorCorteKeyTyped
        if (txtIDCobradorCorte.getText().length() >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtIDCobradorCorteKeyTyped

    private void txtMontoPagadoCorteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoPagadoCorteKeyTyped
        if (txtMontoPagadoCorte.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtMontoPagadoCorteKeyTyped

    private void RefrescarCobrador1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefrescarCobrador1ActionPerformed
       actualizarTablaCortes();
    }//GEN-LAST:event_RefrescarCobrador1ActionPerformed

    private void btnLimpiarCamposCobradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCamposCobradorActionPerformed
        limpiarCamposCobrador();
    }//GEN-LAST:event_btnLimpiarCamposCobradorActionPerformed

    private void txtBuscarCobradorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCobradorKeyTyped
        if (txtBuscarCobrador.getText().length() >= 15) {
            evt.consume();
        }
    }//GEN-LAST:event_txtBuscarCobradorKeyTyped

    private void btnBuscarCobradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCobradorActionPerformed

        String nombreCobrador = txtBuscarCobrador.getText().trim();

        if (nombreCobrador.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un nombre para buscar.");
            return;
        }

        ArrayList<Cobrador> resultado = registroCobrador.buscarCobradorPorNombre(nombreCobrador);

        if (resultado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron cobradores con ese nombre.");
        } else {
            mostrarCobradores(resultado);
        }
    }//GEN-LAST:event_btnBuscarCobradorActionPerformed

    private void btnCerrarSesion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesion1ActionPerformed
        inicioSesion volver = new inicioSesion();
        volver.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCerrarSesion1ActionPerformed

    private void btnEditarCobradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarCobradorActionPerformed
        limpiarLablesCobrador();
        if (!validarCamposCobrador() || !validarTelefonoCobrador()) {
            return;
        }
        String id = txtIDCobrador.getText();
        String nombre = txtNombreCobrador.getText();
        String apellidoPaterno = txtAPCobrador.getText();
        String apellidoMaterno = txtAMCobrador.getText();
        String telefono = txtTelCobrador.getText();

        Cobrador cobradorActualizado = new Cobrador(
            id,
            nombre,
            apellidoPaterno,
            apellidoMaterno,
            telefono
        );

        try {
            registroCobrador.editarCobrador(id, cobradorActualizado);
            JOptionPane.showMessageDialog(null, "El cobrador ha sido editado");
            actualizarTablaCobradores();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al editar el cobrador en la base de datos: " + e.getMessage());
        }
    }//GEN-LAST:event_btnEditarCobradorActionPerformed

    private void btnAgregarCobradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCobradorActionPerformed
        limpiarLablesCobrador();  // Limpiar etiquetas de errores o mensajes anteriores

        if (!validarCamposCobrador() || !validarTelefonoCobrador()) {
            return;
        }

        String nombre = nombreMayus(txtNombreCobrador.getText());
        String apellidoPaterno = nombreMayus(txtAPCobrador.getText());
        String apellidoMaterno = nombreMayus(txtAMCobrador.getText());
        String telefono = txtTelCobrador.getText();

        Cobrador nuevoCobrador = new Cobrador(
            "",  // El ID se generará automáticamente en la base de datos
            nombre,
            apellidoPaterno,
            apellidoMaterno,
            telefono
        );

        boolean validar = registroCobrador.agregarCobradorBD(nuevoCobrador);

        if (!validar) {
            JOptionPane.showMessageDialog(null, "El teléfono ya existe");
        } else {
            JOptionPane.showMessageDialog(null, "El Cobrador ha sido agregado.");
        }

        // Actualizar la tabla en la interfaz gráfica
        actualizarTablaCobradores();

        // Limpiar los campos del formulario
        limpiarCamposCobrador();
    }//GEN-LAST:event_btnAgregarCobradorActionPerformed

    private void txtNombreCobradorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreCobradorKeyTyped
        if (txtNombreCobrador.getText().length()>=15) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreCobradorKeyTyped

    private void txtTelCobradorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelCobradorKeyTyped
        if (txtTelCobrador.getText().length()>=10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtTelCobradorKeyTyped

    private void txtAMCobradorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAMCobradorKeyTyped
        if (txtAMCobrador.getText().length()>=15) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAMCobradorKeyTyped

    private void txtAPCobradorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAPCobradorKeyTyped
        if (txtAPCobrador.getText().length()>=15) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAPCobradorKeyTyped

    private void RefrescarCobradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefrescarCobradorActionPerformed
        actualizarTablaCobradores();
    }//GEN-LAST:event_RefrescarCobradorActionPerformed

    private void btnEliminarCobradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarCobradorActionPerformed
        if (!txtIDCobrador.getText().isEmpty()) {
            String id = txtIDCobrador.getText();
            int n = JOptionPane.showConfirmDialog(null, "¿Seguro que quiere eliminar al cobrador?", "Eliminar Cobrador", JOptionPane.YES_NO_OPTION);
            if (n == 0) {
                try {
                    registroCobrador.eliminarCobrador(id);
                    JOptionPane.showMessageDialog(null, "El Cobrador Ha Sido Eliminado");
                    actualizarTablaCobradores();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el cobrador de la base de datos: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un cobrador");
        }
    }//GEN-LAST:event_btnEliminarCobradorActionPerformed

    private void btnLimpiarCamposClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCamposClientesActionPerformed
        limpiarCamposCliente();
    }//GEN-LAST:event_btnLimpiarCamposClientesActionPerformed

    private void txtBuscarClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarClienteKeyTyped
        if (txtBuscarCliente.getText().length() >= 15) {
            evt.consume();
        }
    }//GEN-LAST:event_txtBuscarClienteKeyTyped

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed

        String nombreCliente = txtBuscarCliente.getText().trim();

        if (nombreCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un nombre para buscar.");
            return;
        }

        ArrayList<Cliente> resultado = registroCobrador.buscarClientePorNombre(nombreCliente);

        if (resultado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron clientes con ese nombre.");
        } else {
            mostrarClientes(resultado);
        }
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        inicioSesion volver = new inicioSesion();
        volver.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void btnEditarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarClienteActionPerformed
        limpiarLablesCliente(); // Limpiar etiquetas de error
        if (!validarCamposCliente() || !validarTelefonoCliente()) {
            return;  // Validar los campos del formulario
        }

        // Obtener datos del formulario
        String id = txtIDCliente.getText();
        String nombre = txtNombreCliente.getText();
        String apellidoPaterno = txtAPCliente.getText();
        String apellidoMaterno = txtAMCliente.getText();
        float saldoActual = Float.parseFloat(txtSaldoCliente.getText());
        String telefono = txtTelCliente.getText();
        boolean presentaDeuda = DBDeudaCliente.isSelected();

        // Crear un objeto Cliente con los datos actualizados
        Cliente clienteActualizado = new Cliente(
            id,
            nombre,
            apellidoPaterno,
            apellidoMaterno,
            saldoActual,
            telefono,
            presentaDeuda
        );

        try {
            registroCobrador.editarCliente(id, clienteActualizado); // Actualizar cliente en la base de datos
            JOptionPane.showMessageDialog(null, "El Cliente Ha Sido Editado");
            actualizarTablaClientes();  // Actualizar la tabla en la interfaz gráfica
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al editar el cliente en la base de datos: " + e.getMessage());
        }
    }//GEN-LAST:event_btnEditarClienteActionPerformed

    private void btnAgregarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarClienteActionPerformed
        limpiarLablesCliente();

        if (!validarCamposCliente() || !validarTelefonoCliente()) {
            return;
        }

        // Obtener los datos del formulario
        String nombre = nombreMayus(txtNombreCliente.getText());
        String apellidoPaterno = nombreMayus(txtAPCliente.getText());
        String apellidoMaterno = nombreMayus(txtAMCliente.getText());
        float saldoActual = Float.parseFloat(txtSaldoCliente.getText());
        String telefono = txtTelCliente.getText();
        boolean presentaDeuda = DBDeudaCliente.isSelected();

        // Crear un objeto Cliente con los datos capturados
        Cliente nuevoCliente = new Cliente(
            "",  // El ID se generará automáticamente en la base de datos
            nombre,
            apellidoPaterno,
            apellidoMaterno,
            saldoActual,
            telefono,
            presentaDeuda
        );

        // Agregar el cliente a la base de datos
        boolean validar = registroCobrador.agregarClienteBD(nuevoCliente);  // Llamada al método para agregar a la base de datos

        if (!validar) {
            JOptionPane.showMessageDialog(null, "El teléfono ya existe");
        }else{
            JOptionPane.showMessageDialog(null, "El Cliente ha sido agregado.");
        }

        // Actualizar la tabla en la interfaz gráfica
        actualizarTablaClientes();

        // Limpiar los campos del formulario
        limpiarCamposCliente();
    }//GEN-LAST:event_btnAgregarClienteActionPerformed

    private void txtNombreClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreClienteKeyTyped
        if (txtNombreCliente.getText().length() >= 15) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreClienteKeyTyped

    private void txtTelClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelClienteKeyTyped
        if (txtTelCliente.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtTelClienteKeyTyped

    private void txtTelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelClienteActionPerformed
        
    }//GEN-LAST:event_txtTelClienteActionPerformed

    private void txtSaldoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSaldoClienteKeyTyped
        if (txtSaldoCliente.getText().length() >= 8) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSaldoClienteKeyTyped

    private void txtAMClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAMClienteKeyTyped
        if (txtAMCliente.getText().length() >= 15) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAMClienteKeyTyped

    private void txtAPClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAPClienteKeyTyped
        if (txtAPCliente.getText().length() >= 15) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAPClienteKeyTyped

    private void RefrescarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefrescarClienteActionPerformed
        // TODO add your handling code here:
        actualizarTablaClientes();
    }//GEN-LAST:event_RefrescarClienteActionPerformed

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
         if (!txtIDCliente.getText().isEmpty()) {
                
            String id = txtIDCliente.getText();
            int n = JOptionPane.showConfirmDialog(null, "¿Seguro que quiere eliminar el cliente?", "Eliminar Cliente", JOptionPane.YES_NO_OPTION);
            if (n == 0) {
                try {
                    registroCobrador.eliminarCliente(id); // Eliminar cliente de la base de datos
                    JOptionPane.showMessageDialog(null, "El Cliente Ha Sido Eliminado");
                    actualizarTablaClientes();  // Actualizar la tabla en la interfaz gráfica
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el cliente de la base de datos: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente");
        }
        
                        
        
    }//GEN-LAST:event_btnEliminarClienteActionPerformed

    private void txtTotalEnDeudaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalEnDeudaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalEnDeudaKeyTyped

    private void btnAgregarCorte2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCorte2ActionPerformed
        mostrarTotales();
    }//GEN-LAST:event_btnAgregarCorte2ActionPerformed

    private void txtIDClienteCorteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDClienteCorteKeyTyped
        if (txtIDClienteCorte.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtIDClienteCorteKeyTyped

    private void btnMostrarTodosAsignarCobradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodosAsignarCobradorActionPerformed
          DefaultTableModel modelo = (DefaultTableModel) tablaAsignarCobrador.getModel();
    modelo.setRowCount(0);

    // Obtener todas las asignaciones
    RegistroCobrador registroCobrador = new RegistroCobrador();
    List<AsignarCobrador> asignaciones = registroCobrador.obtenerTodasAsignaciones();

    // Formatear la fecha en el formato deseado (yyyy-MM-dd)
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Llenar la tabla con los datos
    for (AsignarCobrador asignacion : asignaciones) {
        // Formatear la fecha
        String fechaFormateada = sdf.format(asignacion.getFecha());

        Object[] fila = {
            asignacion.getIdCliente(),
            asignacion.getNombreCliente(),
            asignacion.getIdCobrador(),
            asignacion.getNombreCobrador(),
            fechaFormateada // Mostrar la fecha formateada
        };
        modelo.addRow(fila);
    }
    }//GEN-LAST:event_btnMostrarTodosAsignarCobradorActionPerformed

    private void txtIDClienteAsignarCobradorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDClienteAsignarCobradorKeyTyped
        if (txtIDClienteAsignarCobrador.getText().length() >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtIDClienteAsignarCobradorKeyTyped

    private void txtIDCobradorAsignarCobradorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDCobradorAsignarCobradorKeyTyped
        if (txtIDCobradorAsignarCobrador.getText().length() >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtIDCobradorAsignarCobradorKeyTyped

    private void btnAgregarAsignarCobradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarAsignarCobradorActionPerformed
        if (!validarIDsAsignarCobrador()) {
            return;
        }
                                                               
    String idCliente = txtIDClienteAsignarCobrador.getText();
    String idCobrador = txtIDCobradorAsignarCobrador.getText();
    java.util.Date fechaUtil = fechaAsignarCobrador.getDate();

    // Validar que los campos no estén vacíos
    if (idCliente.isEmpty() || idCobrador.isEmpty() || fechaUtil == null) {
        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Convertir java.util.Date a java.sql.Date
    java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());

    // Llamar al método de RegistroCobrador para agregar la asignación
    RegistroCobrador registroCobrador = new RegistroCobrador();
    boolean resultado = registroCobrador.agregarAsignacion(idCliente, idCobrador, fechaSql);

    if (resultado) {
        JOptionPane.showMessageDialog(this, "Asignación agregada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        limpiarCamposAsignarCobrador();  
        actualizarTablaAsignarCobrador();  
    } else {
        JOptionPane.showMessageDialog(this, "Error al agregar la asignación", "Error", JOptionPane.ERROR_MESSAGE);
    }
     
    
    }//GEN-LAST:event_btnAgregarAsignarCobradorActionPerformed

    private void btnCerrarSesion5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesion5ActionPerformed
        cerrarSesion();
    }//GEN-LAST:event_btnCerrarSesion5ActionPerformed

    private void btnLimpiarCamposAsignarCobradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCamposAsignarCobradorActionPerformed
        limpiarCamposAsignarCobrador();
    }//GEN-LAST:event_btnLimpiarCamposAsignarCobradorActionPerformed

    private void btnMostrarAsignarCobradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarAsignarCobradorActionPerformed
                                                                    
    // Obtener la fecha actual
    java.util.Date fechaHoy = new java.util.Date();
    java.sql.Date sqlFechaHoy = new java.sql.Date(fechaHoy.getTime());

    // Limpiar la tabla
    DefaultTableModel modelo = (DefaultTableModel) tablaAsignarCobrador.getModel();
    modelo.setRowCount(0);

    // Obtener las asignaciones pendientes para la fecha actual
    RegistroCobrador registroCobrador = new RegistroCobrador();
    List<AsignarCobrador> asignaciones = registroCobrador.obtenerAsignacionesPorFecha(sqlFechaHoy);

    // Llenar la tabla con los datos
    for (AsignarCobrador asignacion : asignaciones) {
        Object[] fila = {
            asignacion.getIdCliente(),
            asignacion.getNombreCliente(),
            asignacion.getIdCobrador(),
            asignacion.getNombreCobrador(),
            asignacion.getFecha()
        };
        modelo.addRow(fila);
    }

    // Mostrar mensaje si no hay pendientes
    if (asignaciones.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay pendientes para el día de hoy.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }



    }//GEN-LAST:event_btnMostrarAsignarCobradorActionPerformed

    private void btnClientesLiquidadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesLiquidadosActionPerformed
        List<Cliente> clientesLiquidados = registroCobrador.obtenerClientesPorDeuda(false); // false indica no presenta deuda
    actualizarTablaClientes(clientesLiquidados);
    }//GEN-LAST:event_btnClientesLiquidadosActionPerformed

    private void btnClientesDeudoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesDeudoresActionPerformed
        List<Cliente> clientesDeudores = registroCobrador.obtenerClientesPorDeuda(true); // true indica que presentan deuda
    actualizarTablaClientes(clientesDeudores);
    }//GEN-LAST:event_btnClientesDeudoresActionPerformed

    private void btnBuscarTransaccionesClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarTransaccionesClienteActionPerformed

    try {
        int clienteId = Integer.parseInt(txtBuscarTransaccionesCliente.getText().trim());

        // Llamar al método en RegistroCobrador
        RegistroCobrador registroCobrador = new RegistroCobrador();
        List<Transaccion> transacciones = registroCobrador.buscarTransaccionesPorCliente(clienteId);

        // Limpiar la tabla antes de llenarla
        DefaultTableModel model = (DefaultTableModel) tablaCorte.getModel();
        model.setRowCount(0);

        // Llenar la tabla con los datos obtenidos
        for (Transaccion t : transacciones) {
            model.addRow(new Object[]{
                t.getId(),
                t.getIdCliente(),
                t.getIdCobrador(),
                t.getMontoPagado(),
                t.getFecha()
                
                
            });
        }

        // Mostrar mensaje si no hay resultados
        if (transacciones.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron transacciones para el ID del cliente: " + clienteId);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Por favor, ingresa un ID de cliente válido.");
    }

    }//GEN-LAST:event_btnBuscarTransaccionesClienteActionPerformed

    private void btnBuscarTransaccionesCobradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarTransaccionesCobradorActionPerformed
    try {
        int cobradorId = Integer.parseInt(txtBuscarTransaccionesCobrador.getText().trim());

        // Llamar al método en RegistroCobrador
        RegistroCobrador registroCobrador = new RegistroCobrador();
        List<Transaccion> transacciones = registroCobrador.buscarTransaccionesPorCobrador(cobradorId);

        // Limpiar la tabla antes de llenarla
        DefaultTableModel model = (DefaultTableModel) tablaCorte.getModel();
        model.setRowCount(0);

        // Llenar la tabla con los datos obtenidos
        for (Transaccion t : transacciones) {
            model.addRow(new Object[]{
                t.getId(),
                t.getIdCliente(),
                t.getIdCobrador(),
                t.getMontoPagado(),
                t.getFecha()
            });
        }

        // Mostrar mensaje si no hay resultados
        if (transacciones.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron transacciones para el ID del cobrador: " + cobradorId);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Por favor, ingresa un ID de cobrador válido.");
    }

            
    }//GEN-LAST:event_btnBuscarTransaccionesCobradorActionPerformed

    private void txtBuscarTransaccionesClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarTransaccionesClienteFocusGained
        if (txtBuscarTransaccionesCliente.getText().equals("ID")) {
            txtBuscarTransaccionesCliente.setText(""); 
            txtBuscarTransaccionesCliente.setForeground(java.awt.Color.BLACK);
        }
    }//GEN-LAST:event_txtBuscarTransaccionesClienteFocusGained

    private void txtBuscarTransaccionesClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarTransaccionesClienteFocusLost
        if (txtBuscarTransaccionesCliente.getText().isEmpty()) {
            txtBuscarTransaccionesCliente.setText("ID"); 
            txtBuscarTransaccionesCliente.setForeground(java.awt.Color.GRAY); 
        }
    }//GEN-LAST:event_txtBuscarTransaccionesClienteFocusLost

    private void txtBuscarTransaccionesClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarTransaccionesClienteKeyTyped
        if (txtBuscarTransaccionesCliente.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtBuscarTransaccionesClienteKeyTyped

    private void txtBuscarTransaccionesCobradorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarTransaccionesCobradorKeyTyped
        if (txtBuscarTransaccionesCobrador.getText().length() >= 7) {
            evt.consume();
        }
    }//GEN-LAST:event_txtBuscarTransaccionesCobradorKeyTyped

    private void txtBuscarTransaccionesCobradorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarTransaccionesCobradorFocusGained
        if (txtBuscarTransaccionesCobrador.getText().equals("ID")) {
            txtBuscarTransaccionesCobrador.setText("");
            txtBuscarTransaccionesCobrador.setForeground(java.awt.Color.BLACK);
        }
    }//GEN-LAST:event_txtBuscarTransaccionesCobradorFocusGained

    private void txtBuscarTransaccionesCobradorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarTransaccionesCobradorFocusLost
        if (txtBuscarTransaccionesCobrador.getText().isEmpty()) {
            txtBuscarTransaccionesCobrador.setText("ID");
            txtBuscarTransaccionesCobrador.setForeground(java.awt.Color.GRAY); 
        }
    }//GEN-LAST:event_txtBuscarTransaccionesCobradorFocusLost
        
    private void mostrarClientes(ArrayList<Cliente> resultado) {
    DefaultTableModel modelo = (DefaultTableModel) tablaCliente.getModel();
    modelo.setRowCount(0); 
    for (Cliente cliente : resultado) {
        Object[] fila = {
            cliente.getId(),
            cliente.getNombre(),
            cliente.getApellidoPaterno(),
            cliente.getApellidoMaterno(),
            cliente.getSaldoActual(),
            cliente.getTelefono(),
            cliente.isDeuda()? "Sí" : "No"
        };
        modelo.addRow(fila);
    }
}

    private void mostrarCobradores(ArrayList<Cobrador> resultado) {
    DefaultTableModel modelo = (DefaultTableModel) tablaCobrador.getModel();
    modelo.setRowCount(0); // Limpiar la tabla antes de mostrar nuevos resultados

    // Recorrer la lista de cobradores y agregarlos a la tabla
    for (Cobrador cobrador : resultado) {
        Object[] fila = {
            cobrador.getId(),
            cobrador.getNombre(),
            cobrador.getApellidoPaterno(),
            cobrador.getApellidoMaterno(),
            cobrador.getTelefono(),
        };
        modelo.addRow(fila);
    }
}

    

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new vistaUsuario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox DBDeudaCliente;
    private javax.swing.JPanel PanelCobradores;
    private javax.swing.JPanel PanelCobros;
    private javax.swing.JPanel PanelCorte;
    private javax.swing.JPanel PanelInformes;
    private javax.swing.JButton RefrescarCliente;
    private javax.swing.JButton RefrescarCobrador;
    private javax.swing.JButton RefrescarCobrador1;
    private javax.swing.JButton btnAgregarAsignarCobrador;
    private javax.swing.JButton btnAgregarCliente;
    private javax.swing.JButton btnAgregarCobrador;
    private javax.swing.JButton btnAgregarCorte;
    private javax.swing.JButton btnAgregarCorte2;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnBuscarCobrador;
    private javax.swing.JButton btnBuscarTransaccionesCliente;
    private javax.swing.JButton btnBuscarTransaccionesCobrador;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnCerrarSesion1;
    private javax.swing.JButton btnCerrarSesion2;
    private javax.swing.JButton btnCerrarSesion5;
    private javax.swing.JButton btnCerrarSesion6;
    private javax.swing.JButton btnClientesDeudores;
    private javax.swing.JButton btnClientesLiquidados;
    private javax.swing.JButton btnEditarCliente;
    private javax.swing.JButton btnEditarCobrador;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnEliminarCobrador;
    private javax.swing.JButton btnLimpiarCamposAsignarCobrador;
    private javax.swing.JButton btnLimpiarCamposClientes;
    private javax.swing.JButton btnLimpiarCamposCobrador;
    private javax.swing.JButton btnLimpiarCamposCorte;
    private javax.swing.JButton btnMostrarAsignarCobrador;
    private javax.swing.JButton btnMostrarTodosAsignarCobrador;
    private com.toedter.calendar.JCalendar fechaAsignarCobrador;
    private com.toedter.calendar.JCalendar fechaCorte;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel lblAMCliente;
    private javax.swing.JLabel lblAMCobrador;
    private javax.swing.JLabel lblAPCliente;
    private javax.swing.JLabel lblAPCobrador;
    private javax.swing.JLabel lblIDClienteAsignarCobrador;
    private javax.swing.JLabel lblIDClienteCorte;
    private javax.swing.JLabel lblIDCobradorAsignarCobrador;
    private javax.swing.JLabel lblIDCobradorCorte;
    private javax.swing.JLabel lblMontoPagadoCorte;
    private javax.swing.JLabel lblNombreCliente;
    private javax.swing.JLabel lblNombreCobrador;
    private javax.swing.JLabel lblSaldoCliente;
    private javax.swing.JLabel lblTelCliente;
    private javax.swing.JLabel lblTelCliente1;
    private javax.swing.JLabel lblTelCliente3;
    private javax.swing.JLabel lblTelCliente4;
    private javax.swing.JLabel lblTelCliente7;
    private javax.swing.JLabel lblTelCobrador;
    private javax.swing.JPanel panelClientes;
    private javax.swing.JPanel panelCorte;
    private javax.swing.JTabbedPane pestañas;
    private javax.swing.JTable tablaAsignarCobrador;
    private javax.swing.JTable tablaCliente;
    private javax.swing.JTable tablaCobrador;
    private javax.swing.JTable tablaCorte;
    private javax.swing.JTextField txtAMCliente;
    private javax.swing.JTextField txtAMCobrador;
    private javax.swing.JTextField txtAPCliente;
    private javax.swing.JTextField txtAPCobrador;
    private javax.swing.JTextField txtBuscarCliente;
    private javax.swing.JTextField txtBuscarCobrador;
    private javax.swing.JTextField txtBuscarTransaccionesCliente;
    private javax.swing.JTextField txtBuscarTransaccionesCobrador;
    private javax.swing.JTextField txtIDCliente;
    private javax.swing.JTextField txtIDClienteAsignarCobrador;
    private javax.swing.JTextField txtIDClienteCorte;
    private javax.swing.JTextField txtIDCobrador;
    private javax.swing.JTextField txtIDCobradorAsignarCobrador;
    private javax.swing.JTextField txtIDCobradorCorte;
    private javax.swing.JTextField txtMontoPagadoCorte;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreCobrador;
    private javax.swing.JTextField txtSaldoCliente;
    private javax.swing.JTextField txtTelCliente;
    private javax.swing.JTextField txtTelCobrador;
    private javax.swing.JTextField txtTotalEnDeuda;
    private javax.swing.JTextField txtTotalSaldado;
    // End of variables declaration//GEN-END:variables

    

    

    
}
