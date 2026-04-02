FROM eclipse-temurin:21-jdk-alpine
# O Docker vai buscar o JAR que o IntelliJ enviou para a pasta target sincronizada
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]