FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/kafka-distributor-reader-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
# Add a custom script to modify properties before running the application
COPY modify-properties.sh /modify-properties.sh
RUN chmod +x /modify-properties.sh

ENTRYPOINT ["/modify-properties.sh"]