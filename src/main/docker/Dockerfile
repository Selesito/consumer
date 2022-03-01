FROM maven:3.6.3-openjdk-11
RUN mkdir comsumer
WORKDIR consumer
COPY . .
RUN mvn package -Dmaven.test.skip=true
EXPOSE 8082
CMD ["java", "-jar", "target/consumer-0.0.1-SNAPSHOT.jar"]
