package com.example.tests;

import com.example.base.BaseTest;
import com.example.client.ApiResponse;
import com.example.utils.TokenGenerator;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Завершение сессии")
@Feature("Операция LOGOUT")
@DisplayName("Тестирование операции LOGOUT")
class LogoutTest extends BaseTest {

    @Test
    @DisplayName("Успешный выход после аутентификации")
    @Description("Тест проверяет успешное завершение сессии после LOGIN")
    @Severity(SeverityLevel.BLOCKER)
    void successfulLogoutAfterLogin() {
        String token = TokenGenerator.generateValidToken();

        // Логинимся
        setupSuccessfulLoginFlow(token);

        // Выходим
        ApiResponse response = apiClient.sendRequest(token, "LOGOUT");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getResult()).isEqualTo("OK");
    }

    @Test
    @DisplayName("Выход без предварительного LOGIN")
    @Severity(SeverityLevel.NORMAL)
    void logoutWithoutLogin() {
        String token = TokenGenerator.generateValidToken();

        // Пытаемся выйти без логина
        ApiResponse response = apiClient.sendRequest(token, "LOGOUT");

        assertThat(response.isError()).isTrue();
        assertThat(response.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Повторный выход после LOGOUT")
    @Severity(SeverityLevel.NORMAL)
    void repeatedLogoutAfterLogout() {
        String token = TokenGenerator.generateValidToken();

        // Логинимся и выходим
        setupSuccessfulLoginFlow(token);
        ApiResponse firstLogout = apiClient.sendRequest(token, "LOGOUT");
        assertThat(firstLogout.isSuccess()).isTrue();

        // Пытаемся выйти повторно
        ApiResponse secondLogout = apiClient.sendRequest(token, "LOGOUT");

        assertThat(secondLogout.isError()).isTrue();
    }

    @Test
    @DisplayName("Полный цикл: LOGIN -> ACTION -> LOGOUT")
    @Severity(SeverityLevel.CRITICAL)
    void fullUserSessionCycle() {
        String token = TokenGenerator.generateValidToken();

        // 1. LOGIN
        mockAuthSuccess();
        ApiResponse loginResponse = apiClient.sendRequest(token, "LOGIN");
        assertThat(loginResponse.isSuccess()).isTrue();

        // 2. ACTION
        mockDoActionSuccess();
        ApiResponse actionResponse = apiClient.sendRequest(token, "ACTION");
        assertThat(actionResponse.isSuccess()).isTrue();

        // 3. LOGOUT
        ApiResponse logoutResponse = apiClient.sendRequest(token, "LOGOUT");
        assertThat(logoutResponse.isSuccess()).isTrue();

        // 4. Проверяем что после LOGOUT ACTION недоступен
        ApiResponse actionAfterLogout = apiClient.sendRequest(token, "ACTION");
        assertThat(actionAfterLogout.isError()).isTrue();
    }

    @Test
    @DisplayName("Выход с невалидным токеном")
    @Severity(SeverityLevel.NORMAL)
    void logoutWithInvalidToken() {
        String invalidToken = TokenGenerator.generateShortToken();

        ApiResponse response = apiClient.sendRequest(invalidToken, "LOGOUT");

        assertThat(response.isError()).isTrue();
        assertThat(response.getMessage()).isNotNull();
    }
}