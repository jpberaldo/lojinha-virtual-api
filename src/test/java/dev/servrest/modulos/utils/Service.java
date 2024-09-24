package dev.servrest.modulos.utils;

import dev.servrest.modulos.data.UsuarioData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;

public class Service {

    static Response response;

    public static void configurarEnderecoDaRequisicaoChamada() {

        baseURI = "http://localhost";
        port = 3000;

    }

    public static String gerarProdutoId(int produto) {
        response = given().when().get("/produtos").then().extract().response();
        String productID = response.jsonPath().getString("produtos[" + produto + "]._id");
        return productID;
    }

    public static String selecionarUsuario(int usuario) {
        response = given().when().get("/usuarios").then().extract().response();
        String usuarioSelecionado = response.jsonPath().getString("usuarios[" + usuario + "]._id");
        return usuarioSelecionado;
    }

    public static String selecionarCarrinho(int carrinho) {
        response = given().when().get("/carrinhos").then().extract().response();
        String carrinhoSelecionado = response.jsonPath().getString("carrinhos[" + carrinho + "]._id");
        return carrinhoSelecionado;
    }

    public static List<String> listaDeUsuarios() {

        response = given().when().get("/usuarios").then().extract().response();
        List<String> listaUsuarios = response.jsonPath().getList("usuarios._id");
        return listaUsuarios;
    }

    public static String selecionarUltimoUsuario() {
        List<String> listaDeUsuarios = listaDeUsuarios();
        if (!listaDeUsuarios.isEmpty()) {
            return listaDeUsuarios.get(listaDeUsuarios.size() - 1);
        } else {
            return null;
        }
    }

    public static String gerarTokenUsuario(String email, String senha) {

        response = given()
                .contentType(ContentType.JSON)
                .body(UsuarioData.realizarLoginComUsuario(email, senha))
                .when()
                .post("/login")
                .then().extract().response();

        String token = response.jsonPath().getString("authorization");
        return token;
    }


}
