#####################################################################################
# Build Spring Boot Application
#####################################################################################
FROM maven:3.6.1-jdk-8-alpine as builder

COPY . ./coeus
WORKDIR /coeus

RUN mvn clean package -DskipTests


#####################################################################################
# Run Application
#####################################################################################
#FROM kanesee/tomcat-wait

#COPY docker/*.jar /usr/local/tomcat/lib/
#COPY docker/tomcat-users.xml /usr/local/tomcat/conf
#COPY datasets ./datasets

#COPY --from=builder ./coeus/target/coeus-2.2.war /usr/local/tomcat/webapps/coeus.war


FROM tomcat:7.0.105

COPY docker/*.jar /usr/local/tomcat/lib/
COPY docker/tomcat-users.xml /usr/local/tomcat/conf
COPY datasets ./datasets

COPY --from=builder ./coeus/target/coeus-2.2.war /usr/local/tomcat/webapps/

ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait

RUN ls -l
CMD /wait && catalina.sh run