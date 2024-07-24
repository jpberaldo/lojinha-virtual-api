package dev.servrest.modulos.data;

import dev.servrest.modulos.pojo.CadastroPojo;

public class CadastroData {

    public static CadastroPojo informarDadosDeCadastro(String nome, String email, String password, String administrador) {
        CadastroPojo cadastro = new CadastroPojo();
        cadastro.setNome(nome);
        cadastro.setEmail(email);
        cadastro.setPassword(password);
        cadastro.setAdministrador(administrador);
        return cadastro;

    }
}
