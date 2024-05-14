# MonteCarloSearchBlackjack
A reinforcement learning Blackjack computer algorithm implemented with Monte Carlo tree search. The game allows the player to play alongside the computer simultaneously in single-game segments against the dealer. The player and computer may choose to hit or stand in each round. The dealer always hits at a card total of 16 and below, per the standard rules.
## Implementation
This project implements an abstract-implmenting class structure, with MonteCarloTree serving as the abstract class. This class contains most of the methods essential to Monte Carlo tree search in general. Our extending class, BlackjackTree implements the abstract methods from the abstract class to apply the Monte Carlo tree search algorithm to Blackjack. The BlackjackState class manages specific information about each possible state of the game, which is represented in each node of the tree. Finally, the Blackjack class runs the game and curates the actions of all the playing agents.
## Future tasks
	- Fix constants redundancy
	- Change EXPLORATION to float
	- Consider making constants final variables
	- Consider converting pHand and dHand to Arraylists
	- Change play() in BlackjackTree to take a parameter denoting number of times to select
	- Consider timing play() to player's decision time rather than hardcoding number of simulations
	- Make MonteCarloTree abstract
	- Move calculations for finding number of cards in CLI to a function
	- Implement 5 card win in Blackjack
