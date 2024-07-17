package dev.servrest.modulos.tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Testes que validam a classe de Login")
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
                        "  \"email\": \"testesnovo@qa.com.br\",\n" +
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

    @Test
    @DisplayName("Validar que não permite chamada da requisição para fazer Login com método GET")
    public void testValidarQueNaoPermiteChamarRequisicaoDeLoginComMetodoPost() {

        baseURI = "http://localhost";
        port = 3000;

        this.response = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"fulano@qa.com\",\n" +
                        "  \"password\": \"teste\"\n" +
                        "}")
                .when()
                .get("/login")
                .then()
                .assertThat()
                .body("message", equalTo("Não é possível realizar GET em /login. Acesse http://localhost:3000 para ver as rotas disponíveis e como utilizá-las."))
                .statusCode(405)
                .extract().response();

        System.out.println(response.asString());
    }

    @Test
    @DisplayName("Validar que não permite fazer cadastro com um usuário já cadastrado no sistema")
    public void testValidarQueCadastrarUsuarioComDadosJaUtilizados() {

        baseURI = "http://localhost";
        port = 3000;

        this.response = given().
                contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"nome\": \"Fulano da Silva\",\n" +
                        "  \"email\": \"beltrano@qa.com.br\",\n" +
                        "  \"password\": \"teste\",\n" +
                        "  \"administrador\": \"true\"\n" +
                        "}")
                .when()
                .post("/usuarios")
                .then()
                .assertThat()
                .body("message", equalTo("Este email já está sendo usado"))
                .extract().response();

        System.out.println(response.asString());
    }

    @Test
    @DisplayName("Excluir um usuário existente")
    public void testExcluirUmUsuarioExistente() {

        baseURI = "http://localhost";
        port = 3000;

        given()
                .pathParam("_id", "1boS3Vbhu42nx3vW")
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"))
                .log().all();

    }

    @Test
    @DisplayName("Não permite excluir usuário com item no carrinho")
    public void testNaoPermitirExcluirUsuarioComItemNoCarrinho() {

        baseURI = "http://localhost";
        port = 3000;

        given()
                .pathParam("_id", "0uxuPY0cbmQhpEz1")
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .statusCode(400)
                .body("message", equalTo("Não é permitido excluir usuário com carrinho cadastrado"))
                .log().all();

    }

    @Test
    @DisplayName("Buscar usuário cadastrado por nome")
    public void testBuscarUsuarioFiltrandoPorNome() {

        baseURI = "http://localhost";
        port = 3000;

        this.response = given()
                .param("nome", "Fulano")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Buscar usuário cadastrado por nome não encontrado")
    public void testBuscarUsuarioFiltrandoPorNomeNaoEncontrado() {

        baseURI = "http://localhost";
        port = 3000;

        this.response = given()
                .param("nome", "Teste")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .body("quantidade", equalTo(0))
                .extract().response();

        System.out.println(response.asString());

    }

}
