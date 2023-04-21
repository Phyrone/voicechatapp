# Builder
FROM azul/zulu-openjdk:17 as builder
WORKDIR /build/
COPY . .
RUN ./gradlew build --no-daemon
# Using a builder image to build the application and then copying the jar file to a new image without the build tools or cache files

# Might use Apline Linux for a smaller image (azul/zulu-openjdk-alpine:17)
# Application
FROM azul/zulu-openjdk:17
WORKDIR /application/
VOLUME /application/
COPY --from=builder /build/server/build/libs/*.jar /var/lib/app.jar
CMD ["java", "-jar", "/var/lib/app.jar"]