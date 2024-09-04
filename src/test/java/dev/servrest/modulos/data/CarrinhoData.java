package dev.servrest.modulos.data;

import dev.servrest.modulos.pojo.CarrinhoPojo;
import dev.servrest.modulos.pojo.ProdutoPojo;

import java.util.List;

public class CarrinhoData {

    public static CarrinhoPojo informarDadosParaCarrinho(List<ProdutoPojo> produtos) {
        CarrinhoPojo carrinho = new CarrinhoPojo();
        carrinho.setProdutos(produtos);
        return carrinho;
    }
}
