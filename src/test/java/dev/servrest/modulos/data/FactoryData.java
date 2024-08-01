package dev.servrest.modulos.data;

import com.github.javafaker.Faker;

public class FactoryData {

    private static String dados;

    public static String getDados(Faker conversor) {
        dados = conversor.toString();
        return dados;
    }

    public static String setDados(String conversor) {
        dados = conversor.toString();
        return dados;
    }
}
