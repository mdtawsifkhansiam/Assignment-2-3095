FROM eclipse-temurin:21-jre
VOLUME /tmp
# Copy ONLY the main app.jar, not the plain jar
COPY build/libs/app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]docker ps | findstr kafka
