import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.example.Client;
import org.example.ClientGenerator;
import org.example.UserClient;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Delete client")
public class ClientDeleteTest {
    private static final String MESSAGE_ACCEPTED = "Client successfully removed";
    private UserClient userClient;
    private Client client;


    @Before
    public void setUp() {
        client = ClientGenerator.getRandomClient();
        userClient = new UserClient();
    }
    @Test
    @DisplayName("Client delete by valid credentials")
    public void clientDeleteByValidCredentials() {
        ValidatableResponse response = userClient.create(client);
        String accessToken = response.extract().path("accessToken");
        response = userClient.delete(StringUtils.substringAfter(accessToken, " "));
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isDelete = response.extract().path("success");

        assertThat("Token is null", accessToken, notNullValue());
        assertThat("Code not equal", statusCode, equalTo(SC_ACCEPTED));
        assertThat("Message not equal", message, equalTo(MESSAGE_ACCEPTED));
        assertThat("Client is delete incorrect", isDelete, equalTo(true));
    }
}
