package com.example.client;

import io.restassured.response.Response;

public class ApiResponse {
    private final Response response;

    public ApiResponse(Response response) {
        this.response = response;
    }

    public String getResult() {
        return response.jsonPath().getString("result");
    }

    public String getMessage() {
        return response.jsonPath().getString("message");
    }

    public int getStatusCode() {
        return response.getStatusCode();
    }

    public String getBody() {
        return response.getBody().asString();
    }

    public boolean isSuccess() {
        return "OK".equals(getResult());
    }

    public boolean isError() {
        return "ERROR".equals(getResult());
    }
}