# MonteCarloSearchBlackjack
A reinforcement learning Blackjack computer algorithm implemented with Monte Carlo tree search. The game allows the player to play alongside the computer simultaneously in single-game segments against the dealer. The player and computer may choose to hit or stand in each round. The dealer always hits at a card total of 16 and below, per the standard rules.
## Implementation
This project implements an abstract-implementing class structure, with MonteCarloTree serving as the abstract class. This class contains most of the methods essential to Monte Carlo tree search in general. Our extending class, BlackjackTree implements the abstract methods from the abstract class to apply the Monte Carlo tree search algorithm to Blackjack. The BlackjackState class manages specific information about each possible state of the game, which is represented in each node of the tree. Finally, the Blackjack class runs the game and curates the actions of all the playing agents.
## Monte Carlo Tree Search
Monte Carlo tree search is an algorithm that combines reinforcement learning with search trees to develop a structure comprising of all the choices an agent can make in the game and their respective estimated payoffs. Monte Carlo tree search crucially invokes Monte Carlo methods of simulation, which involves randomizing selections to efficiently model the game where complete simulations would be too resource intensive. The algorithm invovles two main steps, selection, and updating. The first step involves picking a state node to simulate from, using an upper confidence bound algorithm to prioritize states with higher yields and less visits. This node is then potentially expanded to all its available child states depending on the number of visits to that node. The second main step in the process is updating the tree, where the computer first simulates a random sequences of moves from the selected node to a game end, and then a reward is calculated and used to update the weights on the successive parents of the node leading up to the node currently in play. These two actions are termed "rollout" and "backpropagation," respectively.
## Blackjack
In Blackjack, all players (in this case the user and the computer referencing the Monte Carlo tree) play against the dealer. All agents are initially dealt 2 cards, and must choose to "hit" or "stand" each round of the game. When hitting, they receive another card from the deck. The agent seeks to bring their card total as close to 21 as possible without going over, in which case they lose. When they are satisfied with their current total, the agent "stands" to stop receiving cards for the rest of the game. In this game, face cards are worth 10, aces are worth the more useful option out of 1 or 11, and all other cards are worth their rank.
## Possible improvements:
	- Fix constants redundancy
	- Change EXPLORATION to float
	- Consider making constants final variables
	- Consider timing play() to player's decision time rather than hardcoding number of simulations
	- Make MonteCarloTree more abstract
	- Implement 5 card win in Blackjack
## How to run:
```
javac -d bin mcsblackjack/*.java
java -cp bin mcsblackjack.Blackjack
```
