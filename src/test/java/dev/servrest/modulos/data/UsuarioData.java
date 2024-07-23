package dev.servrest.modulos.data;

import dev.servrest.modulos.pojo.UsuarioPojo;

public class UsuarioData {


    public static UsuarioPojo realizarLoginComUsuario(String email, String password) {
        UsuarioPojo usuario = new UsuarioPojo();
        usuario.setEmail(email);
        usuario.setPassword(password);
        return usuario;

    }

}
