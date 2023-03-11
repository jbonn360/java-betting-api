# Simple Betting API
## Description
This is a simple API that can be used to create an account, place bets, and play a simple game of chance. This is a 
multiplayer game that allows multiple clients connected to the server to place a bet on the same outcome, referred to
as a roll.

By default, a new number is rolled every one minute if there's at least one bet associated with the current roll. If no bets
are found, the roll is postponed.

## Requirements
JRE 11+

Kotlin 1.7.0+

Maven 3+

## Run the app
To run the application, run the main function in the Kotlin source file 'SimpleBettingApiApplication.kt' from an IDE. This file is present in the following directory: '../simple-betting-api/src/main/kotlin/com/betting/simplebettingapi/'

Alternatively, the application can also be built into a JAR file and run with the following command: 

    java -jar simple-betting-api.jar

By default, the application binds to port 8080.

## Run the tests
The tests can be run by opening the files under directory 
'../simple-betting-api/src/test/kotlin/com/betting/simplebettingapi/' in an IDE and running them in there.

Alternatively, they can also be run via the following maven command in the project's root directory:

    mvn test


## REST Endpoints

## Create a new Account

### Request

`POST /api/v1/account`

    curl -i -H "Content-Type: application/json" -X POST -d '{"username":"username1","name":"name1","surname":"surname1"}' http://localhost:8080/api/v1/account

### Response

    HTTP/1.1 201 
    Location: /api/v1/account/3
    Content-Length: 0
    Date: Fri, 10 Mar 2023 21:28:23 GMT

## Get Account Details

### Request

`GET /api/v1/account/accountId`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/v1/account/1

### Response

    HTTP/1.1 200 
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Fri, 10 Mar 2023 21:31:15 GMT

    {"id":1,"username":"username1","name":"name1","surname":"surname1","wallet":{"balance":1000.00}}

## Get List of Transactions for an Account

### Request

`GET /api/v1/account/accountId/transactions`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/v1/account/1/transactions

### Response

    HTTP/1.1 200 
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Fri, 10 Mar 2023 21:33:46 GMT

    {"transactions":[{"transactionDt":"2023-03-10T21:25:50.559611Z","transactionType":"INITIAL_DEPOSIT",
    "amount":1000.00,"balanceBefore":0.00,"balanceAfter":1000.00}]}

## Place a Bet

### Request

`POST /api/v1/account/accountId/bets`

    curl -i -H "Content-Type: application/json" -X POST -d '{"betAmount": 900,"numberBetOn": 10}' 
    http://localhost:8080/api/v1/account/1/bets

### Response

    HTTP/1.1 201 
    Location: /api/v1/account/1/bets/1
    Content-Length: 0
    Date: Fri, 10 Mar 2023 21:36:34 GMT


## Get Bet Details

### Request

`GET /api/v1/account/accountId/bets/betId`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/v1/account/1/bets/1

### Response

    HTTP/1.1 200 
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Fri, 10 Mar 2023 21:37:56 GMT

    {"betAmount":900.00,"numberBetOn":10,"amountWon":0,"status":"LOST","placedDt":"2023-03-10T21:36:34.365437Z","roll":
    {"rollDt":"2023-03-10T21:36:52.673755Z","numberRolled":7}}

## Get List of Bets for an Account

### Request

`GET /api/v1/account/accountId/bets`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/v1/account/1/bets

### Response

    HTTP/1.1 200 
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Fri, 10 Mar 2023 21:39:37 GMT

    {"bets":[{"betAmount":900.00,"numberBetOn":10,"amountWon":0,"status":"LOST","placedDt":"2023-03-10T21:36:34.365437Z",
    "roll":{"rollDt":"2023-03-10T21:36:52.673755Z","numberRolled":7}}]}

## Get Leaderboard

### Request

`GET /api/v1/leaderboard`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/v1/leaderboard

### Response

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Fri, 10 Mar 2023 21:41:09 GMT

    {"leaders":[{"username":"username1","winnings":5000},{"username":"username15","winnings":2000},
    {"username":"username3","winnings":1000},{"username":"username4","winnings":850},
    {"username":"username2","winnings":200}]}

## Get Current Roll

### Request

`GET /api/v1/roll`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/v1/roll

### Response

    HTTP/1.1 200 
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Fri, 10 Mar 2023 21:43:06 GMT

    {"rollDt":"2023-03-10T21:43:53.655930537Z"}