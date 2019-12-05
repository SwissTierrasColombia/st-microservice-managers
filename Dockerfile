FROM openjdk:12

VOLUME /tmp

ADD ./target/st-microservice-managers-0.0.1-SNAPSHOT.jar st-microservice-managers.jar

EXPOSE 8080

ENTRYPOINT java -jar /st-microservice-managers.jar