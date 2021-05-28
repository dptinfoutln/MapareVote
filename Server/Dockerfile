FROM openjdk:11-jre
COPY ./target/*withdependencies.jar /app.jar

COPY ./java.security /usr/local/openjdk-11/conf/security/java.security

CMD ["java","-jar","/app.jar"]
