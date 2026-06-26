FROM cgr.dev/chainguard/jdk:latest
WORKDIR /home/nonroot/app
COPY target/produto-service-0.0.1-SNAPSHOT.jar /home/nonroot/app/app.jar
EXPOSE 9002
CMD ["java", "-jar", "app.jar"]
