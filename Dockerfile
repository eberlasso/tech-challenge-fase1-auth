# Estágio 1: Build (Maven + Java 21)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar o cache das dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e gera o pacote
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Execução (JRE 21)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copia o jar gerado no estágio de build
COPY --from=build /app/target/*.jar app.jar

# Define a porta que será exposta
EXPOSE 8080

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]