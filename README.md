# Secret Hitler - Java Implementation

Welcome to the **Secret Hitler** Java implementation! This project is a simulation of the popular board game *Secret Hitler*, written in Java. The game is a hidden-role party game that involves players trying to pass policies to advance their team's agenda (Liberals vs. Fascists). The game contains aspects of diplomacy, deception, and voting.

## Table of Contents
- [Features](#features)
- [Game Flow](#game-flow)
- [Installation](#installation)
- [Game Structure](#game-structure)
- [Future Enhancements](#future-enhancements)
- [License](#license)

## Features

- **Role Assignment:** Players are randomly assigned secret roles as either Liberal, Fascist, or Hitler.
- **Presidential Elections:** Players vote to elect a President and Chancellor in each round.
- **Legislative Sessions:** The elected officials choose policies to enact (Liberal or Fascist).
- **Executive Actions:** The President may take executive actions, such as examining policies or choosing the next Presidential candidate, depending on the state of the game.
- **Game-End Conditions:** The game ends when either 5 Liberal policies are passed, 6 Fascist policies are passed, Hitler is elected Chancellor after 3 Fascist policies, or Hitler is killed.

## Game Flow

1. **Role Assignment**: The game starts by assigning secret roles to players. Each player will be either a Liberal, Fascist, or Hitler.
2. **Elections**: Players nominate a President and Chancellor, followed by a voting session to approve or reject the nominees.
3. **Legislative Session**: The elected President draws 3 policy cards, discards one, and hands the remaining two to the Chancellor, who then discards one and enacts the other.
4. **Executive Actions**: Depending on the state of the game, the President may get to use executive actions, such as examining the top policies in the deck or selecting the next Presidential candidate.
5. **Win Conditions**: The game ends when one of the following happens:
   - 5 Liberal policies are passed (Liberals win).
   - 6 Fascist policies are passed (Fascists win).
   - Hitler is elected Chancellor after 3 Fascist policies (Fascists win).
   - Hitler is assassinated (Liberals win).

## Installation

To run the project locally:

1. **Clone the repository:**
   
```bash
   git clone https://github.com/Jsalvar124/secret-hitler-cli.git
```
2. **Compile and run the project:**
   You can use any IDE that supports Java, such as IntelliJ IDEA or Eclipse.

   Alternatively, you can compile and run it from the command line:
```bash
   cd secret-hitler-cli
   javac Main.java
   java Main
```
## Game Structure

- **Player Class**: Represents a player in the game, including their role (Liberal, Fascist, or Hitler), voting status, and whether they are alive.
- **GameBoard Class**: Manages the state of the game, including the number of enacted policies, the policy deck, and discarded policies.
- **Executive Actions**: Special actions the President can take when certain conditions are met in the game, such as examining policies or selecting the next presidential candidate.
- **Game Logic**: The game tracks elections, policies, and win conditions. It includes methods for reshuffling the deck, managing legislative sessions, and verifying win conditions.


## Future Enhancements

- **GUI Interface**: Add a graphical interface to make the game more user-friendly.
- **Multiplayer Support**: Implement networking to support online multiplayer games using web sockets for real-time interaction.

## License

This project is licensed under the MIT License. 
