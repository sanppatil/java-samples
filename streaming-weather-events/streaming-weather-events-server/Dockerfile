FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY target/sprint-boot-streaming-1.0.jar sprint-boot-streaming-1.0.jar
ENTRYPOINT ["java","-jar","/sprint-boot-streaming-1.0.jar"]