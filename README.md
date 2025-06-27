Wallet App
Простой сервис управления кошельком с операциями депозита и вывода средств.

Описание
Это Spring Boot приложение, использующее PostgreSQL для хранения данных. Миграции базы данных управляются с помощью Liquibase. Для запуска и тестирования проекта используется Docker Compose.

Функционал

Операции пополнения (DEPOSIT) и списания (WITHDRAW)
Проверка баланса с контролем недостатка средств

Требования
Docker и Docker Compose

Java 17+

Maven (для локальной сборки)

Быстрый старт
Запуск через Docker Compose
bash
Копировать
Редактировать
docker-compose up --build
Это запустит:

PostgreSQL с созданной базой и таблицами (миграции применятся автоматически)

Приложение Wallet App на порту 8080

Локальная сборка и запуск
Склонируйте репозиторий

bash
Копировать
Редактировать
git clone <адрес_репозитория>
cd wallet-app
Соберите jar

bash
Копировать
Редактировать
mvn clean package -DskipTests
Запустите приложение

bash
Копировать
Редактировать
java -jar target/*.jar
Документация API (Swagger UI)
После запуска приложения вы можете открыть в браузере удобный интерфейс для работы с API:

http://localhost:8080/swagger-ui/index.html#/Wallet%20API/processWalletOp

Там описаны все доступные эндпоинты, их параметры и примеры запросов/ответов.

Конфигурация
Параметры подключения к базе данных и Liquibase задаются в application.properties или через переменные окружения, например:

properties
Копировать
Редактировать
spring.datasource.url=jdbc:postgresql://localhost:5432/wallet-db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.liquibase.change-log=classpath:changelog/db.changelog-master.yaml
spring.liquibase.enabled=true
Структура проекта
src/main/java — исходники приложения

src/main/resources/changelog — файлы миграций Liquibase в формате YAML

docker-compose.yml — конфигурация для запуска PostgreSQL и приложения через Docker Compose

Dockerfile — инструкция для сборки образа приложения
