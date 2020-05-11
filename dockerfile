FROM openjdk:8-jdk-alpine
MAINTAINER appsdeveloperblog.com
ARG JAR_FILE=target/PhotoAppApiUsers-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} PhotoAppApiUsers-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/PhotoAppApiUsers-0.0.1-SNAPSHOT.jar"]