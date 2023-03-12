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

