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

Clone repository locally

```bash
cd coding-exercise-producer
mvn clean install
```

Optional if importing into Eclipse/STS
```bash
mvn eclipse:eclipse 
```

## Running the project via command line

- Make sure Mongo is running on localhost with url of `mongodb://localhost/`
- Optional for using JMS broker make sure ActiveMQ is running on the URL 'tcp://localhost:61616'

```bash
cd coding-exercise-producer
mvn clean package
```
- To run specifying <option> for `-api` as either `http` or `jms`

```bash
java -jar target/producer-0.0.1-SNAPSHOT.jar -api http
```
```bash
- java -jar target/producer-0.0.1-SNAPSHOT.jar -api jms
```

## Verifying data has been persisted

- In the console logs you should see some indication of events being received either via HTTP or JMS and persisted in the database
- Within the Mongo CLI you should see the data by doing the following;

```javascript
> use customer_event_repository
> db.customer_events.find()
```

- Note: the additional attribute 'apiType' has been added to the customer_event document. This indicates the API type by which the Producer sent the data to the Consumer - either HTTP_RESTFUL or JMS_BROKER
