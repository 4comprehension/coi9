FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY target/rental-store.jar app.jar

ENV SERVER_PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "-Duser.language=en", "-Duser.country=US"]

HEALTHCHECK --interval=5s --timeout=3s --start-period=10s --retries=5 \
  CMD wget --quiet --spider http://localhost:8080/health || exit 1
