package dev.servrest.modulos.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

public class CarrinhoTest {

    Response response;

    @Test
    @DisplayName("Exibir lista de produtos no carrinho")
    public void testExibirListaDeProdutosNoCarrinho() {

        baseURI = "http://localhost";
        port = 3000;

        this.response = given().
                when().
                get("/carrinhos")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

}
