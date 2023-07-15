# SilkPay transactions Microservice

Микросервис транзакций учетной записи SilkPay с Spring Boot, Postgres.
Этот микросервис отвечает за создание учетной записи пользователя и балансов.


## Стэк

- [Java 11](https://jdk.java.net/11/)
- [Junit5](https://junit.org/junit5/docs/current/user-guide/)
- [Gradle](https://gradle.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Postgres](https://www.postgresql.org/)
- [Docker](https://www.docker.com/)

## Как развернуть

На вашем компьютере должен быть установлен **Docker**. Вы можете легко развернуть сервис, выполнив
следующую команду в вашем терминале:

```bash
docker compose up
```

Сначала он создаст и запустит службу **database**, а затем создаст
контейнер сформирует образ докера микросервиса SilkPay и привяжет порт 8080 контейнера для общего пользования.

## Как использовать

Перейдите на страницу документов [OpenAPI](http://localhost:8080/swagger-ui/index.html)


##