package dev.servrest.modulos.utils;

import dev.servrest.modulos.data.UsuarioData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;

public class Service {

    static Response response;

    public static void configurarEnderecoDaRequisicaoChamada() {

        baseURI = "http://localhost";
        port = 3000;

    }

    public static String gerarProdutoId() {
        response = given().when().get("/produtos").then().extract().response();
        String productID = response.jsonPath().getString("produtos[8]._id");
        return productID;
    }

    public static String gerarTokenUsuario() {

        response = given()
                .contentType(ContentType.JSON)
                .body(UsuarioData.realizarLoginComUsuario("fulano@qa.com", "teste"))
                .when()
                .post("/login")
                .then().extract().response();

        String token = response.jsonPath().getString("authorization");
        return token;
    }


}
