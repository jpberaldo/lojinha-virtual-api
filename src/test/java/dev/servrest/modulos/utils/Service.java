package dev.servrest.modulos.utils;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.port;

public class Service {


    public static void configurarEnderecoDaRequisicaoChamada() {

        baseURI = "http://localhost";
        port = 3000;

    }
}
