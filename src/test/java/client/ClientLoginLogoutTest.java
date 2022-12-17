package client;

import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.Client;
import org.example.utils.ClientGenerator;
import org.example.api.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Login and logout client")
public class ClientLoginLogoutTest {
    private static final String MESSAGE_LOGOUT = "Successful logout";
    private static final String MESSAGE_UNAUTHORIZED = "email or password are incorrect";
    private ValidatableResponse response;
    private UserClient userClient;
    private Client client;
    private String accessToken;


    @Before
    public void setUp() {
        client = ClientGenerator.getRandomClient();
        userClient = new UserClient();
    }
    @After
    public void clearState() {
        userClient.deleteClient(StringUtils.substringAfter(accessToken, " "));
    }

    @Test
    @DisplayName("Client login by valid credentials")
    public void clientLoginByValidCredentials() {
        response = userClient.createClient(client);
        accessToken = response.extract().path("accessToken");
        response = userClient.loginClient(client, accessToken);
        int statusCode = response.extract().statusCode();
        boolean isLogin = response.extract().path("success");

        assertThat("Token is null", accessToken, notNullValue());
        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("Client is login incorrect", isLogin, equalTo(true));
    }

    @Test
    @DisplayName("Client logout by valid credentials")
    public void clientLogoutByValidCredentials() {
        response = userClient.createClient(client);
        accessToken = response.extract().path("accessToken");
        response = userClient.loginClient(client, accessToken);
        String refreshToken = response.extract().path("refreshToken");
        refreshToken = "{\"token\":\"" + refreshToken + "\"}";
        response = userClient.logoutClient(refreshToken);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isLogout = response.extract().path("success");

        assertThat("Token is null", refreshToken, notNullValue());
        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("Message not equal", message, equalTo(MESSAGE_LOGOUT));
        assertThat("Client is logout incorrect", isLogout, equalTo(true));
    }

    @Test
    @DisplayName("Client login is empty email")
    public void clientLoginByEmptyEmail() {
        response = userClient.createClient(client);
        accessToken = response.extract().path("accessToken");
        client.setEmail(null);
        response = userClient.loginClient(client, accessToken);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isLogin = response.extract().path("success");

        assertThat("Token is null", accessToken, notNullValue());
        assertThat("Code not equal", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Message not equal", message, equalTo(MESSAGE_UNAUTHORIZED));
        assertThat("Client is login correct", isLogin, equalTo(false));
    }

    @Test
    @DisplayName("Client login is empty password")
    public void clientLoginByEmptyPassword() {
        response = userClient.createClient(client);
        accessToken = response.extract().path("accessToken");
        client.setPassword(null);
        response = userClient.loginClient(client, accessToken);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isLogin = response.extract().path("success");

        assertThat("Token is null", accessToken, notNullValue());
        assertThat("Code not equal", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Message not equal", message, equalTo(MESSAGE_UNAUTHORIZED));
        assertThat("Client is login correct", isLogin, equalTo(false));
    }
}
