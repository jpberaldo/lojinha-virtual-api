package dev.servrest.modulos.utils;

import dev.servrest.modulos.data.FactoryData;
import dev.servrest.modulos.pojo.CarrinhoPojo;
import dev.servrest.modulos.pojo.ProdutoPojo;
import dev.servrest.modulos.pojo.UsuarioPojo;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.port;

public class Service {

    public static void configurarEnderecoDaRequisicaoChamada() {

        baseURI = "http://localhost";
        port = 3000;

    }

    public static String gerarProdutoId(int produto) {
        return ProdutoPojo.gerarProdutoId(produto);
    }

    public static String selecionarUltimoProduto(){
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

    public static String selecionarEmailDoUltimoUsuarioCadastrado(){
        return UsuarioPojo.pegarEmailDoUltimoUsuarioCadastrado();
    }

    public static String selecionarSenhaDoUltimoUsuarioCadastrado(){
        return UsuarioPojo.pegarSenhaDoUltimoUsuarioCadastrado();
    }

}
