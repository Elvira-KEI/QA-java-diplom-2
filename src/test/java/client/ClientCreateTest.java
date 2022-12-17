package client;

import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.Client;
import org.example.utils.ClientGenerator;
import org.example.api.UserClient;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Create client")
public class ClientCreateTest {
    private static final String MESSAGE_FORBIDDEN = "User already exists";
    private static final String MESSAGE_FORBIDDEN_EMPTY_FIELD = "Email, password and name are required fields";
    private ValidatableResponse response;
    private UserClient userClient;
    private Client client;


    @Before
    public void setUp() {
        client = ClientGenerator.getRandomClient();
        userClient = new UserClient();
    }
    @Test
    @DisplayName("Client create by valid credentials")
    public void clientCreateByValidCredentials() {
        response = userClient.createClient(client);
        int statusCode = response.extract().statusCode();
        boolean isCreate = response.extract().path("success");
        String accessToken = response.extract().path("accessToken");
        response = userClient.deleteClient(StringUtils.substringAfter(accessToken, " "));

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("Client is create incorrect", isCreate, equalTo(true));
    }

    @Test
    @DisplayName("Client create is empty email")
    public void clientCreateIsEmptyEmail() {
        client.setEmail(null);
        response = userClient.createClient(client);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isCreate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Message not equal", message, equalTo(MESSAGE_FORBIDDEN_EMPTY_FIELD));
        assertThat("Client is create correct", isCreate, equalTo(false));
    }

    @Test
    @DisplayName("Client create is empty password")
    public void clientCreateIsEmptyPassword() {
        client.setPassword(null);
        response = userClient.createClient(client);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isCreate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Message not equal", message, equalTo(MESSAGE_FORBIDDEN_EMPTY_FIELD));
        assertThat("Client is create correct", isCreate, equalTo(false));
    }

    @Test
    @DisplayName("Client create is empty name")
    public void clientCreateIsEmptyName() {
        client.setName(null);
        response = userClient.createClient(client);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isCreate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Message not equal", message, equalTo(MESSAGE_FORBIDDEN_EMPTY_FIELD));
        assertThat("Client is create correct", isCreate, equalTo(false));
    }

    @Test
    @DisplayName("Repeated request by create client")
    public void repeatedRequestByCreateClient() {
        userClient.createClient(client);
        response = userClient.createClient(client);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isCreate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Message not equal", message, equalTo(MESSAGE_FORBIDDEN));
        assertThat("Client is create correct", isCreate, equalTo(false));
    }
}
