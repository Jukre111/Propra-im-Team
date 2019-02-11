FROM openjdk:8-jdk-alpine AS build
WORKDIR .
COPY ./ ./
RUN ls
RUN ./gradlew --no-daemon --stacktrace clean bootJar


FROM openjdk:8-jre-alpine
RUN apk add --no-cache bash
WORKDIR .
COPY wait-for-it.sh .
COPY --from=build /build/libs/*.jar app.jar
CMD ./wait-for-it.sh db:5432 --timeout=0 -- java -jar app.jar
