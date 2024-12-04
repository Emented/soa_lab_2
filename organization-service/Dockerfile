FROM payara/server-full:6.2024.9-jdk17

COPY keys/organization-service.jks /opt/payara/appserver/glassfish/domains/domain1/config
COPY keys/manager-service.crt manager-service.crt

USER root
RUN keytool -importcert -keystore /opt/payara/appserver/glassfish/domains/domain1/config/cacerts.p12 -storepass changeit -alias manager-service -trustcacerts -file manager-service.crt -noprompt
USER payara

COPY build/libs/organization-service.war /opt/payara/deployments