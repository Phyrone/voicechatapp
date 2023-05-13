FROM azul/zulu-openjdk:20 as builder
WORKDIR /build
COPY . .
RUN ./gradlew server:build --no-daemon


FROM --platform=$BUILDPLATFORM azul/zulu-openjdk-alpine:20-jre-headless
COPY --from=builder /build/server/build/server-*.jar /var/lib/vipserver/server.jar
WORKDIR /app
CMD ["java", "-jar", "/var/lib/vipserver/server.jar"]
