FROM java:8
VOLUME /tmp
COPY kafka-ui-lite-1.2.5.jar app.jar
EXPOSE 8520
ENTRYPOINT ["java","-jar","/app.jar"]