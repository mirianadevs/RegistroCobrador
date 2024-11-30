package unison.registrocobrador;

import java.util.ArrayList;
import java.util.List;

public class UsuarioController {
    private List<Usuario> listaUsuarios;

    // Constructor
    public UsuarioController() {
        this.listaUsuarios = new ArrayList<>();
    }

    // Método para agregar un usuario si no existe
    public String agregarUsuario(Usuario usuario) {
        // Verificar si el usuario ya existe
        for (Usuario u : listaUsuarios) {
            if (u.getUsuario().equals(usuario.getUsuario())) {
                return "Error: Ya existe un usuario con ese nombre.";
            }
        }
        
        // Si no existe, agregar el usuario a la lista
        listaUsuarios.add(usuario);
        return "Usuario agregado correctamente.";
    }

    // Método para listar todos los usuarios (opcional)
    public List<Usuario> obtenerUsuarios() {
        return listaUsuarios;
    }
}
