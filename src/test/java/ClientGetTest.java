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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Get client")
public class ClientGetTest {
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
    @DisplayName("Get client by valid credentials")
    public void clientGetByValidCredentials() {
        ValidatableResponse response = userClient.create(client);
        accessToken = response.extract().path("accessToken");
        response = userClient.getClient(accessToken);
        int statusCode = response.extract().statusCode();
        boolean isGet = response.extract().path("success");
        String email = response.extract().path("client.email");
        String name = response.extract().path("client.name");

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("Client is get incorrect", isGet, equalTo(true));
        assertThat("Email not equal", email, equalTo(client.getEmail()));
        assertThat("Name not equal", name, equalTo(client.getName()));
    }
}
