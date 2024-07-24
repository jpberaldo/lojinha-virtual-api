package dev.servrest.modulos.tests;

import dev.servrest.modulos.data.CadastroData;
import dev.servrest.modulos.utils.Service;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CadastroTest {

    Response response;

    @BeforeEach
    public void beforeEach() {
        Service.configurarEnderecoDaRequisicaoChamada();
    }

    @Test
    @DisplayName("Validar que não permite fazer cadastro com um usuário já cadastrado no sistema")
    public void testValidarQueCadastrarUsuarioComDadosJaUtilizados() {

        this.response = given().
                contentType(ContentType.JSON)
                .body(CadastroData.informarDadosDeCadastro("Fulano da Silva","beltrano@qa.com.br","teste","true"))
                .when()
                .post("/usuarios")
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Este email já está sendo usado"))
                .extract().response();

        System.out.println(response.asString());

    }

}
