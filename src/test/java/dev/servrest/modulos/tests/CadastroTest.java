package dev.servrest.modulos.tests;

import dev.servrest.modulos.data.CadastroData;
import dev.servrest.modulos.utils.Service;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static dev.servrest.modulos.data.FactoryData.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CadastroTest {

    Response response;

    @BeforeEach
    public void beforeEach() {
        Service.configurarEnderecoDaRequisicaoChamada();
    }

    @Test
    @Order(1)
    @DisplayName("Validar que não permite fazer cadastro com um usuário já cadastrado no sistema")
    public void testValidarQueCadastrarUsuarioComDadosJaUtilizados() {

        this.response = given().
                contentType(ContentType.JSON)
                .body(CadastroData.informarDadosDeCadastro(NOME_RANDOMICO, EMAIL_REPETIDO, SENHA_RANDOMICA, "true"))
                .when()
                .post("/usuarios")
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Este email já está sendo usado"))
                .extract().response();

        System.out.println("Nome utilizado: " + NOME_RANDOMICO);
        System.out.println(response.asString());

    }

    @Test
    @Order(2)
    @DisplayName("Cadastrar novo usuario com dados validos")
    public void testCadastrarNovoUsuarioComSucesso() {

        this.response = given()
                .contentType(ContentType.JSON)
                .body(CadastroData.informarDadosDeCadastro(NOME_RANDOMICO, EMAIL_RANDOMICO, SENHA_RANDOMICA, "true"))
                .when()
                .post("/usuarios")
                .then()
                .assertThat()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        System.out.println("Dados do cadastro: " + NOME_RANDOMICO + " /// " + EMAIL_RANDOMICO + " /// " + SENHA_RANDOMICA);
        System.out.println(response.asString());

    }

    @Test
    @Order(3)
    @DisplayName("Excluir um usuário existente")
    public void testExcluirUmUsuarioExistente() {

//        Response usuariosResponse = given()
//                .when()
//                .get("/usuarios")
//                .then()
//                .extract().response();
//
//        String idUsuario = usuariosResponse.jsonPath().getString("usuarios[4]._id");

        String id = Service.cadastrarNovoUsuario().jsonPath().getString("_id");
        System.out.println("Qual é o id? " + id);

        Response teste = given()
                .pathParam("_id", id)
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .assertThat()
                .body("message", equalTo("Registro excluído com sucesso"))
                .statusCode(200)
                .extract().response();

        System.out.println(teste.asString());

    }

    @Test
    @Order(4)
    @DisplayName("Validar que não permite cadastrar novo usuario com método da Requisão PUT")
    public void testValidarNaoPermiteCadastrarUsuarioComChamadaReqPut() {


        Response res = given()
                .contentType(ContentType.JSON)
                .body(CadastroData.informarDadosDeCadastro(NOME_RANDOMICO, EMAIL_RANDOMICO, SENHA_RANDOMICA, "true"))
                .when()
                .put("/usuarios");

        //1 jeito de fazer as validações
        Assertions.assertEquals(res.getStatusCode(), 405);

        String message = res.jsonPath().getString("message");
        //Outro jeito de validar
        Assertions.assertEquals(message, "Não é possível realizar PUT em /usuarios. Acesse http://localhost:3000 para ver as rotas disponíveis e como utilizá-las.");

        //para exibir toda a saída da resposta(header + body)
        res.then().log().all();

    }

}
