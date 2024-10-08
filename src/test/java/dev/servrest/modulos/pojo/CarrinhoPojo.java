package dev.servrest.modulos.pojo;

import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class CarrinhoPojo {

    private List<ProdutoPojo> produtos;
    private static Response response;

    public List<ProdutoPojo> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoPojo> produtos) {
        this.produtos = produtos;
    }

    public static String gerarDadosDoCarrinhoDeProdutos(int carrinho) {
        response = given().when().get("/carrinhos").then().extract().response();
        return response.jsonPath().getString("carrinhos[" + carrinho + "]._id");
    }
}
