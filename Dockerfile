FROM alpine
# LABEL org.opencontainers.image.authors="my@name.com"
RUN apk add openjdk17-jre

# Define the user to use in this instance to prevent using root that even in a container, can be a security risk.
ENV APPLICATION_USER=vapi_user

# Then add the user, create the /app folder and give permissions to our user.
RUN adduser --disabled-password --gecos '' $APPLICATION_USER
RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

# Mark this container to use the specified $APPLICATION_USER
USER $APPLICATION_USER

COPY ./build/libs/vapi4k-template.jar /app/vapi4k-template.jar
COPY src/main/resources /app/src/main/resources

# Make /app the working directory
WORKDIR /app

EXPOSE 8080

CMD []
# Launch java to execute the jar with defaults intended for containers.
ENTRYPOINT ["java", "-server", "-XX:+UseContainerSupport", "-Xmx2048m", "-Dlogback.configurationFile=/app/src/main/resources/logback-do.xml", "-jar", "/app/vapi4k-template.jar"]
