package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.config.ConfigStellarBurgers;
import org.example.entity.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends ConfigStellarBurgers {
    private static final String PATH = "api/orders";
    private static final String PATH_INGREDIENTS = "api/ingredients";

    @Step("get all ingredients")
    public ValidatableResponse getAllIngredients() {
        return given()
                .spec(getSpec())
                .log().all()
                .get(PATH_INGREDIENTS)
                .then()
                .log().all();
    }

    @Step("get orders by authorization")
    public ValidatableResponse getOrdersByAuthorization(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(PATH)
                .then()
                .log().all();
    }

    @Step("get orders without authorization")
    public ValidatableResponse getOrdersWithoutAuthorization() {
        return given()
                .spec(getSpec())
                .log().all()
                .get(PATH)
                .then()
                .log().all();
    }


    @Step("create order by authorization")
    public ValidatableResponse createOrderByAuthorization(Order order, String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(order)
                .log().all()
                .post(PATH)
                .then()
                .log().all();
    }

    @Step("create order without authorization")
    public ValidatableResponse createOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .log().all()
                .post(PATH)
                .then()
                .log().all();
    }
}
