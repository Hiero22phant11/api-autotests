package com.example.tests;

import com.example.base.BaseTest;
import com.example.client.ApiResponse;
import com.example.utils.TokenGenerator;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Действия пользователя")
@Feature("Операция ACTION")
@DisplayName("Тестирование операции ACTION")
class ActionTest extends BaseTest {

    @Test
    @DisplayName("Успешное действие после аутентификации")
    @Description("Тест проверяет выполнение действия после успешного LOGIN")
    @Severity(SeverityLevel.BLOCKER)
    void successfulActionAfterLogin() {
        String token = TokenGenerator.generateValidToken();

        // Сначала выполняем LOGIN
        setupSuccessfulLoginFlow(token);

        // Затем выполняем ACTION
        mockDoActionSuccess();
        ApiResponse response = apiClient.sendRequest(token, "ACTION");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getResult()).isEqualTo("OK");
    }

    @Test
    @DisplayName("Ошибка действия без предварительного LOGIN")
    @Severity(SeverityLevel.CRITICAL)
    void actionFailureWithoutLogin() {
        String token = TokenGenerator.generateValidToken();

        // Пытаемся выполнить ACTION без LOGIN
        ApiResponse response = apiClient.sendRequest(token, "ACTION");

        assertThat(response.isError()).isTrue();
        assertThat(response.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Ошибка действия после LOGOUT")
    @Severity(SeverityLevel.CRITICAL)
    void actionFailureAfterLogout() {
        String token = TokenGenerator.generateValidToken();

        // Логинимся
        setupSuccessfulLoginFlow(token);

        // Выходим
        ApiResponse logoutResponse = apiClient.sendRequest(token, "LOGOUT");
        assertThat(logoutResponse.isSuccess()).isTrue();

        // Пытаемся выполнить ACTION после LOGOUT
        ApiResponse actionResponse = apiClient.sendRequest(token, "ACTION");

        assertThat(actionResponse.isError()).isTrue();
        assertThat(actionResponse.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Ошибка действия при проблеме внешнего сервиса")
    @Severity(SeverityLevel.CRITICAL)
    void actionFailureWhenExternalServiceFails() {
        String token = TokenGenerator.generateValidToken();

        // Логинимся
        setupSuccessfulLoginFlow(token);

        // Настраиваем ошибку внешнего сервиса
        mockDoActionError(500);

        ApiResponse response = apiClient.sendRequest(token, "ACTION");

        assertThat(response.isError()).isTrue();
        assertThat(response.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Действие с невалидным токеном после LOGIN")
    @Severity(SeverityLevel.NORMAL)
    void actionWithInvalidTokenAfterLogin() {
        String validToken = TokenGenerator.generateValidToken();
        String invalidToken = TokenGenerator.generateShortToken();

        // Логинимся с валидным токеном
        setupSuccessfulLoginFlow(validToken);

        // Пытаемся выполнить ACTION с невалидным токеном
        ApiResponse response = apiClient.sendRequest(invalidToken, "ACTION");

        assertThat(response.isError()).isTrue();
    }
}