# coding-exercise-producer

This is the Producer component designed to be run with the Consumer component from coding-exercise-consumer. The function of the Producer is to read the source.txt file from it's resources folder. This customer event data is in the format of individual JSON lines (note seperate lines of JSON - not a JSON array).

This data is then sent to the Consumer component by either a HTTP Restful API or JMS Broker. The default is HTTP Rest although this can be configured at runtime via a runtime arguement (see below).

## Prerequisites

- Java 8
- MongoDB version 3.x
- Apache ActiveMQ (optional if wanting to use the JMS Broker)
- Maven 3.x (for development)

## Building the project

The project is built using Spring 4 Boot.

- Clone repository locally
- cd coding-exercise-producer
- mvn clean install
- mvn eclipse:eclipse (optional if importing into Eclipse/STS)

## Running the project via command line

- cd coding-exercise-producer
- mvn clean package
- java -jar target/producer-0.0.1-SNAPSHOT.jar -api http
- java -jar target/producer-0.0.1-SNAPSHOT.jar -api jms

