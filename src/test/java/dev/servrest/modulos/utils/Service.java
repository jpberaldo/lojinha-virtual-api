package dev.servrest.modulos.utils;

import dev.servrest.modulos.data.FactoryData;
import dev.servrest.modulos.data.UsuarioData;
import dev.servrest.modulos.pojo.UsuarioPojo;
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
        return response.jsonPath().getString("produtos[" + produto + "]._id");
    }

    public static String selecionarUsuario(int usuario) {
        response = given().when().get("/usuarios").then().extract().response();
        return response.jsonPath().getString("usuarios[" + usuario + "]._id");
    }

    public static String selecionarCarrinho(int carrinho) {
        response = given().when().get("/carrinhos").then().extract().response();
        return response.jsonPath().getString("carrinhos[" + carrinho + "]._id");
    }

    public static List<String> gerarlistaDeIDUsuarios() {
        return UsuarioPojo.listaDeIDUsuarios();
    }

    public static String selecionarUltimoUsuario() {
        List<String> listaDeUsuarios = gerarlistaDeIDUsuarios();
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

    public static String gerarNomeDeProdutoRandomico() {
        return FactoryData.NOME_PRODUTO_RANDOMICO;
    }

    public static String gerarDescricaoRandomico() {
        return FactoryData.DESCRICAO_PRODUTO_RANDOMICO;
    }

    public static int gerarValorProdutoRandomico() {
        return FactoryData.VALOR;
    }

    public static int gerarQuantidadeProdutoRandomico() {
        return FactoryData.VALOR;
    }


}
