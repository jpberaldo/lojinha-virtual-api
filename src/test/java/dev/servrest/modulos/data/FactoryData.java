package dev.servrest.modulos.data;

public class FactoryData {

    private static String dados;
    public static final String EMAIL_REPETIDO = "beltrano@qa.com.br";

    public static String getDados() {
        return dados;
    }

    public static String setDados(String dado) {
        dados = dado;
        return dados;
    }
}
