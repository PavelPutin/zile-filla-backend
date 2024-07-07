FROM gradle:jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
ENV EXPOSE_SUBTREE_ROOT=$EXPOSE_SUBTREE_ROOT
RUN gradle build --no-daemon

FROM eclipse-temurin:21-jre
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]