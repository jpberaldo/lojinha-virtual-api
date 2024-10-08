package dev.servrest.modulos.pojo;

import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ProdutoPojo {

    private String nome;
    private int preco;
    private String descricao;
    private int quantidade;
    static Response response;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public static String gerarProdutoId(int produto) {
        response = given().when().get("/produtos").then().extract().response();
        return response.jsonPath().getString("produtos[" + produto + "]._id");
    }

    public static List<String> gerarlistaDeIDdeProdutos() {
        response = given().when().get("/produtos").then().extract().response();
        return response.jsonPath().getList("produtos._id");
    }

    public static String gerarDadosParaSelecionarUltimoProduto() {
        List<String> listaDeProdutos = gerarlistaDeIDdeProdutos();
        if (!listaDeProdutos.isEmpty()) {
            return listaDeProdutos.get(listaDeProdutos.size() - 1);
        } else {
            return null;
        }
    }
}
