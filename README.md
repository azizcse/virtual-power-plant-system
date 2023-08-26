# Spring Boot, Virtual power plant system 

## Prerequisite
To run this application Your local environment must have 

1. Java 11+
2. Apache kafka_2.13-3.5.0
3. Redis

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/azizcse/virtual-power-service.git
```
## 2. Setup Apache Kafka

Download Kafka
```bash
https://www.apache.org/dyn/closer.cgi?path=/kafka/3.5.0/kafka_2.13-3.5.0.tgz
```
### Kafka with ZooKeeper
+ Open new terminal and run `bin/zookeeper-server-start.sh config/zookeeper.properties` to start ZooKeeper service
### Start the Kafka broker service
+ Open another new terminal and run `bin/kafka-server-start.sh config/server.properties` to start kafka service
+ To Clear Kafka cache and logs run `  rm -rf /tmp/kafka-logs /tmp/zookeeper /tmp/kraft-combined-logs`

*Note: To Run Apache Kafak in windows you need to run respective .bat script*

## 3. Install Redis server

To Setup redis please flow the instruction
```bash
https://redis.io/docs/getting-started/installation/
```

## 4. Run the application using gradle
```bash
./gradlew bootRun
```
The app will start running at <http://localhost:8080>

### JWT Auth API


| Method | Url                 | Decription | Sample Valid Request Body| 
| ------ |---------------------| ---------- |--------------------------|
| POST   | /api/v1/auth/signup | Sign up | [JSON](#signup)          |
| POST   | /api/v1auth/signin  | Log in | [JSON](#signin)          |

##### <a id="signup">Sign Up -> /api/v1/auth/signup</a>
```json
{
  "name": "Aziz",	
  "email": "example@gmail.com",
  "password": "password"
}
```
##### <a id="signin">Sign Up -> /api/v1/auth/signin</a>
```json
{
  "email": "example@gmail.com",
  "password": "password"
}
```
### **Battery Register and Search API**


| Method | Url                      | Decription                     | Sample Valid Request Body | 
|--------|--------------------------|--------------------------------|---------------------------|
| POST   | /api/v1/battery/register | Large list of battery register | [JSON](#battery_register) |
| GET    | /api/v1/battery/search   | Search battery with range      | [PARAM](#rangesearch)     |

##### <a id="battery_register">Battery Register -> /api/v1/battery/register</a>
```json
[
  {
    "name": "Hay Street",
    "postcode": "6000",
    "capacity": 23500
  },
  {
    "name": "Mount Adams",
    "postcode": "6525",
    "capacity": 12000
  }
]
```

##### <a id="rangesearch">Range Search -> /api/v1/battery/search</a>
```
startPostcode=100
endPostcode=400
minCapacity=23 (Optional) 
maxCapacity=50 (Optional)

```
### Endpoint to identify batteries below a certain capacity threshold.


| Method | Url                                          | Decription                | Sample Valid Request Body | 
|--------|----------------------------------------------|---------------------------|---------------------------|
| GET    | /api/v1/battery/below-capacity/{threshold}   | Large list Search         | [PARAM](#threshold)       |

##### <a id="threshold">Search with threshold-> /api/v1/battery/below-capacity/{threshold}</a>
```
API is pagination suported
page=1 (Optional)
size=30 (Optional)
```

## Kafka Realtime Data stream API

| Method | Url                      | Decription                                                             | Sample Valid Request Body | 
|--------|--------------------------|------------------------------------------------------------------------|---------------------------|
| POST   | /api/v2/battery/register | Large volume of battery list registration handle in distributed server | [JSON](#kafkaregister)    |
| PUT    | /api/v2/battery/{id}     | Battery capacity update realtime notifier                              | [JSON](#update)           |

##### <a id="kafkaregister">Register battery in distributed server-> /api/v2/battery/register</a>
```json
[
  {
    "name": "Hay Street",
    "postcode": "6000",
    "capacity": 23500
  },
  {
    "name": "Mount Adams",
    "postcode": "6525",
    "capacity": 12000
  }
]
```

##### <a id="update">Realtime Update battery capacity-> /api/v2/battery/{id}</a>
```json
  {
    "name": "Hay Street",
    "postcode": "6000",
    "capacity": 23500
  }
```

## Comments on Recommended Features
**Ensure the system can handle a large number of battery registrations concurrently.**
  > Added Async custom background dispatcher queue named BackgroundTaskManager.java. 
  > That capable to handle large number of battery registration. Each registration request will just enqueued the list of battery registration task

**Implement rate-limiting on the API to prevent misuse.**
  > Under development

**Extend the postcode range query to allow filtering based on minimum or maximum watt capacity.**
  > Enhanced api capability /api/v1/battery/search
  
**Introduce an endpoint to identify batteries below a certain capacity threshold.**
   > API Added /api/v1/battery/below-capacity/{threshold}

**Implement JWT-based authentication and authorization for the API.**
   > Added JWT Authentication system. You need to add JWT token to access the API

**Encrypt battery data at rest.**
   > In progress

**Introduce a real-time data stream (e.g., using Kafka or RabbitMQ) that sends updates on battery capacities.**
  >Apache Kafka added to handle large number of battery registration in API V2

**Provide an endpoint for real-time stats on aggregated battery capacities.**
   > Added Apache kafka

**Integrate a logging framework and log significant system events.**
   >- Standard logging system added with logback-spring.xml 
   >- Log file will be generated under root dir inside logs folder
   >- Normal logs will be added in Event-2023-08-24-0.log file named with current date
   >- Console log will be added in Error-2023-08-25-0.log file

**Add endpoints for health checks and metrics.**
  > Health and metrics check Endpoint added
  >- /actuator/metrics
  >- /actuator/health
  
**Implement caching using tools like Redis to improve query performance.**
   > Redis caching added in battery search api

**Design the system as a set of microservices with inter-service communication.**
  > Apache kafka works as a distributed microservices

**Along with unit tests, provide integration and end-to-end tests.**
  >Added Service class unite test, that not cover all the source
  > Because of time limit not able to cover all. 

**Implement a CI/CD pipeline for automated testing and deployment.**
  >In progress

**Containerize the application using Docker and provide Kubernetes manifests for deployment.**
  > Docker file added to create docker Image and run it in docker container

**Use Swagger or a similar tool for API documentation.**
  > In progress

**Ensure backward com**
  >API versioning added


### Kafka with ZooKeeper
MAC/Linux
$ bin/zookeeper-server-start.sh config/zookeeper.properties
Windows
$ bin\windows\zookeeper-server-start.bat config\zookeeper.properties

### Start the Kafka broker service
MAC/Linux
$ bin/kafka-server-start.sh config/server.properties
Windows
$ bin\windows\kafka-server-start.bat config\server.properties

  rm -rf /tmp/kafka-logs /tmp/zookeeper /tmp/kraft-combined-logs