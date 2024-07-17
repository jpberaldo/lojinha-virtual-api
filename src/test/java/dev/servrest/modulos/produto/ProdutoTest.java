package dev.servrest.modulos.produto;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class ProdutoTest {

    Response response;

    @Test
    @DisplayName("Exibir lista de produtos cadastrados")
    public void testExibirListaDeUsuariosCadastrados() {

        baseURI = "http://localhost";
        port = 3000;

        this.response = given().
                when().
                get("/produtos")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Cadastrar novo produto válido")
    public void testCadastrarNovoProdutoValido() {

        baseURI = "http://localhost";
        port = 3000;

        String token = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"fulano@qa.com\",\n" +
                        "  \"password\": \"teste\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .extract().path("authorization");

        System.out.println(token);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body("{\n" +
                        "  \"nome\": \"Logitech GPRO Purple\",\n" +
                        "  \"preco\": 210,\n" +
                        "  \"descricao\": \"Mouse\",\n" +
                        "  \"quantidade\": 5\n" +
                        "}")
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        System.out.println(response);

    }

    @Test
    @DisplayName("Não permite cadastrar produto ja registrado no sistema")
    public void testNaoPermiteCadastrarProdutoJaRegistrado() {

        baseURI = "http://localhost";
        port = 3000;

        String token = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"fulano@qa.com\",\n" +
                        "  \"password\": \"teste\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .extract().path("authorization");

        System.out.println(token);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body("{\n" +
                        "  \"nome\": \"Logitech GPRO Pink\",\n" +
                        "  \"preco\": 600,\n" +
                        "  \"descricao\": \"Mouse\",\n" +
                        "  \"quantidade\": 80\n" +
                        "}")
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .body("message", equalTo("Já existe produto com esse nome"))
                .statusCode(400)
                .extract().response();

        System.out.println(response);

    }

    @Test
    @DisplayName("Não permite cadastrar produto sem enviar o parametro do token corretamente na requisição")
    public void testNaoPermiteCadastrarProdutoSemOParametroTokenCorreto() {

        baseURI = "http://localhost";
        port = 3000;

        String token = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"fulano@qa.com\",\n" +
                        "  \"password\": \"teste\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .extract().path("authorization");

        System.out.println(token);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("authorizatio", token)
                .body("{\n" +
                        "  \"nome\": \"Logitech GPRO Pink\",\n" +
                        "  \"preco\": 600,\n" +
                        "  \"descricao\": \"Mouse\",\n" +
                        "  \"quantidade\": 80\n" +
                        "}")
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
                .statusCode(401)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Não permite cadastrar produto com token inválido")
    public void testNaoPermiteCadastrarProdutoComTokenInvalido() {

        baseURI = "http://localhost";
        port = 3000;

        String token = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"fulano@qa.com\",\n" +
                        "  \"password\": \"teste\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .extract().path("authorization");

        Response response = given()
                .contentType(ContentType.JSON)
                .header("authorization", "a")
                .body("{\n" +
                        "  \"nome\": \"Logitech GPRO Pink\",\n" +
                        "  \"preco\": 600,\n" +
                        "  \"descricao\": \"Mouse\",\n" +
                        "  \"quantidade\": 80\n" +
                        "}")
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
                .statusCode(401)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Não permite cadastrar produto com usuário administrador = false")
    public void testNaoPermiteCadastrarProdutoComADMIgualFalse() {

        baseURI = "http://localhost";
        port = 3000;

        String token = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"testesnovo@qa.com.br\",\n" +
                        "  \"password\": \"teste\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .extract().path("authorization");

        System.out.println(token);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body("{\n" +
                        "  \"nome\": \"Logitech GPRO Yellow\",\n" +
                        "  \"preco\": 150,\n" +
                        "  \"descricao\": \"Mouse\",\n" +
                        "  \"quantidade\": 10\n" +
                        "}")
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .body("message", equalTo("Rota exclusiva para administradores"))
                .statusCode(403)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Não permitir cadastrar produto com usuário inválido")
    public void testNaoPermiteCadastrarProdutoComUsuarioInvalido() {

        baseURI = "http://localhost";
        port = 3000;

        String tempToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRlc3Rlc25vdm9AcWEuY29tLmJyIiwicGFzc3dvcmQiOiJ0ZXN0ZSIsImlhdCI6MTcyMDcyODI4MywiZXhwIjoxNzIwNzI4ODgzfQ.waV6O_a2cj18XZTPyzs5SsZclQ8QvQVYyYjvsgFg1TA";

        Response response = given()
                .contentType(ContentType.JSON)
                .header("authorization", tempToken)
                .body("{\n" +
                        "  \"nome\": \"Logitech GPRO Yellow\",\n" +
                        "  \"preco\": 150,\n" +
                        "  \"descricao\": \"Mouse\",\n" +
                        "  \"quantidade\": 10\n" +
                        "}")
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .body("message", equalTo("Rota exclusiva para administradores"))
                .statusCode(403)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Excluir um produto valido")
    public void testExcluirUmProdutoValido() {

        baseURI = "http://localhost";
        port = 3000;

        given()
                .pathParam("_id", "8fp9Pmu2hUMnLylM")
                .when()
                .delete("/produtos/{_id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"))
                .log().all();

    }



}
