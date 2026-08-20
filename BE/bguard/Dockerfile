# ---------- Stage 1: build ----------
# Immagine con JDK completo, usata solo per compilare — non finisce nell'immagine finale
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

# Copiamo prima solo i file necessari a Gradle per capire la struttura del progetto
# (utile anche per il caching dei layer Docker, anche se qui non lo ottimizziamo ulteriormente)
COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .

# Copiamo i tre moduli: app dipende da api e domain, servono tutti per la build
COPY domain domain
COPY api api
COPY app app

RUN chmod +x gradlew && ./gradlew :app:bootJar --no-daemon

# ---------- Stage 2: runtime ----------
# Solo JRE: niente compilatore, niente Gradle, niente sorgenti — immagine minimale
FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

COPY --from=build /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
