FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD target/protools-activiti-poc-0.0.1-SNAPSHOT.jar protools-activiti-poc-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/protools-activiti-poc-0.0.1-SNAPSHOT.jar"]
