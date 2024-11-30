package unison.registrocobrador;

public class UsuarioInicioSesion {
    private String usuario;
    private String contraseña;

    // Constructor
    public UsuarioInicioSesion(String usuario, String contraseña) {
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    // Getters
    public String getUsuario() {
        return usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    // Setters
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    // Método para validar las credenciales
    public boolean validarCredenciales(String usuarioIngresado, String contraseñaIngresada) {
        return this.usuario.equals(usuarioIngresado) && this.contraseña.equals(contraseñaIngresada);
    }
}