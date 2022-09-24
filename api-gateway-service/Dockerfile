# Run
FROM openjdk:11.0-jre
ENV HOME /opt/app
WORKDIR $HOME

COPY target/*.jar $HOME/app.jar

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.7.2/opentelemetry-javaagent-all.jar .

EXPOSE 80

ENTRYPOINT java -javaagent:${HOME}/opentelemetry-javaagent-all.jar -jar app.jar -v
