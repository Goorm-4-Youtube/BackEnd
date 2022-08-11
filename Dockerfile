FROM openjdk:11
WORKDIR /var/jenkins_home/workspace/BackEnd_Pipeline
ARG JAR_FILE=youtube-clone-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} .
#COPY ${JAR_FILE} /var/jenkins_home/workspace/BackEnd_Pipeline/*.jar
#COPY /var/lib/jenkins/workspace/teamproject_CI_1/target/backend-0.0.1-SNAPSHOT.jar /home/ubuntu/*.jar
CMD java -jar youtube-clone-0.0.1-SNAPSHOT.jar
