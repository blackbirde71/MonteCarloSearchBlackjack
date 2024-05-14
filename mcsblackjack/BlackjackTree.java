/*
* Extends MonteCarloSearchTree abstract class (will implement abstract methods for game specifics, like recognizing a terminal (game over) state)
*/
package mcsblackjack;
import java.util.*; 

public class BlackjackTree extends MonteCarloTree<BlackjackState> {

	public int dealerCard;

	BlackjackTree(int dealerCard, ArrayList<Integer> initCards, int EXPLORATION, int NUMITERATIONS) {
		super(new BlackjackState(initCards, false), EXPLORATION, NUMITERATIONS);
		this.dealerCard = dealerCard;
	}

	public void update() {
        double newReward = 0;

        for(int i=0; i<NUMITERATIONS; i++){
            newReward += rollout(current.state);
        }

        newReward /= NUMITERATIONS;

        double oldReward = current.reward;

        //type of backprop:
        BackPropType bct;

        if (current.state.isStanding) {
            current.rewardStand += newReward;
            current.reward += newReward;
            bct = BackPropType.STAND;
        } else {
            current.rewardHit += newReward;
            current.reward += newReward;
            bct = BackPropType.HIT;
        }

        // difference between the old and new reward
        // that needs to be backpropagated
        double diff = current.reward - oldReward;        

        backpropagate(diff, bct);
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
        if (r.nextInt(2) > 0) {
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

	public double max(double a, double b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    public void backpropagate(double addedReward, BackPropType backPropType) {
        BackPropType bct = backPropType;
        while (true) {
            if (!current.equals(root)) {
                if (bct == BackPropType.STAND) {
                    current.parent.rewardStand += addedReward;
                    // STAND only applies the first time
                    bct = BackPropType.HIT;
                } else {
                    current.parent.rewardHit+= addedReward;
                }
                if (current.parent.count == 0) {
                    System.out.println("ISSUE WITH count = 0");
                }
                // count is not updated yet
                current.parent.reward = (current.parent.count + 1) * max(current.parent.rewardHit / current.parent.count, current.parent.rewardStand);
            }
            current.count++;
            if (current.equals(gameNode)) {
                break;
            }
            current = current.parent;
        }
    }

    public Node findMax(){

        double ucb, maxUcb;
        ucb = 0.0;
        maxUcb = 0.0;
        Node maxNode = current.children.get(0);

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

    public double calcReward(BlackjackState state) {
    	double score = state.score;
    	return score / 21; 
    }

    public boolean isEnd(BlackjackState state) {
    	return state.isEnd;
    }

    public static void main(String[] args){
    }

    // 0-51 for cards, 52 for standing
    public void updateGameState(int newCard) {
    	gameNode = gameNode.children.get(newCard);
    }

    public String chooseMove() {
        double totalHitScore = 0.0;

        for(Node n : gameNode.children) {
            if (n != null) {
            	totalHitScore += n.reward / n.count;
            }
        }
        totalHitScore = 1.2 * totalHitScore / 52;

        double standScore;

        // in other words, if the node's score is equal to 21:
        if (gameNode.children.size() != 0) {
        	standScore = gameNode.children.get(52).reward / gameNode.children.get(52).count;
        } else {
        	standScore = gameNode.reward;
        }

        if (totalHitScore > standScore) {
        	return "HIT";
        } else {
        	return "STAND";
        }
    }

    public String play() {
    	for (int i=0; i<1000; i++) {
    		select(gameNode);
    	}

    	return chooseMove();
    }
}
