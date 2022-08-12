FROM openjdk:11

ARG XMX=1024m
ARG PROFILE=production
ARG CLOUD_CONFIG
ARG NEW_RELIC_ENVIRONMENT

ENV XMX=$XMX
ENV PROFILE=$PROFILE
ENV CLOUD_CONFIG=$CLOUD_CONFIG
ENV NEW_RELIC_ENVIRONMENT=$NEW_RELIC_ENVIRONMENT

VOLUME /tmp

ADD ./target/st-microservice-managers-1.5.4-beta.1.jar st-microservice-managers.jar
ADD ./target/newrelic.jar newrelic.jar
ADD ./newrelic.yml newrelic.yml

EXPOSE 8080

ENTRYPOINT java -javaagent:/newrelic.jar -Dnewrelic.environment=$NEW_RELIC_ENVIRONMENT -Xmx$XMX -jar /st-microservice-managers.jar --spring.profiles.active=$PROFILE --spring.cloud.config.uri=$CLOUD_CONFIG