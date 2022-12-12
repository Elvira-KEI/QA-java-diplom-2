package org.example;

import org.apache.commons.lang3.RandomStringUtils;

//генератор клиента
public class ClientGenerator {
    public static Client getRandomClient() {
        final String clientEmail = RandomStringUtils.randomAlphabetic(8);
        final String clientPassword = RandomStringUtils.randomAlphabetic(8);
        final String clientName = RandomStringUtils.randomAlphabetic(8);
        return new Client(clientEmail, clientPassword, clientName);
    }
}
