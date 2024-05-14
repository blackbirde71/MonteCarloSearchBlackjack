/**
* Abstract class for Monte Carlo Tree
*
* Inner class "Node"
*	Needs to store:
*		- State of the table
*		- Reward - r_i - Cumulative rewards of associated leaf nodes
*		- Number of states - n_i - Number of associated leaf nodes
*/
package mcsblackjack;
import java.util.*; 
public abstract class MonteCarloTree<State>{
    public final float EXPLORATION;
    public final int NUMITERATIONS;
	public Node root;
    public Node current;
    public Node gameNode;

    enum BackPropType {
        STAND,
        HIT,
    }

	public class Node{
        public ArrayList<Node> children;
        public Node parent;
        public boolean isChildless;
        public State state;
        public int action;

        public long count;
        public double reward;
        public double rewardHit;
        public double rewardStand;


        public boolean equals(Node a) {
            return this.state.equals(a.state);
        }

        public Node(State state) {
            this.children = new ArrayList<Node>();
            this.isChildless = true;
            this.state = state;
            this.count = 0;
            this.reward = 0;
            this.rewardHit = 0;
            this.rewardStand = 0;
        }
    }

    MonteCarloTree(State initialState, int EXPLORATION, int NUMITERATIONS){
        this.NUMITERATIONS = NUMITERATIONS;
        this.EXPLORATION = EXPLORATION;
    	root = new Node(initialState);
        current = root;
        gameNode = root;
        // System.out.println(root.equals(current));
        // System.out.println(root.equals(gameNode));
        // System.out.println(current.equals(gameNode));

    }

    // public void trackMove(State newState){
    //     for(Node n : gameNode.children){
    //         if(n.state.equals(newState)){
    //             gameNode = n;
    //         }
    //     }
    // }

    abstract int chooseMove();

    public void select(Node n) {
        // System.out.println("selection reached:");
        current = n;
        if (isEnd(current.state)) {
            update();
        }
        else { 
            if(current.isChildless){
               if (current.count==0) {
                   // System.out.println("count==0");
                   update();
               } else {
                   // System.out.println("count!=0");
                   State[] availMoves = findMoves(current.state);
                   for(State s : availMoves){
                       Node newNode;
                       if (s != null) {
                           newNode = new Node(s);
                           newNode.parent = current;
                       } else {
                           newNode = null;
                       }
                       current.children.add(newNode); // CONNECT PARENT POINTERS
                   }
                   current.isChildless = false;
   
                   // // null check - to not explore the cards that are already in the game
                   // Node newCurrent;
                   // while (true) {
                   //     // sometimes children is null
                   //     newCurrent = current.children.get(new Random().nextInt(current.children.size()));
                   //     if (newCurrent != null) break;
                   // }
   
                   // always explore the stand option first
                   current = current.children.get(52);
                   update();
                }
            } else {
                   current = findMax();
                   select(current);
                   // update();
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

    abstract void update();

    public double rollout(State simState){
        if(isEnd(simState)){
            return calcReward(simState);
        }
        simState = getRandomMove(simState);
        return rollout(simState);
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

    // make it truly abstract!!
    abstract Node findMax();
    
    // abstract void resetCurrent(); // GO TO CURRENT STATE IN GAME
    abstract State getRandomMove(State state);
    abstract boolean isEnd(State state);
    abstract State[] findMoves(State state);
    abstract double calcReward(State state);
    // public static void main(String[] args){
    // }
}