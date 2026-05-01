# ── Stage 1: Build ────────────────────────────────────────────────────────────
# Use full JDK image to compile and package the application
FROM eclipse-temurin:21 AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml first — Docker layer caching means
# dependencies are only re-downloaded when pom.xml changes
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Download all dependencies (cached layer)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code and build the JAR
COPY src src
RUN ./mvnw package -DskipTests -B

# ── Stage 2: Run ──────────────────────────────────────────────────────────────
# Use JRE-only Alpine image — much smaller final image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy only the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Exec form — required by project spec
ENTRYPOINT ["java", "-jar", "app.jar"]
