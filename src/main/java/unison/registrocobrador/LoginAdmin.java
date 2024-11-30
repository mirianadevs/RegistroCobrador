package unison.registrocobrador;
import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class LoginAdmin extends JFrame {

    private JLabel labelAdmin;
    private JPasswordField txtPassword;
    private JButton btnConfirmar, btnCerrar;
    private int intentosFallidos = 0;
    private Timer cooldownTimer;
    private boolean enCooldown = false;

    public LoginAdmin() {
        // Configuración de la ventana
        setTitle("Acceso de Administrador");
        setSize(300, 200);
        setLocationRelativeTo(null); // Centrar la ventana

        // Cambiar el comportamiento de cierre de la ventana para que no cierre todo el programa
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Agregar un listener para el evento de cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // En vez de cerrar, volver a la ventana de inicio de sesión
                cerrarVentana();
            }
        });

        // Crear componentes
        labelAdmin = new JLabel("Ingrese la contraseña de admin:");
        txtPassword = new JPasswordField(15);
        btnConfirmar = new JButton("Confirmar");
        btnCerrar = new JButton("Cancelar");

        // Panel para agregar componentes
        JPanel panel = new JPanel();
        panel.add(labelAdmin);
        panel.add(txtPassword);
        panel.add(btnConfirmar);
        panel.add(btnCerrar);
        add(panel);

        // Evento del botón Confirmar
        btnConfirmar.addActionListener(evt -> validarAcceso());
        btnCerrar.addActionListener(evt -> cerrarVentana());

    }

    // Método para validar el acceso
    private void validarAcceso() {
        if (enCooldown) {
            JOptionPane.showMessageDialog(this, "Demasiados intentos.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String password = String.valueOf(txtPassword.getPassword());

        // Contraseña predeterminada de admin
        String adminPassword = "1234";  // Puedes cambiar esta contraseña

        if (password.equals(adminPassword)) {
            // Si la contraseña es correcta, abrir la ventana de registrarUsuario
            registrarUsuario ventana = new registrarUsuario();
            ventana.setVisible(true);
            this.dispose(); // Cerrar la ventana actual
        } else {
            // Si la contraseña es incorrecta, incrementar el contador de intentos fallidos
            intentosFallidos++;
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta. " , "Error", JOptionPane.ERROR_MESSAGE);

            // Si alcanza 4 intentos fallidos, activar cooldown
            if (intentosFallidos >= 4) {
                activarCooldown();
            }
        }
    }

    // Método para cerrar la ventana y redirigir a la pantalla de inicio de sesión
    private void cerrarVentana() {
        inicioSesion ventanaInicio = new inicioSesion(); // Asumiendo que tienes una ventana llamada IniciarSesion
        ventanaInicio.setVisible(true);
        this.dispose(); // Cerrar la ventana actual
    }

    // Método para activar el cooldown de 1 minuto
    private void activarCooldown() {
        enCooldown = true;
        btnConfirmar.setEnabled(false); // Deshabilitar el botón Confirmar
        intentosFallidos = 0; // Reiniciar el contador de intentos fallidos

        // Mostrar mensaje de cooldown
        JOptionPane.showMessageDialog(this, "Has alcanzado el número máximo de intentos.", "Demasiados intentos", JOptionPane.WARNING_MESSAGE);

        // Usar un Timer para implementar el cooldown de 60 segundos
        cooldownTimer = new Timer();
        cooldownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                enCooldown = false;
                btnConfirmar.setEnabled(true); // Rehabilitar el botón Confirmar
            }
        }, 60000); // 60 segundos de cooldown
    }

    public static void main(String[] args) {
        // Para probar la ventana de LoginAdmin
        new LoginAdmin().setVisible(true);
    }
}
