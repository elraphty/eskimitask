FROM openjdk:8-jre-alpine

ADD target/scala-**/eskimitask-assembly-0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]