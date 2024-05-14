/*
* Extends MonteCarloSearchTree abstract class (will implement abstract methods for game specifics, like recognizing a terminal (game over) state)
*/
package mcsblackjack;
import java.util.*; 

public class BlackjackTree extends MonteCarloTree<BlackjackState> {

	// the first card of the dealer is visible, so it should be
	// account for in simulations
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


        // reward is weighted, i.e. the average of all rollouts
        newReward /= NUMITERATIONS;

        double oldReward = current.reward;

        // type of backprop (enum)
        BackPropType bct;

        // update() is only called from leaf nodes
        // so it does not deal with mixed sources of reward: standing or hitting
        // so no need to call max()
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
			// a flag that helps to skip through the cards that you know are in the game
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

			// if the card is not to be skipped, then add it as child
			// i.e. it is possible to draw it from the current hand
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

        // biasing the random walk so it explores the stand option more often
        // 50% of the time it will opt for standing
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
    			
    			// if the card is to be skipped, do not simulate the random walk on it
    			// to not waste time
    			if (shouldSkip) {
    				continue;
    			} else {
    				randNum = num;
    				break;
    			}
            }
    		
    		// get current hand
            ArrayList<Integer> currentCards = new ArrayList<Integer>(state.cards);

            // randNum == 52 is STANDING
            if (randNum != 52) {
          		currentCards.add(randNum);
            	return new BlackjackState(currentCards, false);
            } else {
            	return new BlackjackState(currentCards, true);
            }
        }
    }

    // a function that helps to find the greatest of two numbers
	public double max(double a, double b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    // "feeding" the updates in a reward of a leaf node back to its parents
    public void backpropagate(double addedReward, BackPropType backPropType) {
        BackPropType bct = backPropType;
        while (true) {
        	// update the parent's rewards if the current node has a parent, i.e. non-root
            if (!current.equals(root)) {
                if (bct == BackPropType.STAND) {
                    current.parent.rewardStand += addedReward;
                    // STAND are terminal - no children
                    // so the parents of STAND nodes are by definition HITs
                    // so update the BackPropType for the next iteration
                    bct = BackPropType.HIT;
                } else {
                    current.parent.rewardHit+= addedReward;
                }
                if (current.parent.count == 0) {
                    System.out.println("ISSUE WITH count = 0");
                }
                // count is updated later, so count + 1 here
                current.parent.reward = (current.parent.count + 1) * max(current.parent.rewardHit / current.parent.count, current.parent.rewardStand);
            }
            // each update is +1 to the count
            current.count++;

            // breaks when you reach the current game state
            // no need to backpropagate to the root - earlier nodes will never be used
            if (current.equals(gameNode)) {
                break;
            }
            current = current.parent;
        }
    }

    public Node findMax(){

    	// UCB is Upper Confidence Boundary
        double ucb, maxUcb;
        ucb = 0.0;
        maxUcb = 0.0;
        Node maxNode = current.children.get(0);

        // for the first 52 iterations, this is basically going from 0 to 51
        // we made changes to the classic monte carlo model because we don't want it 
        // to go sequentially and thus bias it to the first few cards, so we select randomly
        // limit: 52 cards in total - # of cards in the hand - 1 dealer card + 1 from stand + 1 from rollout directly from the node
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

        // if all the children have been explored at least once, then select by UCB
        else {
            // skip the stand option
            for(int i=0; i<52; i++) {
            	Node n = current.children.get(i);
	            if (n != null) {
	            	// balancing exploitation (first term) and exploration (second term)
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

    // normalizing the scores to the interval [0,1]
    public double calcReward(BlackjackState state) {
    	double score = state.score;
    	return score / 21; 
    }

    // determines if the node is terminal
    public boolean isEnd(BlackjackState state) {
    	return state.isEnd;
    }

    // 0-51 for cards, 52 for standing
    public void updateGameState(int newCard) {
    	gameNode = gameNode.children.get(newCard);
    }

    // after the simulation for the current turn is done, choose the most promising option
    // by comparing the expected value of STANDING and HITTING
    public String chooseMove() {
        double totalHitScore = 0.0;

        // E(Hitting) = average of the weighted rewards from all the cards
        // that can be dealt, excluding the dealer's cards and your current hand
        for(Node n : gameNode.children) {
            if (n != null) {
            	totalHitScore += n.reward / n.count;
            }
        }
        // risk factor of 1.1
        totalHitScore = 1.1 * totalHitScore / gameNode.children.size();

        double standScore;

        // some gameNode's have no possible children
        // e.g. busting
        // so the reward is calculated from the only possible option,
        // standing, which has the index of 52
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
    	// run a thousand simulations
    	for (int i=0; i<1000; i++) {
    		select(gameNode);
    	}

    	// choose the best move based on the simulations above
    	return chooseMove();
    }
}
