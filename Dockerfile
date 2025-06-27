FROM maven:3.9.9-eclipse-temurin-17 as builder
COPY pom.xml ./
COPY ./src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
COPY --from=builder /target/*.jar /*.jar
ENTRYPOINT ["java", "-jar", "/*.jar"]