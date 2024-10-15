package dev.servrest.modulos.tests;

import dev.servrest.modulos.data.UsuarioData;
import dev.servrest.modulos.utils.Service;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes que validam a classe de Login")
public class UsuarioTest {

    Response response;

    @BeforeEach
    public void beforeEach() {
        Service.configurarEnderecoDaRequisicaoChamada();
    }

    @Test
    @Order(1)
    @DisplayName("Exibir lista de usuários cadastrados")
    public void testExibirListaDeUsuariosCadastrados() {

        this.response = given().
                when().
                get("/usuarios")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        int qtdUsuarios = response.jsonPath().get("quantidade");
        Assertions.assertNotEquals(0, qtdUsuarios);
        System.out.println("Voce tem: " + qtdUsuarios + " usuarios cadastrados");
        System.out.println("Adicionando retorno da response \n===========================" + "\n" + response.asString());
    }

    @Test
    @Order(2)
    @DisplayName("Validar Login com usuário válido")
    public void testRealizarLoginComDadosValidos() {

        this.response = given().
                contentType(ContentType.JSON)
                .body(UsuarioData.realizarLoginComUsuario("fulano@qa.com", "teste"))
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
    @Order(3)
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
    @Order(4)
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
    @Order(5)
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
    @Order(6)
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
    @Order(7)
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
    @Order(8)
    @DisplayName("Não permite excluir usuário com item no carrinho")
    public void testNaoPermitirExcluirUsuarioComItemNoCarrinho() {

        given()
                .pathParam("_id", Service.selecionarUsuario(0))
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
    @Order(9)
    @DisplayName("Buscar usuário cadastrado por nome")
    public void testBuscarUsuarioFiltrandoPorNome() {

        this.response = given()
                .queryParam("nome", "Fulano")
                .when()
                .get("/usuarios")
                .then()
                .assertThat()
                .statusCode(200)
                .body("usuarios.nome", hasItem("Fulano da Silva"))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(10)
    @DisplayName("Buscar usuário cadastrado por email")
    public void testBuscarUsuarioFiltrandoPorEmail() {

        this.response = given()
                .queryParam("email", "fulano@qa.com")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(11)
    @DisplayName("Buscar usuário cadastrado por senha")
    public void testBuscarUsuarioFiltrandoPorSenha() {

        this.response = given()
                .queryParam("password", "teste")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(12)
    @DisplayName("Filtrar apenas usuários que não são adminitradores")
    public void testFiltrarApenasUsuariosNaoAdmin() {

        this.response = given()
                .queryParam("administrador", "false")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .body("usuarios.nome", hasItem("Teste alteracao"))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(13)
    @DisplayName("Filtrar apenas usuários que são adminitradores")
    public void testFiltrarApenasUsuariosAdmin() {

        this.response = given()
                .queryParam("administrador", "true")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .body("usuarios.nome", hasItem("Fulano da Silva"))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(14)
    @DisplayName("Fazer busca de usuario por 2 ou mais filtros")
    public void testBuscarUsuarioPorDoisOuMaisFiltros() {

        Map<String, String> parametros = new HashMap<>();
        parametros.put("nome", "Fulano");
        parametros.put("email", "fulano@qa.com");

        this.response = given()
                .queryParams(parametros)
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
    @Order(15)
    @DisplayName("Buscar usuário cadastrado por nome não encontrado")
    public void testBuscarUsuarioFiltrandoPorNomeNaoEncontrado() {

        this.response = given()
                .queryParam("nome", "Aleatorio")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .body("quantidade", equalTo(0))
                .extract().response();

        System.out.println(response.asString());

    }

    @Test
    @Order(16)
    @DisplayName("Validar lista de usuários cadastrados de outro jeito")
    public void testValidarListaDeUsuariosCadastrados() {

        //Como exibir e filtrar dentro de uma
        // lista de objetos que estao dentro de um body convertido para Json
        this.response = given().
                when().
                get("/usuarios");

        JSONObject json = new JSONObject(response.asString());
        for (int i = 0; i < json.getJSONArray("usuarios").length(); i++) {
            String usuariosNome = json.getJSONArray("usuarios").getJSONObject(i).get("nome").toString();
            System.out.println(usuariosNome);
        }

    }

    @Test
    @Order(17)
    @DisplayName("Editar dados de um usuário com sucesso")
    public void testEditarDadosDeUmUsuarioComSucesso() {

        response = given()
                .pathParam("_id", "3voIW0eX25UpduaG")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"nome\": \"Teste alteracao\",\n" +
                        "  \"email\": \"teste@emailtestes.com\",\n" +
                        "  \"password\": \"teste\",\n" +
                        "  \"administrador\": \"false\"\n" +
                        "}")
                .put("/usuarios/{_id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro alterado com sucesso"))
                .extract().response();

        System.out.println(response.asString());
    }

    @Test
    @Order(18)
    @DisplayName("Editar dados de um usuário com Id inválido")
    public void testEditarDadosDeUmUsuarioComIdInvalido() {

        response = given()
                .pathParam("_id", "xxx")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"nome\": \"Teste alteracao\",\n" +
                        "  \"email\": \"teste@emailtestes.com\",\n" +
                        "  \"password\": \"teste\",\n" +
                        "  \"administrador\": \"false\"\n" +
                        "}")
                .put("/usuarios/{_id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Este email já está sendo usado"))
                .extract().response();

        System.out.println(response.asString());
    }

}
