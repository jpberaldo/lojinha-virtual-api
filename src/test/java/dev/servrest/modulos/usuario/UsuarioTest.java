package dev.servrest.modulos.usuario;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UsuarioTest {

    @Test
    @DisplayName("Exibir lista de usuários cadastrados")
    public void testExibirListaDeUsuariosCadastrados() {

        baseURI = "http://localhost:3000";

        Response response = given().
                when().
                get("/usuarios")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Validar Login com usuário válido")
    public void testRealizarLoginComDadosValidos() {

        baseURI = "http://localhost:3000";

        Response response = given().
                when().
                get("/usuarios")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }
}
