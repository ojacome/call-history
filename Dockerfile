#FROM eclipse-temurin:21-jdk-jammy
#
#WORKDIR /app
#
#COPY build/libs/tenpo-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jdk-jammy as builder

WORKDIR /workspace
COPY . .

RUN ./gradlew clean build

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=builder /workspace/build/libs/tenpo-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]