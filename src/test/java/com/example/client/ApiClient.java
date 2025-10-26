package com.example.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.qameta.allure.Allure;

import java.util.HashMap;
import java.util.Map;

public class ApiClient {
    private final String baseUrl;
    private final String apiKey;

    public ApiClient(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public ApiResponse sendRequest(String token, String action) {
        return Allure.step("Отправка запроса: action=" + action + ", token=" + maskToken(token), () -> {
            Map<String, String> formParams = new HashMap<>();
            formParams.put("token", token);
            formParams.put("action", action);

            Response response = RestAssured.given()
                    .baseUri(baseUrl)
                    .header("X-Api-Key", apiKey)
                    .contentType("application/x-www-form-urlencoded")
                    .accept("application/json")
                    .formParams(formParams)
                    .log().all()
                    .post("/endpoint")
                    .then()
                    .log().all()
                    .extract().response();

            return new ApiResponse(response);
        });
    }

    public ApiResponse sendRequestWithInvalidApiKey(String token, String action) {
        return Allure.step("Отправка запроса с невалидным API ключом", () -> {
            Map<String, String> formParams = new HashMap<>();
            formParams.put("token", token);
            formParams.put("action", action);

            Response response = RestAssured.given()
                    .baseUri(baseUrl)
                    .header("X-Api-Key", "invalid_key")
                    .contentType("application/x-www-form-urlencoded")
                    .accept("application/json")
                    .formParams(formParams)
                    .post("/endpoint");

            return new ApiResponse(response);
        });
    }

    private String maskToken(String token) {
        if (token == null || token.length() <= 8) {
            return "***";
        }
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }
}