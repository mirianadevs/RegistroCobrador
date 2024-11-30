
package unison.registrocobrador;

public class Usuario {
     private String Id;
    private String Usuario;
    private String contraseña;
    private boolean esAdmin;
    
    public Usuario(String Id, String Usuario, String contraseña, boolean esAdmin) {
        this.Id = Id;
        this.Usuario = Usuario;
        this.contraseña = contraseña;
        this.esAdmin = esAdmin;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public String getId() {
        return Id;
    }

    public String getUsuario() {
        return Usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public boolean esAdmin() {
        return esAdmin;
    }

    
   
}
