package dev.servrest.modulos.tests;

import dev.servrest.modulos.utils.Service;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;

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

        System.out.println(response.asString());

    }

    @Test
    @Order(2)
    @DisplayName("Buscar carrinho por ID")
    public void testBuscarCarrinhoPorId() {

        this.response = given()
                .pathParam("_id", "qbMqntef4iTOwWfg")
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

}
