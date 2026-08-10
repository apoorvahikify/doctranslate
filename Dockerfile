# ==============================
# Build stage
# ==============================

FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src
COPY tessdata ./tessdata

RUN mvn clean package -DskipTests


# ==============================
# Run stage
# ==============================

FROM eclipse-temurin:21-jre

WORKDIR /app

# Install Tesseract OCR
RUN apt-get update && \
    apt-get install -y \
    tesseract-ocr \
    tesseract-ocr-eng \
    tesseract-ocr-kan && \
    rm -rf /var/lib/apt/lists/*

# Copy application
COPY --from=build /app/target/*.jar app.jar

# Copy project tessdata
COPY --from=build /app/tessdata ./tessdata

# Render provides PORT automatically
EXPOSE 8081

CMD ["java", "-jar", "app.jar"]