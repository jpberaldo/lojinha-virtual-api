package dev.servrest.modulos.data;

import dev.servrest.modulos.pojo.UsuarioPojo;

public class UsuarioData {


    public static UsuarioPojo realizarLoginComUsuarioValido() {

        UsuarioPojo usuario = new UsuarioPojo();
        usuario.setEmail("testesnovo@qa.com.br");
        usuario.setPassword("teste");
        return usuario;

    }

    public static UsuarioPojo realizarLoginComEmailInvalido() {

        UsuarioPojo usuario = new UsuarioPojo();
        usuario.setEmail("testesnovo");
        usuario.setPassword("teste");
        return usuario;

    }
}
