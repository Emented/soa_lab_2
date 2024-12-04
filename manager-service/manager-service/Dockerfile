FROM openjdk:17
COPY build/libs/manager-service-0.0.1-SNAPSHOT.jar manager-service.jar

COPY keys/manager-service.jks manager-service.jks
COPY keys/organization-service.crt organization-service.crt
RUN keytool -import -trustcacerts -alias organization-service -file organization-service.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

ENTRYPOINT ["java", "-jar", "-Djavax.net.debug=all", "-Djavax.net.ssl.trustStore=classpath:manager-service.jks", "-Djavax.net.ssl.trustStorePassword=admin_pass", "manager-service.jar"]