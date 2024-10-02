package dev.servrest.modulos.pojo;

import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UsuarioPojo {

    private String email;
    private String password;
    static Response response;

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

    public static List<String> listaDeIDUsuarios() {

        response = given().when().get("/usuarios").then().extract().response();
        List<String> listaUsuarios = response.jsonPath().getList("usuarios._id");
        return listaUsuarios;
    }
}
