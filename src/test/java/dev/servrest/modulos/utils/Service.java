package dev.servrest.modulos.utils;

import dev.servrest.modulos.data.CadastroData;
import dev.servrest.modulos.data.FactoryData;
import dev.servrest.modulos.pojo.CarrinhoPojo;
import dev.servrest.modulos.pojo.ProdutoPojo;
import dev.servrest.modulos.pojo.UsuarioPojo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static dev.servrest.modulos.data.FactoryData.*;
import static io.restassured.RestAssured.*;

public class Service {

    public static void configurarEnderecoDaRequisicaoChamada() {

        baseURI = "http://localhost";
        port = 3000;

    }

    public static String gerarProdutoId(int produto) {
        return ProdutoPojo.gerarProdutoId(produto);
    }

    public static String selecionarUltimoProduto() {
        return ProdutoPojo.gerarDadosParaSelecionarUltimoProduto();
    }

    public static String selecionarUsuario(int usuario) {
        return UsuarioPojo.gerarDadosParaSelecaoDeUsuario(usuario);
    }

    public static String selecionarCarrinho(int carrinho) {
        return CarrinhoPojo.gerarDadosDoCarrinhoDeProdutos(carrinho);
    }

    public static String selecionarUltimoUsuario() {
        return UsuarioPojo.fazerSelecaoDoUltimoUsuario();
    }

    public static String gerarTokenUsuario(String email, String senha) {
        return UsuarioPojo.gerarDadosDeTokenDoUsuario(email, senha);
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

    public static String selecionarEmailDoUltimoUsuarioCadastrado() {
        return UsuarioPojo.pegarEmailDoUltimoUsuarioCadastrado();
    }

    public static String selecionarSenhaDoUltimoUsuarioCadastrado() {
        return UsuarioPojo.pegarSenhaDoUltimoUsuarioCadastrado();
    }

    public static Response cadastrarNovoUsuario() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(CadastroData.informarDadosDeCadastro(NOME_RANDOMICO, EMAIL_RANDOMICO, SENHA_RANDOMICA, "true"))
                .when()
                .post("/usuarios")
                .then()
                .extract()
                .response();

        return response;
    }

}
