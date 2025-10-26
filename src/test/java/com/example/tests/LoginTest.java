package com.example.tests;

import com.example.base.BaseTest;
import com.example.client.ApiResponse;
import com.example.utils.TokenGenerator;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Аутентификация пользователя")
@Feature("Операция LOGIN")
@DisplayName("Тестирование операции LOGIN")
class LoginTest extends BaseTest {

    @Test
    @DisplayName("Успешная аутентификация с валидным токеном")
    @Description("Тест проверяет успешную аутентификацию когда внешний сервис возвращает 200 OK")
    @Severity(SeverityLevel.BLOCKER)
    void successfulLoginWithValidToken() {
        String token = TokenGenerator.generateValidToken();

        mockAuthSuccess();

        ApiResponse response = apiClient.sendRequest(token, "LOGIN");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getResult()).isEqualTo("OK");
        assertThat(response.getMessage()).isNull();
    }

    @Test
    @DisplayName("Ошибка аутентификации при 500 ошибке внешнего сервиса")
    @Severity(SeverityLevel.CRITICAL)
    void loginFailureWhenExternalServiceReturns500() {
        String token = TokenGenerator.generateValidToken();

        mockAuthError(500);

        ApiResponse response = apiClient.sendRequest(token, "LOGIN");

        assertThat(response.isError()).isTrue();
        assertThat(response.getResult()).isEqualTo("ERROR");
        assertThat(response.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Ошибка аутентификации при 401 ошибке внешнего сервиса")
    @Severity(SeverityLevel.CRITICAL)
    void loginFailureWhenExternalServiceReturns401() {
        String token = TokenGenerator.generateValidToken();

        mockAuthError(401);

        ApiResponse response = apiClient.sendRequest(token, "LOGIN");

        assertThat(response.isError()).isTrue();
        assertThat(response.getMessage()).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Ошибка аутентификации при невалидном токене")
    @ValueSource(strings = {"SHORT", "VERY_LONG_TOKEN_MORE_THAN_32_CHARS_12345", "invalid_lowercase_token", "token_with_special@chars", "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"}) // Добавил токен с G
    @Severity(SeverityLevel.NORMAL)
    void loginFailureWithInvalidToken(String invalidToken) {
        ApiResponse response = apiClient.sendRequest(invalidToken, "LOGIN");

        assertThat(response.isError()).isTrue();
        assertThat(response.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Ошибка при неверном API ключе")
    @Severity(SeverityLevel.CRITICAL)
    void loginFailureWithInvalidApiKey() {
        String token = TokenGenerator.generateValidToken();

        ApiResponse response = apiClient.sendRequestWithInvalidApiKey(token, "LOGIN");

        assertThat(response.isError()).isTrue();
        assertThat(response.getMessage()).isNotNull();
    }
}