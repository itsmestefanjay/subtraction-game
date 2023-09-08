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

### How to play
- start the application by either
  - run `java -jar build/libs/nim-game-1.0.0.jar` or
  - use `./gradlew bootRun` or
  - use via the IDEA in the class `NimGameApplication.class`
- Open a REST client and set the host to `localhost:8089/nim-game/api/v1`
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
  "sticks": 13
}
```

## Parameters
- you can configure the game two ways:
  - enabling expert mode by setting `expert.mode=true` in the application.properties or `-Dexpert.mode=true`
  - setting the initial amount of sticks by setting `stick.amount=<random number>` in the application.properties 
  or `-Dstick.amount=<random number>`
  - example: `java -Dexpert.mode=true -Dstick.amount=100 -jar build/libs/nim-game-1.0.0.jar`
  - Make sure to add them BEFORE the -jar option

## modes
- The _standard mode_ mplements a random amount of sticks to be drawn by the computer
- The _expert mode_ is just a simple implementation of Bouton's strategy where the winning strategy is to remove
sticks = `stake % (maxDraw + 1)` or sticks = `maxDraw` if the result would be 0.