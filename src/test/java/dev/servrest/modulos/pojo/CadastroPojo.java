package dev.servrest.modulos.pojo;

import dev.servrest.modulos.data.CadastroData;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

import static dev.servrest.modulos.data.FactoryData.*;
import static io.restassured.RestAssured.given;

public class CadastroPojo {

    private String nome;
    private String email;
    private String password;
    private String administrador;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public static Map<String, String> cadastroNovoUsuario() {

        given()
                .contentType(ContentType.JSON)
                .body(CadastroData.informarDadosDeCadastro(NOME_RANDOMICO, EMAIL_RANDOMICO, SENHA_RANDOMICA, "true"))
                .log().all()
                .when()
                .post("/usuarios");

        Map<String, String> dadosDoNovoUsuario = new HashMap<>();
        dadosDoNovoUsuario.put("email", EMAIL_RANDOMICO);
        dadosDoNovoUsuario.put("senha", SENHA_RANDOMICA);
        return dadosDoNovoUsuario;

    }
}
