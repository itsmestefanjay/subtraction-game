## Subtraction Game

This game is a variant of the Nim game. Each player is allowed to draw up to 3 sticks from a single stack of sticks. 
The player who draws the last stick loses.

### Prerequisites
- JDK 17+
- Internet connection to download dependencies

### Rules
- You start first
- You are allowed to draw between 1 and 3 sticks
- Then the computer does its move
- If you draw the last stick you lose, otherwise you win

### How to build
- go to the route directory of the project
- build the JAR by running `./gradlew bootJar` in your terminal

### How to play
- start the application by either
  - run `java -jar build/libs/subtraction-game-1.0.0.jar` or
  - run `./gradlew bootRun` or
  - use via the IDEA in the class `NimGameApplication.class`
- Open a REST client and set the host to `localhost:8089/subtraction-game/api/v1`
- To make a move send a PUT to the endpoint `/draw` with the JSON payload to draw 2 sticks:
```json
{
  "sticks": 2
}
```
- the response tells you what the computer did and how many sticks are left:
```json
{
  "sticksLeft": 13,
  "message": "Computer took 2 sticks. Your turn..."
}
```
- The game is over when no sticks are left:
```json
{
  "sticksLeft": 0,
  "message": "Computer took the last stick. You win!"
}
```
- you can restart the game by a PUT of the new amount of sticks (>1) to the endpoint `/restart`:
```json
{
  "initialSticks": 15
}
```

### Parameters
- you can configure the game two ways:
  - enabling expert mode by setting `-Dexpert.mode=true` or `-Dspring.profiles.active=expert` in the console or adding 
  the value to the default section in the `application.yaml`
  - setting the initial amount of sticks by setting `stick.amount: <random number>` in the application.yaml 
  or `-Dstick.amount=<random number>`
  - example: `java -Dspring.profiles.active=expert -Dstick.amount=46 -jar build/libs/subtraction-game-1.0.0.jar`
  - Make sure to add them BEFORE the -jar option and that the jar file was built

### About game modes
- The _standard mode_ implements a random amount of sticks to be drawn by the computer
- The _expert mode_ is just a simple implementation of Bouton's strategy where the winning strategy is to calculate the number `(stake - 1) % (maxDraw + 1)`
  - if the number is **not** 0, we remove the number from the stake
  - if the number is 0, we remove a random number between 1-3 (to make it more random again)
