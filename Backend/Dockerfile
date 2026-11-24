FROM maven:3.9-eclipse-temurin-17 AS build
ARG SERVICE
WORKDIR /app
COPY ${SERVICE}/pom.xml ./
COPY ${SERVICE}/src ./src
RUN --mount=type=cache,target=/root/.m2,sharing=locked \
    mvn -B -DskipTests package && cp target/*.jar /app/service.jar

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/service.jar ./service.jar
USER 10001:10001
ENTRYPOINT ["java", "-jar", "/app/service.jar"]
