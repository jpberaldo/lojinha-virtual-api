package dev.servrest.modulos.data;

import dev.servrest.modulos.pojo.ProdutoPojo;

public class ProdutoData {


    public static ProdutoPojo cadastrarProduto(String nomeProduto, String descricao, int valor, int qtd) {

        ProdutoPojo produto = new ProdutoPojo();
        produto.setNome(nomeProduto);
        produto.setDescricao(descricao);
        produto.setPreco(valor);
        produto.setQuantidade(qtd);
        return produto;
    }
}
