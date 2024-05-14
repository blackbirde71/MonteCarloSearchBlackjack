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

	BlackjackTree(int dealerCard, ArrayList<Integer> initCards, int EXPLORATION, int NUMITERATIONS) {
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
				if (i == state.cards.get(j)) {
					shouldSkip = true;
					break;
				}
			}
			// comparing to the dealer's card
			if (i == dealerCard) {
				shouldSkip = true;
			}


			if (!shouldSkip) {
				ArrayList<Integer> stateCards = new ArrayList<Integer>(state.cards);
				stateCards.add(i);
				b[i] = new BlackjackState(stateCards, false);
			}
		}

		return b;
	}

	public BlackjackState getRandomMove(BlackjackState state) {
        int randNum;

        // selects a random card to simulate
        while (true) {
        	int num = new Random().nextInt(53);

        	// skip through the cards that you know are in the game
        	boolean shouldSkip = false;

			// comparing to the state's hand
			for (int j=0; j<state.numCards; j++) {
				if (num == state.cards.get(j)) {
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

        ArrayList<Integer> currentCards = new ArrayList<Integer>(state.cards);
        if (randNum != 52) {
      		currentCards.add(randNum);
        	return new BlackjackState(currentCards, false);
        } else {
        	return new BlackjackState(currentCards, true);
        }
    }

    //make sure that you cannot draw over your fifth card

    public double calcReward(BlackjackState state) {
    	int score = state.score;
    	if (score > 21) return 0.0;
    	if (state.numCards == 5) return 1.0;
    	// System.out.print("calcReward score: ");
    	// System.out.println(state.score);
    	return score / 21; 
    }

    public boolean isEnd(BlackjackState state) {
    	return state.isEnd;
    }

    public static void main(String[] args){
    	// System.out.println(11);
    }

    // 0-51 for cards, 52 for standing
    public void updateGameState(int newCard) {
    	gameNode = gameNode.children.get(newCard);
    }

    public String chooseMove() {
        double totalHitScore = 0.0;

        // should it be sum of reward/count
        // or (sum of rewards) / count
        // System.out.println(gameNode.children);
        // System.out.println(123);
        for(int i=0; i<52; i++){
        	Node n = gameNode.children.get(i);
        	// null check against skipped cards
            if (n != null) {
            	totalHitScore += n.reward / n.count;
            }
        }
        totalHitScore = totalHitScore / 52;

        double standScore = gameNode.children.get(52).reward;

        if (totalHitScore > standScore) {
        	return "HIT";
        } else {
        	return "STAND";
        }
    }

    public void printNodes(Node n) {
    	System.out.println(current.children);
    	System.out.println(n.reward);
    	for (Node a : n.children) {
    		if (a != null) {
    			printNodes(a);
    		}
    	}
    }

    public int play() {
    	for (int i=0; i<2; i++) {
    		select();
    	}
    	// Node n = root;
    	// printNodes(n);
    	// return chooseMove();

    	// System.out.println(root.reward);
    	return 1;
    }
}