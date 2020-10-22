FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
COPY . /home/spring/spring-app
WORKDIR /home/spring/spring-app
RUN chown -R spring:spring /home/spring/
USER spring:spring
RUN ./mvnw package
EXPOSE 3000
ENTRYPOINT ["java","-jar","/home/spring/spring-app/target/authentication-rest-service-0.0.1-SNAPSHOT.jar"]