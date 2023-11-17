FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ADD ./target/maanyuba-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]