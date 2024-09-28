package dev.servrest.modulos.data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class FactoryData {

    private static String dados;
    public static final String EMAIL_REPETIDO = "beltrano@qa.com.br";
    static Faker data = new Faker(new Locale("pt-BR"));
    public static String NOME_RANDOMICO = setDados(data.name().fullName());
    public static String SENHA_RANDOMICA = setDados(data.internet().password());
    public static String EMAIL_RANDOMICO = setDados(data.internet().emailAddress());
    public static String NOME_PRODUTO_RANDOMICO = setDados(data.food().ingredient());
    public static String DESCRICAO_PRODUTO_RANDOMICO = setDados(data.animal().name());
    public static int VALOR = Integer.parseInt(setDados(String.valueOf(data.random().nextInt(50000))));

    public static String getDados() {
        return dados;
    }

    public static String setDados(String dado) {
        dados = dado;
        return dados;
    }

}
