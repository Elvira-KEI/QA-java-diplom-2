package org.example.api;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.example.entity.Client;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private static final String CREATE_CLIENT_PATH = "api/auth/register";
    private static final String LOGIN_CLIENT_PATH = "api/auth/login";
    private static final String LOGOUT_CLIENT_PATH = "api/auth/logout";
    private static final String DELETE_CLIENT_PATH = "api/auth/user";
    private static final String CLIENT_PATH = "api/auth/user";

    protected RequestSpecification getSpec(){
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }

    @Step("get client")
    public ValidatableResponse getClient(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(CLIENT_PATH)
                .then()
                .log().all();
    }

    @Step("creating client")
    public ValidatableResponse create(Client client){
        return  given()
                .spec(getSpec())
                .body(client)
                .when()
                .post(CREATE_CLIENT_PATH)
                .then();
    }
    @Step("login client")
    public ValidatableResponse login(Client client, String accessToken){
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .body(client)
                .log().all()
                .post(LOGIN_CLIENT_PATH)
                .then() .log().all();

    }
    @Step("logout client")
    public ValidatableResponse logoutClient(String refreshToken) {
        return given()
                .spec(getSpec())
                .body(refreshToken)
                .log().all()
                .post(LOGOUT_CLIENT_PATH)
                .then()
                .log().all();
    }
    @Step("delete client")
    public ValidatableResponse delete(String accessToken){
        return
                given()
                        .spec(getSpec())
                        .auth().oauth2(accessToken)
                        .log().all()
                        .delete(DELETE_CLIENT_PATH)
                        .then().log().all();
    }
    @Step("update client by authorization")
    public ValidatableResponse updateClientByAuthorization(Client client, String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(client)
                .log().all()
                .patch(CLIENT_PATH)
                .then()
                .log().all();
    }

    @Step("update client without authorization")
    public ValidatableResponse updateClientWithoutAuthorization(Client client) {
        return given()
                .spec(getSpec())
                .body(client)
                .log().all()
                .patch(CLIENT_PATH)
                .then()
                .log().all();
    }


}
