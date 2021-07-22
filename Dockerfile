FROM openjdk:jdk-slim-buster

COPY "./target/tipear-SNAPSHOT.jar" "tipear.jar"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "tipear.jar"]
