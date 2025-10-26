package com.example.base;

import com.example.client.ApiClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class BaseTest {
    protected static final String APP_BASE_URL = "http://localhost:8080";
    protected static final String MOCK_BASE_URL = "http://localhost:8888";
    protected static final String VALID_API_KEY = "qazWSXedc";
    protected static final String INVALID_API_KEY = "invalid_key";

    protected WireMockServer wireMockServer;
    protected ApiClient apiClient;

    @BeforeEach
    void setUp() {
        Allure.step("Запуск WireMock сервера на порту 8888", () -> {
            wireMockServer = new WireMockServer(8888);
            wireMockServer.start();
            configureFor("localhost", 8888);
        });

        Allure.step("Инициализация API клиента", () -> {
            apiClient = new ApiClient(APP_BASE_URL, VALID_API_KEY);
        });
    }

    @AfterEach
    void tearDown() {
        Allure.step("Остановка WireMock сервера", () -> {
            if (wireMockServer != null) {
                wireMockServer.stop();
            }
        });
    }

    protected void mockAuthSuccess() {
        Allure.step("Настройка мока: /auth возвращает 200 OK", () -> {
            wireMockServer.stubFor(post(urlEqualTo("/auth"))
                    .willReturn(aResponse().withStatus(200)));
        });
    }

    protected void mockAuthError(int statusCode) {
        Allure.step("Настройка мока: /auth возвращает ошибку " + statusCode, () -> {
            wireMockServer.stubFor(post(urlEqualTo("/auth"))
                    .willReturn(aResponse().withStatus(statusCode)));
        });
    }

    protected void mockDoActionSuccess() {
        Allure.step("Настройка мока: /doAction возвращает 200 OK", () -> {
            wireMockServer.stubFor(post(urlEqualTo("/doAction"))
                    .willReturn(aResponse().withStatus(200)));
        });
    }

    protected void mockDoActionError(int statusCode) {
        Allure.step("Настройка мока: /doAction возвращает ошибку " + statusCode, () -> {
            wireMockServer.stubFor(post(urlEqualTo("/doAction"))
                    .willReturn(aResponse().withStatus(statusCode)));
        });
    }

    protected void setupSuccessfulLoginFlow(String token) {
        Allure.step("Подготовка успешного LOGIN для токена: " + token, () -> {
            mockAuthSuccess();
            apiClient.sendRequest(token, "LOGIN");
        });
    }
}