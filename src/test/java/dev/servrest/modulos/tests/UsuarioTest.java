package dev.servrest.modulos.tests;

import dev.servrest.modulos.data.UsuarioData;
import dev.servrest.modulos.utils.Service;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@DisplayName("Testes que validam a classe de Login")
public class UsuarioTest {

    Response response;

    @BeforeEach
    public void beforeEach() {
        Service.configurarEnderecoDaRequisicaoChamada();
    }

    @Test
    @DisplayName("Exibir lista de usuários cadastrados")
    public void testExibirListaDeUsuariosCadastrados() {

        this.response = given().
                when().
                get("/usuarios")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Validar Login com usuário válido")
    public void testRealizarLoginComDadosValidos() {

        this.response = given().
                contentType(ContentType.JSON)
                .body(UsuarioData.realizarLoginComUsuario("testesnovo@qa.com.br", "teste"))
                .when()
                .post("/login")
                .then()
                .assertThat()
                .body("message", equalTo("Login realizado com sucesso"))
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Validar Login com email inválido")
    public void testRealizarLoginComEmailInvalido() {

        this.response = given().
                contentType(ContentType.JSON)
                .body(UsuarioData.realizarLoginComUsuario("testesnovo", "teste"))
                .when()
                .post("/login")
                .then()
                .assertThat()
                .body("email", equalTo("email deve ser um email válido"))
                .statusCode(400)
                .extract().response();

        System.out.println(response.asString());
    }

    @Test
    @DisplayName("Validar Login com senha em branco")
    public void testRealizarLoginComSenhaEmBranco() {

        this.response = given().
                contentType(ContentType.JSON)
                .body(UsuarioData.realizarLoginComUsuario("testesnovo@qa.com.br", ""))
                .when()
                .post("/login")
                .then()
                .assertThat()
                .body("password", equalTo("password não pode ficar em branco"))
                .statusCode(400)
                .extract().response();

        System.out.println(response.asString());
    }

    @Test
    @DisplayName("Validar Login com email em branco")
    public void testRealizarLoginComEmailEmBranco() {

        this.response = given().
                contentType(ContentType.JSON)
                .body(UsuarioData.realizarLoginComUsuario("", "teste"))
                .when()
                .post("/login")
                .then()
                .assertThat()
                .body("email", equalTo("email não pode ficar em branco"))
                .statusCode(400)
                .extract().response();

        System.out.println(response.asString());
    }

    @Test
    @DisplayName("Validar Login com senha incorreta")
    public void testRealizarLoginComSenhaIncorreta() {

        this.response = given().
                contentType(ContentType.JSON)
                .body(UsuarioData.realizarLoginComUsuario("fulano@qa.com", "123"))
                .when()
                .post("/login")
                .then()
                .assertThat()
                .body("message", equalTo("Email e/ou senha inválidos"))
                .statusCode(401)
                .extract().response();

        System.out.println(response.asString());
    }

    @Test
    @DisplayName("Validar que não permite chamada da requisição para fazer Login com método GET")
    public void testValidarQueNaoPermiteChamarRequisicaoDeLoginComMetodoPost() {

        this.response = given().
                contentType(ContentType.JSON)
                .body(UsuarioData.realizarLoginComUsuario("fulano@qa.com", "teste"))
                .when()
                .get("/login")
                .then()
                .assertThat()
                .body("message", equalTo("Não é possível realizar GET em /login. Acesse http://localhost:3000 para ver as rotas disponíveis e como utilizá-las."))
                .statusCode(405)
                .extract().response();

        System.out.println(response.asString());
    }


    @Test
    @DisplayName("Excluir um usuário existente")
    public void testExcluirUmUsuarioExistente() {

        given()
                .pathParam("_id", "1boS3Vbhu42nx3vW")
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"))
                .log().all();

    }

    @Test
    @DisplayName("Não permite excluir usuário com item no carrinho")
    public void testNaoPermitirExcluirUsuarioComItemNoCarrinho() {

        given()
                .pathParam("_id", "0uxuPY0cbmQhpEz1")
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .statusCode(400)
                .body("message", equalTo("Não é permitido excluir usuário com carrinho cadastrado"))
                .log().all();

    }

    @Test
    @DisplayName("Buscar usuário cadastrado por nome")
    public void testBuscarUsuarioFiltrandoPorNome() {

        this.response = given()
                .param("nome", "Fulano")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Buscar usuário cadastrado por email")
    public void testBuscarUsuarioFiltrandoPorEmail() {

        this.response = given()
                .param("email", "fulano@qa.com")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Buscar usuário cadastrado por senha")
    public void testBuscarUsuarioFiltrandoPorSenha() {

        this.response = given()
                .param("password", "teste")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Verificar e listar se usuário for administrador")
    public void testVerificarEListarSeUsuarioEAdministrador() {

        this.response = given()
                .param("administrador", "false")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .body("usuarios.nome", hasItem("Beltrano da Silva"))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Fazer busca de usuario por 2 ou mais filtros")
    public void testBuscarUsuarioPorDoisOuMaisFiltros() {

        Map<String, String> parametros = new HashMap<>();
        parametros.put("nome", "Fulano");
        parametros.put("email", "fulano@qa.com");

        this.response = given()
                .params(parametros)
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .body("usuarios.nome", hasItem("Fulano da Silva"))
                .body("usuarios.email", hasItem("fulano@qa.com"))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @DisplayName("Buscar usuário cadastrado por nome não encontrado")
    public void testBuscarUsuarioFiltrandoPorNomeNaoEncontrado() {

        this.response = given()
                .param("nome", "Teste")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .body("quantidade", equalTo(0))
                .extract().response();

        System.out.println(response.asString());

    }

}
