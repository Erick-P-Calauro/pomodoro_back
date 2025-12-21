FROM maven:3.9.12-eclipse-temurin-21-alpine AS base

WORKDIR /usr/app

COPY . .

RUN mvn clean install -DskipTests

RUN openssl genpkey -algorithm RSA -out ./src/main/resources/app.key -pkeyopt rsa_keygen_bits:2048

RUN openssl rsa -in ./src/main/resources/app.key -pubout -out ./src/main/resources/app.pub

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]