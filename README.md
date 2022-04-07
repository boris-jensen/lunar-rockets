# lunar-rockets

A service to receive rocket messages and present the resulting rocket states

## Running the program
The program requires java 17

To compile:
```
mvn clean package
```

To run: 
```
java -jar target/rockets-0.0.1-SNAPSHOT.jar
```

## Web interface specification
The running service exposes an openapi v3 interface specification at 

```
http://localhost:8088/v3/api-docs/
```

and exposes a swagger UI at 
```
http://localhost:8088/swagger-ui/index.html
```

## Code structure
The code is split in 2 packages: `com.lunar.rockets.controller` and `com.lunar.rockets.domain`. The `domain` package is the pure domain logic, which handles the rocket state machine, message reordering and message deduplication, and the `controller` package exposes a simple web interface to the domain service, marshalling and unmarshalling the messages and rockets, and mapping to and from the domain classes. 

The `RocketState` class handles the simple rocket state machine. There are a couple of TODOs in there, where the specifications were unclear to me. The `MessageQueue` class handles message reordering and deduplication. It is not thread safe, so the `acceptMessage` method on the `RocketService` class is synchronized

The `MessageController` and `RocketController` classes expose the web interface. An example query for the message controller is

```
POST http://localhost:8088/messages
```

with json body

```
{
    "metadata": {
        "channel": "193270a9-c9cf-404a-8f83-838e71d9ae67",
        "messageNumber": 1,
        "messageTime": "2022-02-02T19:39:05.86337+01:00",
        "messageType": "RocketLaunched"
    },
    "message": {
        "type": "Falcon-9",
        "launchSpeed": 500,
        "mission": "ARTEMIS"
    }
}
```

and after messages have been posted, the rockets are available at 
```
http://localhost:8088/rockets
```
and individual rockets at
```
http://localhost:8088/rockets/<channel>
```

There is currently no persistence configured, so all rocket states will be dropped once the service is shut down.

## Testing via supplied program
The service only exposes an http endpoint, not https, so it fails the test program when run with default values. 

When I run the test program with an http endpoint argument like this

```
./rockets launch "http://localhost:8088/messages" --message-delay=500ms --concurrency-level=1
```

I get errors like the following

```
2022/04/07 09:04:51 [error]	 Failed to sent message: status code 415: "{\"timestamp\":\"2022-04-07T07:04:51.228+00:00\",\"status\":415,\"error\":\"Unsupported Media Type\",\"path\":\"/messages\"}"
```

It seems that the request sent by the test program requires another media type. I haven't had time to debug this, or setup https.

## Tests
The test folder contains test classes for the `RocketState`, `MessageQueue` and `RocketService` classes. These classes contain the most important business logic. There are no tests of the web controllers due to time constraints.

## Todo
These are the major tasks remaining in the code:

- Expose https
- Fix Unsupported media type from `rockets` program
- Remove synchronized requirement for `acceptMessage` method, and add multithreaded service test.
- Clarify business logic TODOs
- Persistence
- Testing web controller classes
