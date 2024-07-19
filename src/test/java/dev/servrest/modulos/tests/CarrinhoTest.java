package dev.servrest.modulos.tests;

import dev.servrest.modulos.utils.Service;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

public class CarrinhoTest {

    Response response;

    @BeforeEach
    public void beforeEach() {
        Service.configurarRequisicaoChamada();
    }

    @Test
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

}
