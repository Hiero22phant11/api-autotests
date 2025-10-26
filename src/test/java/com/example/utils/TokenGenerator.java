package com.example.utils;

import java.util.Random;

public class TokenGenerator {
    private static final String VALID_CHARS = "0123456789ABCDEF";
    private static final int TOKEN_LENGTH = 32;
    private static final Random random = new Random();

    // Генерирует валидный токен (32 символа, A-Z0-9)
    public static String generateValidToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(VALID_CHARS.charAt(random.nextInt(VALID_CHARS.length())));
        }
        return token.toString();
    }

    // Генерирует короткий токен (10 символов)
    public static String generateShortToken() {
        return generateValidToken().substring(0, 10);
    }

    // Генерирует длинный токен (37 символов)
    public static String generateLongToken() {
        return generateValidToken() + "EXTRA";
    }

    // Генерирует токен с недопустимыми символами
    public static String generateTokenWithInvalidChars() {
        String validToken = generateValidToken();
        return validToken.substring(0, 10) + "abc@#" + validToken.substring(15);
    }

    // Генерирует токен в нижнем регистре
    public static String generateLowerCaseToken() {
        return generateValidToken().toLowerCase();
    }

    // Генерирует пустой токен
    public static String generateEmptyToken() {
        return "";
    }

    // Возвращает null вместо токена
    public static String generateNullToken() {
        return null;
    }
}