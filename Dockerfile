FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /build
COPY BasketballData/src/ ./src/
RUN javac --add-modules jdk.httpserver -d /build/bin /build/src/*.java

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
COPY --from=build /build/bin/ ./bin/
COPY BasketballData/NBAstats.csv BasketballData/BasicStats.csv BasketballData/allgames.csv BasketballData/weights.csv ./BasketballData/

ENV PORT=8080
EXPOSE 8080

CMD ["sh", "-c", "java --add-modules jdk.httpserver -cp /app/bin PredictServer /app/BasketballData ${PORT}"]
