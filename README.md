# Java Betting API
## Description
This is a simple API that can be used to create an account, place bets, and play a simple game of chance. 

It uses an in-memory H2 database instance to persist information.

## Assumptions
1. Game id refers to a game entity in the database that describes what kind of game is being played.
2. Game activity id is used by the client of the API to identify that the response refers to the same game activity that was specified in the request. It is not the game activity entity's identifier in the database.

## Requirements
JRE 11+

Maven 3+

## Run the app
To run the application, run the main method in the source file 'JavaBettingApiApplication.java' from an IDE. This file is present in the following directory: '../java-betting-api/src/main/java/com/betting/javabettingapi/'. If running from an IDE, annotation processing has to be enabled due to the following dependencies: lombok and mapstruct.

Alternatively, the application can also be built into a JAR file and run with the following command: 

    java -jar java-betting-api.jar

By default, the application binds to port 8080.

## Run the tests
The tests can be run by opening the files under directory 
'../java-betting-api/src/test/java/com/betting/javabettingapi/' in an IDE and running them in there.

Alternatively, they can also be run via the following maven command in the project's root directory:

    mvn test

## REST Endpoints

## Create a new Account

### Request

`POST /api/v1/player`

    curl -i -H "Content-Type: application/json" -X POST -d '{"username":"username1"}' http://localhost:8080/api/v1/player

### Response

    HTTP/1.1 201 
    Location: /api/v1/player/1
    Content-Length: 0
    Date: Fri, 12 Mar 2023 21:28:23 GMT

## Get Account Details

### Request

`GET /api/v1/player/playerId`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/v1/player/1

### Response

    HTTP/1.1 200 
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Fri, 12 Mar 2023 21:31:15 GMT

    {"id":1,"username":"username1", "wallet":{"id":1,"balance":2000.00,"currency":"EUR"}}

## Place Bet

### Request

`POST /api/v1/gameactivity`

    curl -i -H "Content-Type: application/json" -X POST -d '{"gameActivityId":"17841111117391","betAmount":10.50,
    "currency":"EUR","playerId":"1","gameId":"1"}' http://localhost:8080/api/v1/gameactivity

### Response

    HTTP/1.1 201 
	Content-Type: application/json
	Transfer-Encoding: chunked
	Date: Mon, 12 Mar 2023 21:32:02 GMT

	{"gameActivityId":"1784111117391","outcome":"LOSS","winAmount":-10.50,"currency":"EUR","playerBalanceAfter":1979.00}

## Get List of All Bets

### Request

`GET /api/v1/gameactivity?limit=2&offset=0`

    curl -i -H 'Accept: application/json' "http://localhost:8080/api/v1/gameactivity?limit=2&offset=0"

### Response

    HTTP/1.1 200 
	Content-Type: application/json
	Transfer-Encoding: chunked
	Date: Mon, 12 Mar 2023 21:33:38 GMT

	{"betResultList":[{"gameActivityId":"17841111117391","outcome":"LOSS","winAmount":-10.50,"currency":"EUR","playerBalanceAfter":1989.50,"betAmount":10.50,"gameId":"1"},{"gameActivityId":"1784111117391","outcome":"LOSS","winAmount":-10.50,"currency":"EUR","playerBalanceAfter":1979.00,"betAmount":10.50,"gameId":"1"}]}
