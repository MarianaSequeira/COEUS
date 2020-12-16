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
FROM tomcat:7.0.105

COPY docker/mysql-connector-java-5.1.16-bin.jar /usr/local/tomcat/lib
COPY docker/sqljdbc4-4.0.jar /usr/local/tomcat/lib
COPY docker/sqljdbc4.jar /usr/local/tomcat/lib

COPY --from=builder ./coeus/target/coeus-2.2.war /usr/local/tomcat/webapps/coeus-2.2.war


#ADD target/coeus-2.2.war /usr/local/tomcat/webapps

# Add docker-compose-wait tool -------------------
ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait

CMD /wait && catalinaa.sh run

#CMD [“catalina.sh”, “run”]