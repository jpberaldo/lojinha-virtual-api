package dev.servrest.modulos.tests;

import dev.servrest.modulos.data.ProdutoData;
import dev.servrest.modulos.utils.Service;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes que validam a classe de Produto")
public class ProdutoTest {

    Response response;

    @BeforeEach
    public void beforeEach() {
        Service.configurarEnderecoDaRequisicaoChamada();
    }

    @Test
    @Order(1)
    @DisplayName("Exibir lista de produtos cadastrados")
    public void testExibirListaDeUsuariosCadastrados() {

        this.response = given().
                when().
                get("/produtos")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(2)
    @DisplayName("Cadastrar novo produto válido")
    public void testCadastrarNovoProdutoValido() {

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
                .body(ProdutoData.cadastrarProduto("TV", "Para assistir filmes e séries", 1000, 200))
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(3)
    @DisplayName("Não permite cadastrar produto ja registrado no sistema")
    public void testNaoPermiteCadastrarProdutoJaRegistrado() {

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
    @Order(4)
    @DisplayName("Não permite cadastrar produto sem enviar o parametro do token corretamente na requisição")
    public void testNaoPermiteCadastrarProdutoSemOParametroTokenCorreto() {

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
    @Order(5)
    @DisplayName("Não permite cadastrar produto com token inválido")
    public void testNaoPermiteCadastrarProdutoComTokenInvalido() {

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
    @Order(6)
    @DisplayName("Não permite cadastrar produto com usuário administrador = false")
    public void testNaoPermiteCadastrarProdutoComADMIgualFalse() {

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
    @Order(7)
    @DisplayName("Não permitir cadastrar produto com usuário inválido")
    public void testNaoPermiteCadastrarProdutoComUsuarioInvalido() {

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
    @Order(8)
    @DisplayName("Excluir um produto valido")
    public void testExcluirUmProdutoValido() {

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
