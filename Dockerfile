FROM adoptopenjdk/openjdk11:jre-nightly

WORKDIR /app
EXPOSE 8080
COPY build/libs/kotlin-auth-base-api-all.jar .
ENTRYPOINT ["java", "-jar", "kotlin-auth-base-api-all.jar"]