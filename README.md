# API Autotests

Автотесты для веб-сервиса на Spring Boot.

## Технологии
- Java 17
- JUnit 5
- WireMock
- Allure Framework
- Maven
- REST Assured

## Запуск приложения

Перед запуском тестов необходимо запустить тестируемое приложение:

```bash
java -jar -Dsecret=qazWSXedc -Dmock=http://localhost:8888/ internal-0.0.1-SNAPSHOT.jar