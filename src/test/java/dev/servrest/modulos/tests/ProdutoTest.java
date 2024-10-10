package dev.servrest.modulos.tests;

import dev.servrest.modulos.data.ProdutoData;
import dev.servrest.modulos.data.UsuarioData;
import dev.servrest.modulos.utils.Service;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
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
    public void testExibirListaDeProdutosCadastrados() {

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

        this.response = given()
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario("fulano@qa.com", "teste"))
                .body(ProdutoData.cadastrarProduto(Service.gerarNomeDeProdutoRandomico(),
                        Service.gerarDescricaoRandomico(),
                        Service.gerarValorProdutoRandomico(),
                        Service.gerarQuantidadeProdutoRandomico()))
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

        Response gerarListaDeProdutos = given().when().get("/produtos").then().extract().response();
        String produtoComNomeRepetido = gerarListaDeProdutos.jsonPath().getString("produtos[" + 0 + "].nome");

        this.response = given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body(ProdutoData.cadastrarProduto(produtoComNomeRepetido, Service.gerarDescricaoRandomico(), 150, 10))
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .body("message", equalTo("Já existe produto com esse nome"))
                .statusCode(400)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(4)
    @DisplayName("Não permite cadastrar produto sem enviar o parametro do token corretamente na requisição")
    public void testNaoPermiteCadastrarProdutoSemOParametroTokenCorreto() {

        String token = given().
                contentType(ContentType.JSON)
                .body(Service.gerarTokenUsuario("fulano@qa.com", "teste"))
                .when()
                .post("/login")
                .then()
                .extract().path("authorization");

        System.out.println(token);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("authorization", token + "a")
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
                .header("authorization", token + "a")
                .body(ProdutoData.cadastrarProduto(Service.gerarNomeDeProdutoRandomico(),
                        Service.gerarDescricaoRandomico(), 150, 10))
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
                .body(ProdutoData.cadastrarProduto(Service.gerarNomeDeProdutoRandomico(),
                        Service.gerarDescricaoRandomico(), 150, 10))
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
                .pathParam("_id", Service.selecionarUltimoProduto())
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario("fulano@qa.com", "teste"))
                .when()
                .delete("/produtos/{_id}")
                .then()
                .assertThat()
                .body("message", equalTo("Registro excluído com sucesso"))
                .statusCode(HttpStatus.SC_OK)
                .log().all();

    }

    @Test
    @Order(9)
    @DisplayName("Alterar o nome de um produto com sucesso")
    public void testAlterarNomeDeProdutoComSucesso() {

        given()
                .pathParam("_id", Service.selecionarUltimoProduto())
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario("fulano@qa.com", "teste"))
                .body(ProdutoData.cadastrarProduto(Service.gerarNomeDeProdutoRandomico() + "1", Service.gerarDescricaoRandomico(), 150, 10))
                .when()
                .put("/produtos/{_id}")
                .then()
                .assertThat()
                .body("message", equalTo("Registro alterado com sucesso"))
                .and()
                .statusCode(HttpStatus.SC_OK)
                .log().all();

    }

    @Test
    @Order(10)
    @DisplayName("Validar que nao permite alterar nome de produto para um existente")
    public void testAlterarNomeDeProdutoComNaoSucesso() {

        given()
                .pathParam("_id", "0uxuPY0cbmQhpEz1")
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario("fulano@qa.com", "teste"))
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
    @DisplayName("Validar que nao permite alterar produto com token do produto inválido")
    public void testAlterarNomeProdutoComTokenDeUsuarioInvalido() {

        given()
                .pathParam("_id", "abc")
                .contentType(ContentType.JSON)
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
