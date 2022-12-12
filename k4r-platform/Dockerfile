FROM maven:3.6.3-jdk-11 as MAVEN_TOOL_CHAIN

WORKDIR /build

COPY ./k4r-camel/src/ /build/k4r-camel/src/
COPY ./k4r-camel/pom.xml /build/k4r-camel/
COPY ./pom.xml /build

RUN mvn --batch-mode --update-snapshots \
      -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=WARN \
      -Dorg.slf4j.simpleLogger.showDateTime=true -Dorg.slf4j.simpleLogger.dateTimeFormat=HH:mm:ss,SSS \
      clean install -P docker -pl k4r-camel

FROM openjdk:11.0.6-jre
COPY --from=MAVEN_TOOL_CHAIN /build/k4r-camel/target/k4r-camel.jar /usr/app/app.jar
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
