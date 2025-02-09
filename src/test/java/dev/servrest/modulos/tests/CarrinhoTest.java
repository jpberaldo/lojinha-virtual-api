package dev.servrest.modulos.tests;

import dev.servrest.modulos.pojo.CadastroPojo;
import dev.servrest.modulos.utils.Service;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarrinhoTest {

    Response response;

    @BeforeEach
    public void beforeEach() {
        Service.configurarEnderecoDaRequisicaoChamada();
    }

    @Test
    @Order(1)
    @DisplayName("Exibir lista de produtos no carrinho")
    public void testExibirListaDeProdutosNoCarrinho() {

        this.response = given().
                when().
                get("/carrinhos")
                .then()
                .statusCode(200)
                .extract().response();

        int qtdDeCarrinhos = response.jsonPath().get("quantidade");
        Assertions.assertNotEquals(0, qtdDeCarrinhos);
        System.out.println("Voce tem: " + qtdDeCarrinhos + " de carrinhos cadastrados");
        System.out.println(response.asString());

    }

    @Test
    @Order(2)
    @DisplayName("Buscar carrinho por ID")
    public void testBuscarCarrinhoPorId() {

        this.response = given()
                .pathParam("_id", Service.selecionarCarrinho(0))
                .when()
                .get("/carrinhos/{_id}")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(3)
    @DisplayName("Buscar carrinho por ID inválido")
    public void testBuscarCarrinhoPorIdInvalido() {

        this.response = given()
                .pathParam("_id", "BeeJh5lz3k6kSIzA")
                .when()
                .get("/carrinhos/{_id}")
                .then()
                .statusCode(400)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(4)
    @DisplayName("Realizar cadastro de produto no carrinho com sucesso")
    public void testRealizarCadastroDeProdutoNoCarrinhoComSucesso() {


        List<Map<String, Object>> produtos = new ArrayList<>();
        Map<String, Object> produto = new HashMap<>();
        produto.put("idProduto", Service.selecionarUltimoProduto());
        produto.put("quantidade", 1);
        produtos.add(produto);

        Map<String, Object> body = new HashMap<>();
        body.put("produtos", produtos);
        System.out.println(body);

        Map<String, String> dadosUsuario = CadastroPojo.cadastroNovoUsuario();
        String email = dadosUsuario.get("email");
        String senha = dadosUsuario.get("senha");


        this.response = given()
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario(email, senha))
                .body(body)
                .when()
                .post("/carrinhos")
                .then()
                .assertThat()
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .statusCode(201)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(5)
    @DisplayName("Excluir carrinho com sucesso")
    public void testExcluirCarrinhoComSucesso() {

        this.response = given()
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario(Service.selecionarEmailDoUltimoUsuarioCadastrado(), Service.selecionarSenhaDoUltimoUsuarioCadastrado()))
                .when()
                .delete("/carrinhos/concluir-compra")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(6)
    @DisplayName("Realizar cadastro de 2 produtos ou mais no carrinho com sucesso")
    public void testRealizarCadastroDeDoisProdutosOuMaisNoCarrinhoComSucesso() {

        Service.cadastrarNovoUsuario();
        List<Map<String, Object>> produtos = new ArrayList<>();
        Map<String, Object> produto = new HashMap<>();
        produto.put("idProduto", Service.selecionarUltimoProduto());
        produto.put("quantidade", 1);
        produtos.add(produto);

        Map<String, Object> produto2 = new HashMap<>();
        produto2.put("idProduto", Service.gerarProdutoId(4));
        produto2.put("quantidade", 1);
        produtos.add(produto2);

        Map<String, Object> body = new HashMap<>();
        body.put("produtos", produtos);

        this.response = given()
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario(Service.selecionarEmailDoUltimoUsuarioCadastrado(), Service.selecionarSenhaDoUltimoUsuarioCadastrado()))
                .body(body)
                .when()
                .post("/carrinhos")
                .then()
                .assertThat()
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .statusCode(201)
                .extract().response();
        System.out.println(response.asString());

    }

    @Test
    @Order(7)
    @DisplayName("Realizar teste que não permite incluir produto no carrinho sem informar o id do produto")
    public void testNaoPermiteCadastroSemInformarIdDoProduto() {

        this.response = given()
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario("isabela.gomide@live.com", "lyzwwijvvst81y9"))
                .body("{\n" +
                        "  \"produtos\": [\n" +
                        "    {\n" +
                        "      \"idProduto\": \"a\",\n" +
                        "      \"quantidade\": 2\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .when()
                .post("/carrinhos")
                .then()
                .assertThat()
                .body("message", equalTo("Não é permitido ter mais de 1 carrinho"))
                .statusCode(400)
                .extract().response();
        System.out.println(response.asString());

    }

    @Test
    @Order(8)
    @DisplayName("Não permitir cadastrar 2 carrinhos para 1 cliente")
    public void testNaoPermitirCadastrarDoisCarrinhosParaCliente() {

        List<Map<String, Object>> produtos = new ArrayList<>();
        Map<String, Object> produto = new HashMap<>();
        produto.put("idProduto", Service.gerarProdutoId(5));
        produto.put("quantidade", 5);
        produtos.add(produto);

        Map<String, Object> body = new HashMap<>();
        body.put("produtos", produtos);

        this.response = given()
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario(Service.selecionarEmailDoUltimoUsuarioCadastrado(), Service.selecionarSenhaDoUltimoUsuarioCadastrado()))
                .body(body)
                .when()
                .post("/carrinhos")
                .then()
                .assertThat()
                .body("message", equalTo("Não é permitido ter mais de 1 carrinho"))
                .statusCode(400)
                .extract().response();
        System.out.println(response.asString());

    }

    @Test
    @Order(9)
    @DisplayName("Validar mensagem de quando produto nao é encontrado")
    public void testNaoValidarMsgDeProdutoNaoEncontrado() {

        this.response = given()
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario("teste@emailnovo.com", "teste"))
                .body("{\n" +
                        "  \"produtos\": [\n" +
                        "    {\n" +
                        "      \"idProduto\": \"a\",\n" +
                        "      \"quantidade\": 1\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .when()
                .post("/carrinhos")
                .then()
                .assertThat()
                .body("message", equalTo("Produto não encontrado"))
                .statusCode(400)
                .extract().response();
        System.out.println(response.asString());

    }

    @Test
    @Order(10)
    @DisplayName("Buscar carrinho com id válido")
    public void testBuscarCarrinhoComIdValido() {

        this.response = given()
                .pathParam("_id", Service.selecionarCarrinho(0))
                .contentType(ContentType.JSON)
                .when()
                .get("/carrinhos/{_id}")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(11)
    @DisplayName("Buscar carrinho com id inválido")
    public void testBuscarCarrinhoComIdInvalido() {

        this.response = given()
                .pathParam("_id", Service.selecionarCarrinho(0) + "a")
                .contentType(ContentType.JSON)
                .when()
                .get("/carrinhos/{_id}")
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Carrinho não encontrado"))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(12)
    @DisplayName("Excluir carrinho sem sucesso, quando nao encontra carrinho para o usuário")
    public void testExcluirCarrinhoSemSucesso() {

        this.response = given()
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario("cesar.paes@gmail.com", "ak81ne6e29"))
                .when()
                .delete("/carrinhos/concluir-compra")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Não foi encontrado carrinho para esse usuário"))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(13)
    @DisplayName("Excluir carrinho sem sucesso por conta do token invalido")
    public void testExcluirCarrinhoSemSucessoComTokenInvalido() {

        this.response = given()
                .contentType(ContentType.JSON)
                .header("authorization", Service.gerarTokenUsuario("test@emailnovo.com", "teste") + "a")
                .when()
                .delete("/carrinhos/concluir-compra")
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
                .extract().response();

        System.out.println(response.asString());

    }

}
