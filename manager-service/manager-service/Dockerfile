FROM openjdk:17
COPY build/libs/manager-service-0.0.1-SNAPSHOT.jar manager-service.jar

COPY keys/manager-service.jks manager-service.jks
COPY keys/organization-service-new.crt organization-service-new.crt
RUN keytool -import -trustcacerts -alias organization-service -file organization-service-new.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

ENTRYPOINT ["java", "-jar", "-Djavax.net.ssl.trustStore=classpath:manager-service.jks", "-Djavax.net.ssl.trustStorePassword=changeit", "manager-service.jar"]