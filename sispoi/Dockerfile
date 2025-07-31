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
    apt-get install -y --no-install-recommends \
    fontconfig \
    fonts-liberation \
    fonts-dejavu \
    wget \
    unzip \
    && mkdir -p /usr/share/fonts/truetype/msttcorefonts \
    && cd /tmp \
    && wget -q https://github.com/corefonts/corefonts/raw/master/arial.ttf \
    && wget -q https://github.com/corefonts/corefonts/raw/master/arialbd.ttf \
    && wget -q https://github.com/corefonts/corefonts/raw/master/ariali.ttf \
    && wget -q https://github.com/corefonts/corefonts/raw/master/arialbi.ttf \
    && mv *.ttf /usr/share/fonts/truetype/msttcorefonts/ \
    && fc-cache -fv \
    && fc-list | grep -i arial \
    && apt-get remove -y wget unzip \
    && rm -rf /var/lib/apt/lists/* /tmp/*

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-Xmx2048m", "-jar", "app.jar"]
