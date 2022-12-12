import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.example.Client;
import org.example.ClientGenerator;
import org.example.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Update client")
public class ClientUpdateTest {
    private static final String MESSAGE_UNAUTHORIZED = "You should be authorised";
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
        userClient.delete(StringUtils.substringAfter(accessToken, " "));
    }
    @Test
    @DisplayName("Update client by authorization")
    public void updateClientByAuthorization() {
        response = userClient.create(client);
        accessToken = response.extract().path("accessToken");
        response = userClient.login(client, accessToken);
        response = userClient.updateClientByAuthorization(ClientGenerator.getRandomClient(), accessToken);
        int statusCode = response.extract().statusCode();
        boolean isUpdate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("Client is update incorrect", isUpdate, equalTo(true));
    }

    @Test
    @DisplayName("Update client without authorization")
    public void updateClientWithoutAuthorization() {
        response = userClient.create(client);
        accessToken = response.extract().path("accessToken");
        response = userClient.updateClientWithoutAuthorization(ClientGenerator.getRandomClient());
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isUpdate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Message not equal", message, equalTo(MESSAGE_UNAUTHORIZED));
        assertThat("Client is update correct", isUpdate, equalTo(false));
    }
}
