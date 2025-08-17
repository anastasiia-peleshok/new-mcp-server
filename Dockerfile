FROM openjdk:17-jdk-slim
COPY target/mcp-server-0.0.1-SNAPSHOT.jar /server.jar
ENTRYPOINT ["java", "-jar", "/server.jar"]
