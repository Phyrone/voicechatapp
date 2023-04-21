FROM azul/zulu-openjdk:17 as builder
WORKDIR /build/
COPY . .
RUN ./gradlew build --no-daemon
FROM azul/zulu-openjdk:17

COPY --from=builder /build/build/libs/*.jar /var/lib/app.jar
CMD ["java", "-jar", "/var/lib/app.jar"]