package dev.servrest.modulos.usuario;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class UsuarioTest {

    Response response;


    @Test
    @DisplayName("Exibir lista de usuários cadastrados")
    public void testExibirListaDeUsuariosCadastrados() {

        baseURI = "http://localhost";
        port = 3000;

        this.response = given().
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

        baseURI = "http://localhost";
        port = 3000;

        this.response = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"fulano@qa.com\",\n" +
                        "  \"password\": \"teste\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .assertThat()
                .body("message", equalTo("Login realizado com sucesso"))
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Validar Login com email inválido")
    public void testRealizarLoginComEmailInvalido() {

        baseURI = "http://localhost";
        port = 3000;

        this.response = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"fulano\",\n" +
                        "  \"password\": \"teste\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .assertThat()
                .body("email", equalTo("email deve ser um email válido"))
                .statusCode(400)
                .extract().response();

        System.out.println(response.asString());
    }

    @Test
    @DisplayName("Validar Login com senha em branco")
    public void testRealizarLoginComSenhaEmBranco() {

        baseURI = "http://localhost";
        port = 3000;

        this.response = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"fulano@qa.com\",\n" +
                        "  \"password\": \"\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .assertThat()
                .body("password", equalTo("password não pode ficar em branco"))
                .statusCode(400)
                .extract().response();

        System.out.println(response.asString());
    }

    @Test
    @DisplayName("Validar Login com email em branco")
    public void testRealizarLoginComEmailEmBranco() {

        baseURI = "http://localhost";
        port = 3000;

        this.response = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"\",\n" +
                        "  \"password\": \"teste\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .assertThat()
                .body("email", equalTo("email não pode ficar em branco"))
                .statusCode(400)
                .extract().response();

        System.out.println(response.asString());
    }

    @Test
    @DisplayName("Validar Login com senha incorreta")
    public void testRealizarLoginComSenhaIncorreta() {

        baseURI = "http://localhost";
        port = 3000;

        this.response = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"fulano@qa.com\",\n" +
                        "  \"password\": \"teste123\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .assertThat()
                .body("message", equalTo("Email e/ou senha inválidos"))
                .statusCode(401)
                .extract().response();

        System.out.println(response.asString());
    }


}
