FROM eclipse-temurin:21-jre
VOLUME /tmp
# FIX: Explicitly copy app.jar to avoid picking up the 'plain' jar
COPY build/libs/app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]