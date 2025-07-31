FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests        \
    && ls -lh target

FROM eclipse-temurin:21-jre
WORKDIR /app

# Instala fuentes Arial y configura fontconfig
RUN apt-get update && \
    apt-get install -y --no-install-recommends fontconfig ttf-mscorefonts-installer && \
    fc-cache -fv && \
    rm -rf /var/lib/apt/lists/*

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-Xmx2048m", "-jar", "app.jar"]
