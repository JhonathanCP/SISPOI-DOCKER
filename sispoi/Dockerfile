FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests        \
    && ls -lh target

FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia fuentes Arial desde resources e instala fontconfig
COPY src/main/resources/fonts/*.ttf /usr/share/fonts/truetype/arial/
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    fontconfig \
    && fc-cache -fv \
    && fc-list | grep -i arial \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-Xmx2048m", "-jar", "app.jar"]
