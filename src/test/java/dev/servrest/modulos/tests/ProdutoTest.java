package dev.servrest.modulos.tests;

import com.github.javafaker.Faker;
import dev.servrest.modulos.data.CadastroData;
import dev.servrest.modulos.data.ProdutoData;
import dev.servrest.modulos.data.UsuarioData;
import dev.servrest.modulos.pojo.ProdutoPojo;
import dev.servrest.modulos.utils.Service;
import dev.servrest.modulos.utils.Token;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.util.Locale;

import static dev.servrest.modulos.data.FactoryData.setDados;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes que validam a classe de Produto")
public class ProdutoTest {

    private String userId = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZ1bGFub0BxYS5jb20iLCJwYXNzd29yZCI6InRlc3RlIiwiaWF0IjoxNzIzNDk1MzU4LCJleHAiOjE3MjM0OTU5NTh9.rAXikTgF333Ft_-g1tavN9kmcvSCAzXspRJFMzcIIpQ";
    Response response;
    Faker dados = new Faker(new Locale("pt-BR"));

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

//        String token = given().
//                contentType(ContentType.JSON)
//                .body("{\n" +
//                        "  \"email\": \"fulano@qa.com\",\n" +
//                        "  \"password\": \"teste\"\n" +
//                        "}")
//                .when()
//                .post("/login")
//                .then()
//                .extract().path("authorization");

        Token token = Token.VALIDO;

        System.out.println(token);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("authorization", token.getToken())
                .body(ProdutoData.cadastrarProduto(setDados(dados.food().dish()), "XXX", Integer.parseInt(setDados(String.valueOf(dados.random().nextInt(10000)))), 10))
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
                .body(UsuarioData.realizarLoginComUsuario("fulano@qa.com", "teste"))
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

        Response response = given()
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario("teste@xd.com", "teste"))
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

        Response response = given()
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario("teste@xd.com", "teste"))
                .body(ProdutoData.cadastrarProduto("Logitech GPRO Yellow", "XXX", 150, 20))
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
                .pathParam("_id", Service.gerarProdutoId(0))
                .when()
                .delete("/produtos/{_id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"))
                .log().all();

    }

    @Test
    @Order(9)
    @DisplayName("Alterar o nome de um produto com sucesso")
    public void testAlterarNomeDeProdutoComSucesso() {

        given()
                .pathParam("_id", Service.gerarProdutoId(0))
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario("fulano@qa.com", "teste"))
                .body(ProdutoData.cadastrarProduto("Teste T3", "Alterando descricao", 210, 10))
                .when()
                .put("/produtos/{_id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("message", equalTo("Registro alterado com sucesso"))
                .log().all();

    }

    @Test
    @Order(10)
    @DisplayName("Validar que nao permite alterar nome de produto para um existente")
    public void testAlterarNomeDeProdutoComNaoSucesso() {

        given()
                .pathParam("_id", "0uxuPY0cbmQhpEz1")
                .contentType(ContentType.JSON)
                .header("authorization", userId)
                .body(ProdutoData.cadastrarProduto("Logitech GPRO Purple Novo", "Mouse Alterado", 210, 5))
                .when()
                .put("/produtos/{_id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Já existe produto com esse nome"))
                .log().all();

    }

    @Test
    @Order(11)
    @DisplayName("Validar que nao permite alterar produto com token invalido")
    public void testAlterarNomeProdutoComTokenInvalido() {

        given()
                .pathParam("_id", "0uxuPY0cbmQhpEz1")
                .contentType(ContentType.JSON)
                .header("authorization", "")
                .body(ProdutoData.cadastrarProduto("Logitech GPRO Purple Novo", "Mouse Alterado", 210, 5))
                .when()
                .put("/produtos/{_id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
                .log().all();

    }


    @Test
    @Order(12)
    @DisplayName("Validar que nao permite alterar produto com token de usuário inválido")
    public void testAlterarNomeProdutoComTokenDeUsuarioInvalido() {

        given()
                .pathParam("_id", "abc")
                .contentType(ContentType.JSON)
                .header("authorization", userId)
                .body(ProdutoData.cadastrarProduto("Logitech GPRO Purple Novo", "Mouse Alterado", 210, 5))
                .when()
                .put("/produtos/{_id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
                .log().all();

    }


}
