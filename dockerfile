FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 7000

CMD ["java", "-jar", "app.jar"]