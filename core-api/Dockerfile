# Stage 1 : build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copier d'abord les fichiers de dépendances pour profiter du cache Docker
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Télécharger les dépendances (layer cachée si pom.xml ne change pas)
RUN ./mvnw dependency:go-offline -q

# Copier les sources et compiler
COPY src ./src
RUN ./mvnw package -DskipTests -q

# Stage 2 : runtime
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Copier uniquement le jar depuis le stage builder
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]