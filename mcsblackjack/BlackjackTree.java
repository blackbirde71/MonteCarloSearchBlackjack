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
		Random r = new Random();
        
        // biasing the algorithm so it explores the stand option more often
        if (r.nextInt(2) == 0) {
        	ArrayList<Integer> currentCards = new ArrayList<Integer>(state.cards);
        	return new BlackjackState(currentCards, true);
        } else {
        	int randNum;
    
            // selects a random card to simulate
            while (true) {
            	int num = r.nextInt(52);
    
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
    }

    public Node findMax(){
        // System.out.println("findmax:");
        double ucb, maxUcb;
        ucb = 0.0;
        maxUcb = 0.0;
        Node maxNode = current.children.get(0);

        // always explore the Stand option

        // if (current.count < 2) {
        //     System.out.println("STAND");
        //     return current.children.get(52);
        // }

        // for the first 52 iterations, this is basically going from 0 to 51
        // we made changes to the classic monte carlo model because we don't it 
        // to go sequentially and thus bias it to the first few cards, so
        // we select randomly
        // 52 cards - # in hand - 1 dealer card + 1 stand + 1 rollout directly from the node
        // ^ this is very important - overflow otherwise
        if (current.count < (52 - current.state.numCards - 1 + 1 + 1)) {
            Random r = new Random();
            int index = r.nextInt(52);
            while (true) {
            	// System.out.println(index);
                Node c = current.children.get(index % 52);
                if (c != null && c.count == 0) {
                    maxNode = c;
                    break;
                }
                index++;
            }
        }
        // if all the children have been explored at least once, then select by ucb 
        else {
            // skip the stand option
            for(int i=0; i<52; i++) {
            	Node n = current.children.get(i);
	            // null check - to not explore the cards that are already in the game
	            if (n != null) {
	                ucb = n.reward / n.count + EXPLORATION * Math.sqrt(Math.log(n.parent.count)/n.count); //> CATCH COUNT COUNT == 0 AND N== 0
	                if (ucb > maxUcb) {
	                    maxNode = n;
	                    maxUcb = ucb;
	                }
	            }
	        }
        }
        return maxNode;
    }

    //make sure that you cannot draw over your fifth card

    public double calcReward(BlackjackState state) {
    	double score = state.score;
    	if (score > 21) return 0.0;
    	if (state.numCards == 5) return 1.0;
    	// System.out.print("calcReward score: ");
    	// System.out.println(score / 21);
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

    public int chooseMove() {
        double totalHitScore = 0.0;

        // should it be sum of reward/count
        // or (sum of rewards) / count
        // System.out.println(gameNode.children);
        // System.out.println(123);
        for(int i=0; i<52; i++){
        	Node n = gameNode.children.get(i);
        	// null check against skipped cards
            if (n != null) {
            	// System.out.print(" | reward: ");
    			// System.out.println(n.reward / n.count);
            	totalHitScore += n.reward / n.count;
            }
        }
        totalHitScore = totalHitScore / 52;

        double standScore = gameNode.children.get(52).reward / gameNode.children.get(52).count;
        System.out.print("Hit score: ");
        System.out.println(totalHitScore);
        System.out.print("Stand score: ");
        System.out.println(standScore);

        if (totalHitScore > standScore) {
        	System.out.println("HIT");
        	return -1;
        } else {
        	System.out.println("STAND");
        	return gameNode.state.score;
        }
    }

    public void printNodes(Node n) {
    	System.out.println("root count");
		System.out.println(n.count);
		System.out.println("root reward");
    	System.out.println(n.reward);
    	for (Node a : n.children) {
    		if (a != null) {
    			// System.out.print(a.children.size());
    			System.out.print(" count: ");
    			System.out.print(a.count);
    			System.out.print(" | reward: ");
    			System.out.println(a.reward / a.count);
    		}
    	}
    	// System.out.println(n.children.get(51).state.score);
    }

    public int play() {
    	for (int i=0; i<500; i++) {
    		// System.out.println("new select");
    		select(gameNode);
    	}
    	Node n = root;
    	printNodes(n.children.get(30));
    	return chooseMove();

    	// System.out.println(current.reward);
    	// return 1;
    }
}
