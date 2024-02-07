# Virtual Library

A Virtual Book Library built with Spring Boot.

## Description

Virtual Library is an online book library. It is a monolothic three tier application that supports various features that any traditional online library would have such as browsing, checkout, and searching. It supports login with Spring Security and utilizes Spring Data JPA along with PostgreSQL to load data. The front end of this project is handled by Thymeleaf and utilizes Spring Batch to load Book data into the DB which it then uses to generate 100k Book Recommendations.

## Getting Started

### Installing

* To install first clone the repository.
* From here go to src/main/resources/application.properties.
* Replace the following below with your local PostgresSQL Database for local configuration.
```
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=mysecretpassword
```
* Then you will want to run the following command
```
./mvnw clean package   
```
* After the build is completed move to executing the program.
* Note: We assume that you have a command line or IDE that has Maven installed.
* Initial setup with take around 5-10 minutes for DB setup and Recommendation System.

### Executing program

* From here run the following.
```
java -jar .\target\virtual-library-0.0.1-SNAPSHOT.jar
```
* The server should start up on Localhost:8081, however if that port is already taken change this in the settings.
