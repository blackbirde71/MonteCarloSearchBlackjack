/*
* Extends MonteCarloSearchTree abstract class (will implement abstract methods for game specifics, like recognizing a terminal (game over) state)
*/
package mcsblackjack;
import java.util.*; 

public class BlackjackTree extends MonteCarloTree<BlackjackState> {
	// public Node root;
    // public Node current;
    // public Node gameNode;

	public int dealerCard;

	BlackjackTree(int dealerCard, int[] initCards, int EXPLORATION, int NUMITERATIONS) {
		super(new BlackjackState(initCards, false), EXPLORATION, NUMITERATIONS);
		this.dealerCard = dealerCard;
	}

	public BlackjackState[] findMoves(BlackjackState state) {
		BlackjackState[] b = new BlackjackState[53];
		// 53rd element is standing
		b[52] = new BlackjackState(state.cards, true);

		for (int i=0; i<52; i++) {
			// skip through the cards that you know are in the game
			boolean shouldSkip = false;

			// comparing to the state's hand
			for (int j=0; j<state.numCards; j++) {
				shouldSkip = false;
				if (i == state.cards[j]) {
					shouldSkip = true;
					break;
				}
			}
			// comparing to the dealer's card
			if (i == dealerCard) {
				shouldSkip = true;
			}


			if (!shouldSkip) {
				int[] stateCards = state.cards;
				stateCards[state.numCards] = i;
				b[i] = new BlackjackState(stateCards, false);
			}
		}

		return b;
	}

	public BlackjackState getRandomMove(BlackjackState state) {
        int randNum;

        while (true) {
        	int num = new Random().nextInt(53);

        	// skip through the cards that you know are in the game
        	boolean shouldSkip = false;

			// comparing to the state's hand
			for (int j=0; j<state.numCards; j++) {
				shouldSkip = false;
				if (num == state.cards[j]) {
					shouldSkip = true;
					break;
				}
			}
			// comparing to the dealer's card
			if (num == dealerCard) {
				shouldSkip = true;
			}

			if (shouldSkip) {
				continue;
			} else {
				randNum = num;
				break;
			}
        }
        int[] currentCards = state.cards;
        if (randNum != 52) {
      		currentCards[state.numCards] = randNum;
        	return new BlackjackState(currentCards, false);
        } else {
        	return new BlackjackState(currentCards, true);
        }
    }

    //make sure that you cannot draw over your fifth card

    public double calcReward(BlackjackState state) {
    	int score = state.score;
    	if (score > 21) return 0.0;
    	if (state.cards.length == 5) return 1.0;
    	return score / 21; 
    }

    public boolean isEnd(BlackjackState state) {
    	return state.isEnd;
    }

    public static void main(String[] args){
    	System.out.println(11);
    }

    // 0-51 for cards, 52 for standing
    public void updateGameState(int newCard) {
    	gameNode = gameNode.children.get(newCard);
    }

    public int chooseMove() {
        double totalHitScore = 0.0;

        // should it be sum of reward/count
        // or (sum of rewards) / count
        for(int i=0; i<52; i++){
            Node n = gameNode.children.get(i);
            totalHitScore += n.reward / n.count;
        }
        totalHitScore = totalHitScore / 52;

        double standScore = gameNode.children.get(52).reward;

        if (totalHitScore > standScore) {
        	return -1;
        } else {
        	return gameNode.state.score;
        }
    }

    public int play() {
    	for (int i=0; i<10; i++) {
    		select();
    	}
    	return chooseMove();
    }
}