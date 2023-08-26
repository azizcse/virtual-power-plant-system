FROM openjdk:17
EXPOSE 8080
ADD build/libs/*.jar virtual_power_system.jar
ENTRYPOINT ["java","-jar","virtual_power_system.jar"]