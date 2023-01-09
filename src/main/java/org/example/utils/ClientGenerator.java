package org.example.utils;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.entity.Client;


public class ClientGenerator {
    public static Client getRandomClient() {

        final String name  = RandomStringUtils.randomAlphabetic(8);
        final String password  = RandomStringUtils.randomAlphabetic(8);
        final String email  = name.toLowerCase() + "@yandex.ru";

        Allure.addAttachment("Name : ", name);
        Allure.addAttachment("Password : ", password );
        Allure.addAttachment("Email : ", email );


        return new Client(email , password, name);
    }
}
