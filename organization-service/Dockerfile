FROM openjdk:17
COPY build/libs/organization-service-0.0.1-SNAPSHOT.jar organization-service.jar

COPY keys/organization-service.jks organization-service.jks
COPY keys/manager-service.crt manager-service.crt
RUN keytool -import -trustcacerts -alias manager-service -file manager-service.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

ENTRYPOINT ["java", "-jar", "-Djavax.net.ssl.trustStore=classpath:organization-service.jks", "-Djavax.net.ssl.trustStorePassword=admin_pass", "organization-service.jar"]