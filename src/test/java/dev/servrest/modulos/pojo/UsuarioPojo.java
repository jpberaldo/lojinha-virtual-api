package dev.servrest.modulos.pojo;

import dev.servrest.modulos.data.UsuarioData;
import io.restassured.http.ContentType;
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
        return response.jsonPath().getList("usuarios._id");
    }

    public static String gerarDadosParaSelecaoDeUsuario(int usuario) {
        response = given().when().get("/usuarios").then().extract().response();
        return response.jsonPath().getString("usuarios[" + usuario + "]._id");
    }

    public static String gerarDadosDeTokenDoUsuario(String email, String senha) {

        response = given()
                .contentType(ContentType.JSON)
                .body(UsuarioData.realizarLoginComUsuario(email, senha))
                .when()
                .post("/login")
                .then().extract().response();

        return response.jsonPath().getString("authorization");
    }

}
