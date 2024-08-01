package dev.servrest.modulos.tests;

import com.github.javafaker.Faker;

import static dev.servrest.modulos.data.FactoryData.*;

import dev.servrest.modulos.data.CadastroData;
import dev.servrest.modulos.utils.Service;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CadastroTest {

    Response response;
    Faker dados = new Faker(new Locale("pt-BR"));

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
                .body(CadastroData.informarDadosDeCadastro(
                        setDados(dados.name().fullName()),
                        setDados(dados.internet().safeEmailAddress()),
                        setDados(dados.internet().password()),
                        "true"))
                .when()
                .post("/usuarios")
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Este email já está sendo usado"))
                .extract().response();

        System.out.println("Nome utilizado" + getDados(dados));
        System.out.println(response.asString());

    }

    @Test
    @Order(2)
    @DisplayName("Cadastrar novo usuario com dados validos")
    public void testCadastrarNovoUsuarioComSucesso() {


        response = given()
                .contentType(ContentType.JSON)
                .body(CadastroData.informarDadosDeCadastro(FactoryData.setDados(dados.name().fullName()), "test@emailnovo.com", "teste", "true"))
                .when()
                .post("/usuarios")
                .then()
                .assertThat()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(3)
    @DisplayName("Excluir um usuário existente")
    public void testExcluirUmUsuarioExistente() {

        Response usuariosResponse = given()
                .when()
                .get("/usuarios")
                .then()
                .extract().response();

        String idUsuario = usuariosResponse.jsonPath().getString("usuarios[4]._id");

        response = given()
                .pathParam("_id", idUsuario)
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(4)
    @DisplayName("Validar que não permite cadastrar novo usuario com método da Requisão PUT")
    public void testValidarNaoPermiteCadastrarUsuarioComChamadaReqPut() {


        Response res = given()
                .contentType(ContentType.JSON)
                .body(CadastroData.informarDadosDeCadastro("Joao", "teste@emailnovo.com", "teste", "true"))
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
