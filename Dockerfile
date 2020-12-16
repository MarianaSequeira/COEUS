FROM tomcat:7.0.105

COPY docker/mysql-connector-java-5.1.16-bin.jar /usr/local/tomcat/lib
COPY docker/sqljdbc4-4.0.jar /usr/local/tomcat/lib
COPY docker/sqljdbc4.jar /usr/local/tomcat/lib

ADD target/coeus-2.2.war /usr/local/tomcat/webapps