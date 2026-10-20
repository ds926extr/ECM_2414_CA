# ECM_2414_CA

## Overview

This project is a multi-threaded card game implemented in Java.

The game consists of `n` players and `n` decks.

Each player runs on a separate thread and begins with 4 cards.

During the game, each player:

- draws a card from their source deck
- chooses a card to discard
- discards the card into their destination deck
- checks whether they have a winning hand

A player wins when all four cards in their hand have the same value.

Once a player wins, all other players stop playing and the final player hands and deck states are written to output files.

## Project Structure

```text
src/
├── Card.java
├── CardDeck.java
├── Player.java
└── CardGame.java

cardsTest/
├── CardDeckTest.java
├── PlayerTest.java
└── CardGameTest.java
```

### Card

Represents an individual card.

Each card stores an integer value.

### CardDeck

Represents a thread-safe FIFO deck.

Cards are added to the end of the deck and removed from the front of the deck.

`CardDeck` also provides the ability to write its final contents to an output file.

### Player

Represents an individual player.

Each player has:

- a player number
- a four-card hand
- a source deck
- a destination deck
- access to the shared game-over state

Players prefer to keep cards which have the same value as their player number and discard cards which do not match their player number.

### CardGame

Contains the main program.

`CardGame` is responsible for:

- reading the number of players
- validating the input pack
- creating players and decks
- dealing cards
- starting the player threads
- waiting for the game to finish
- writing the final deck output files

## Input Pack

The program asks for:

- the number of players
- the filename of the pack

For `n` players, the pack must contain exactly `8 × n` cards.

Each line of the pack file must contain one non-negative integer.

Invalid pack files are rejected and the user is asked to enter another filename.

## Card Dealing

The first `4n` cards are dealt to the players using round-robin dealing.

The remaining `4n` cards are dealt to the decks using the same round-robin approach.

Therefore, each player starts with 4 cards and each deck starts with 4 cards.

## Game Flow

For player `i`:

```text
draw from deck i
        ↓
add card to hand
        ↓
select a card to discard
        ↓
discard to next deck
        ↓
check for winning hand
```

The final player discards into Deck 1, creating a circular arrangement of players and decks.

Access to the decks is synchronised to prevent unsafe concurrent modification.

## Winning

A player wins when they have four cards of the same value.

When a player wins, the shared game-over state is updated so that all other player threads stop.

## Output Files

The game produces one output file for each player and one output file for each deck.

For example, with two players:

```text
player1_output.txt
player2_output.txt
deck1_output.txt
deck2_output.txt
```

Player output records information such as:

```text
player 1 initial hand ...
player 1 draws a ... from deck 1
player 1 discards a ... to deck 2
player 1 current hand ...
player 1 wins
player 1 exits
player 1 final hand ...
```

Deck output records the final contents of the deck:

```text
deck1 contents: 2 4 7 1
```

A deck may contain a different number of cards at the end of the game because the players operate concurrently.

## Testing

JUnit 5 is used for testing.

Tests are divided into:

- `CardDeckTest`
- `PlayerTest`
- `CardGameTest`

The tests cover:

- FIFO deck behaviour
- empty and non-empty decks
- pack validation
- round-robin card dealing
- winning and non-winning hands
- drawing and discarding
- empty source deck handling
- stopping after another player wins
- output file creation
- output file contents

## Running the Program

Compile the Java source files and run the `CardGame` class.

The program will ask for the number of players and then a pack filename.

You must use `4` players and use `pack4.txt` for the pack filename.

## Requirements

- Java 17
- JUnit 5 for running tests
