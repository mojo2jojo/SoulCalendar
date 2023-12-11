# Welcome to Soul Calendar!

This Java / Maven / Spring Boot (version 3.2.0) based project provides simple API for managing user calendars and scheduling events.
This README provides an overview of the **Soul Calendar Service**, including its functionality, setup instructions, technical considerations, and potential improvements.
<p align="center">
  <img src="https://media1.tenor.com/m/0xj4Ir5KJJYAAAAC/calendar-invite.gif" alt="animated" />
</p>

## Title

* [Requirements](#requirements)
* [How to start?](#how-to-start)
* [About service & choices made](#about-service--choices-made)
* [Endpoints you can call](#endpoints-you-can-call)
* [Future improvements](#future-improvements)
* [Contact](#contact)

## Requirements

* Spring boot : version 3.2.0
* Maven : apache-maven-3.8.X
* Java : version 17

My configuration :

```
ankitver-mac:calendar ankitver$ mvn --version
Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
Maven home: /usr/local/Cellar/maven/3.8.6/libexec
Java version: 21, vendor: XXXX, runtime: /Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home
Default locale: en_IN, platform encoding: UTF-8
OS name: "mac os x", version: "13.5.1", arch: "x86_64", family: "mac"
```
```
ankitver-mac:calendar ankitver$ java -version
java version "21" 2023-09-19 LTS
Java(TM) SE Runtime Environment (build 21+35-LTS-2513)
Java HotSpot(TM) 64-Bit Server VM (build 21+35-LTS-2513, mixed mode, sharing)
```
## How to start?

1. Clone this repository
2. Install the required dependencies:  
	#### navigate to dir having *pom.xml* file and run
	```
	mvn clean package
	```
3. Once successfully built, you can run the service by one of these two methods:
	```
	mvn spring-boot:run 
	OR
	java -jar target/calendar-0.0.1-SNAPSHOT.jar
	```
Once the application runs you should see something like this

```
2023-12-11T13:14:03.244+05:30  INFO 6698 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
2023-12-11T13:14:03.259+05:30  INFO 6698 --- [           main] com.soul.calendar.CalendarApplication    : Started CalendarApplication in 4.455 seconds (process running for 4.951)
```
## About Service & choices made

Service uses an **in-memory database (H2)**  to store the data. Service run on **port 8080**. 

Here is what this little application demonstrates: 

* Full integration with the latest **Spring boot**. Framework itself provide: inversion of control, dependency injection, etc. Framework makes me adhere to best practices. 
* Packaging as a single war with embedded container (tomcat 8): No need to install a container separately on the host just run using the ``java -jar`` command
* Exception mapping from application exceptions to the right HTTP response with exception details in the body
* *Spring Data* Integration with JPA/Hibernate with just a few lines of configuration and familiar annotations. 

### Why used H2 database? 

#### Following are the reason to use (Biggest reason is simple to use)
*   Can run as an in-memory database.
*   Simple and quick to get started with, and is light weight (only 2MB).
*   SQL compliant so it is compatible with most relational databases.

#### Access H2 db console
* It automatically starts when you start service
* In browser ``` http://localhost:8080/h2-console``` to access console.
* Properties of database
	```
	spring.datasource.url=jdbc:h2:mem:calendardb
	spring.datasource.driverClassName=org.h2.Driver
	spring.datasource.username=root
	spring.datasource.password=root
	spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
	spring.h2.console.enabled=true
	``` 
## Endpoints you can call

* Following operation can be performed.
	1. [Create user](#create-user)
	2. [Update user by userid](#update-user-by-userid)
	3. [Get details of user by id](#get-details-of-user-by-id)
	4. [Get all user](#get-all-user)
	5. [Create event | Book slot](#create-event--book-slot)
	6. [List all events of a user by userId](#list-all-events-of-a-user-by-userid)
	7. [Get details of an event by eventId](#get-details-of-an-event-by-eventid)
	8. [Find conflict slots for a userid](#find-conflict-slots-for-a-userid)
	9. [Find favourable slot for a list of userId](#find-favourable-slot-for-a-list-of-userid)
	10. [Recur existing event by event id ](#recur-existing-event-by-event-id)

#### Create user

* HTTP Method:  POST
* URL:  http://localhost:8080/api/user
* Curl command 
	```
	curl --location 'http://localhost:8080/api/user' \
	--header 'Content-Type: application/json' \
	--data-raw '{
	    "name": "Ankit",
	    "email": "dsds@gmail.com"
	}'
	```
* Response 
	```
	{
    "name": "Ankit",
    "email": "dsds@gmail.com",
    "updatedAt": "2023-12-11T09:33:22.367+00:00",
    "id": 1,
    "createdAt": "2023-12-11T09:33:22.366+00:00"
	}
	```
* Note: None
	
#### Update user by userId

* HTTP Method:  PUT
* URL:  http://localhost:8080/api/user/{userId}
* Curl command 
	```
	curl --location --request PUT 'http://localhost:8080/api/user/1' \
	--header 'Content-Type: application/json' \
	--data-raw '{
	    "name": "Verma",
	    "email": "dsdsds@gmail.com",
	    "updatedAt": "2023-12-10T20:15:08.835+00:00",
	    "createdAt": "2023-12-10T20:15:08.835+00:00",
	    "id": 1
	}'
	```
* Response 
	```
	{
    "name": "Verma",
    "email": "dsdsds@gmail.com",
    "updatedAt": "2023-12-11T09:33:22.367+00:00",
    "id": 1,
    "createdAt": "2023-12-10T20:15:08.835+00:00"
	}
	```
* Note: None

#### Get details of user by Id

* HTTP Method:  GET
* URL:  http://localhost:8080/api/user/{userId}
* Curl command 
	```
	curl --location --request GET 'http://localhost:8080/api/user/1'
	```
* Response 
	```
	{
    "name": "Verma",
    "email": "dsdsds@gmail.com",
    "updatedAt": "2023-12-11T09:33:22.367+00:00",
    "id": 1,
    "createdAt": "2023-12-10T20:15:08.835+00:00"
	}
	```
* Note: None

#### Get all user

* HTTP Method:  GET
* URL:  http://localhost:8080/api/user
* Curl command 
	```
	curl --location --request GET 'http://localhost:8080/api/user'
	```
* Response 
	```
	[
	    {
	        "name": "Ankit",
	        "email": "dsds@gmail.com",
	        "updatedAt": "2023-12-11T09:33:22.367+00:00",
	        "id": 1,
	        "createdAt": "2023-12-11T09:33:22.366+00:00"
	    }
	]
	```
* Note: No pagination added

#### Create event | Book slot

* HTTP Method:  POST
* URL:  http://localhost:8080/api/events
* Curl command 
	```
	curl --location 'http://localhost:8080/api/events' \
	--header 'Content-Type: application/json' \
	--data '{
	    "host": "1",
	    "participants": [2,3],
	    "startAt": "2023-12-10T14:30:00.156+00:00",
	    "endAt": "2023-12-10T15:30:00.156+00:00"
	}'
	```
* Response 
	```
	{
        "host": "1",
	    "participants": [2,3],
	    "startAt": "2023-12-10T14:30:00.156+00:00",
	    "endAt": "2023-12-10T15:30:00.156+00:00"
	    "updatedAt": "2023-12-11T09:33:22.367+00:00",
        "id": 1,
        "createdAt": "2023-12-11T09:33:22.366+00:00"
	}
	```
* Note:  
  * Users can create busy slots for themselves by providing empty participants
  * Host id is taken in body because auth does not exist. Otherwise we can fetch it from token. 
  * Day is not considered. You can book slot for alone or with many people using this same endpoint for a given startAt or endAt

#### List all events of a user by userId

* HTTP Method:  GET
* URL:  http://localhost:8080/api/events?userId={someId}
* Curl command 
	```
	curl --location 'http://localhost:8080/api/events?userId=1'
	```
* Response 
	```
	{
		"eventList": [1,2,3]
	}
	```
* Note: 
	* No pagination added
	* Return list of eventId. Get details of all events by calling below api.

#### Get details of an event by eventId

* HTTP Method:  GET
* URL:  http://localhost:8080/api/events/{eventId}
* Curl command 
	```
	curl --location 'http://localhost:8080/api/events/1'
	```
* Response 
	```
	{
        "host": "1",
	    "participants": [2,3],
	    "startAt": "2023-12-10T14:30:00.156+00:00",
	    "endAt": "2023-12-10T15:30:00.156+00:00"
	    "updatedAt": "2023-12-11T09:33:22.367+00:00",
        "id": 1,
        "createdAt": "2023-12-11T09:33:22.366+00:00"
	}
	```
* Note: None

####  Find conflict slots for a userId

* HTTP Method:  GET
* URL:  http://localhost:8080/api/events/conflicts?userId={someUserId}
* Curl command 
	```
	curl --location 'http://localhost:8080/api/events/conflicts?userId=1'
	```
* Response 
	```
	{
        "1" : {
	        {
		        "host": "1",
			    "participants": [2,3],
			    "startAt": "2023-12-10T14:30:00.156+00:00",
			    "endAt": "2023-12-10T15:30:00.156+00:00"
			    "updatedAt": "2023-12-11T09:33:22.367+00:00",
		        "id": 1,
		        "createdAt": "2023-12-11T09:33:22.366+00:00"
			},
			{
		        "host": "1",
			    "participants": [2,3],
			    "startAt": "2023-12-10T14:30:00.156+00:00",
			    "endAt": "2023-12-10T15:30:00.156+00:00"
			    "updatedAt": "2023-12-11T09:33:22.367+00:00",
		        "id": 2,
		        "createdAt": "2023-12-11T09:33:22.366+00:00"
			},
        }
	}
	```
* Note: 
	* Returns a list of conflicts along with conflicting even per conflict.
	* A flaw exists. In response, the "startAt" and "endAt" times are overridden. Only check the id and then the details to get the entire event information.
	* Conflict is check for all event. No duration is taken. 
	* Pagination is not present


#### Find favourable slot for a list of userId

* HTTP Method:  POST
* URL:   http://localhost:8080/api/events/findFavourableSlot
* Curl command 
	```
	curl --location 'http://localhost:8080/api/events/findFavourableSlot' \
	--header 'Content-Type: application/json' \
	--data '{
	    "participants": [1,2,3],
	    "durationInMinutes": 60
	}'
	```
* Response 
	```
	{
		"EndAt":"2023-12-11T00:59:00.156+00:00",
		"StartAt":"2023-12-10T23:59:00.156+00:00"
	}
	```
* Note:  
  * Users can find favourable slot for a list of participant.
  * Favourable slot is calculated from current time. 
  * Max limit for Favourable slot is 1 year from current time. 
  * If no slot exist EndAt & StartAt time will be same

#### Recur existing event by event id  

* HTTP Method:  POST
* URL: http://localhost:8080/api/events/recurringMeeting
* Curl command 
	```
	curl --location 'http://localhost:8080/api/events/recurringMeeting' \
	--header 'Content-Type: application/json' \
	--data '{
		"eventId": 1,
		"offsetInDays": 10,
		"frequency": 3
	}'
	```
* Response 
	```
	{
		Created
	}
	```
* Note:  
  * Users can recur an existing event only.
  * User authorization to recur event is not checked. 


## Future Improvements

The project can be further improved in the following ways:

* Conduct thorough testing.
* Making code more modular. Breaking service functions into smaller units so that it is easy to test.
* Implement user authentication and authorization with JWT tokens.
* Improving logging.
* Use Lombok library.
* Making better decisions about api signature , db schema etc.  
* Focus on making things rigid and complete before adding new functionality.

## Contact

For any questions or feedback, please contact Ankit Verma. Email: vermaav861997@gmail.com
