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

	public class Node{
        public ArrayList<Node> children;
        public Node parent;
        public boolean isChildless;
        public long count;
        public double reward;
        public State state;
        public int action;

        public boolean equals(Node a) {
            return this.state.equals(a.state);
        }

        public Node(State state){
            this.children = new ArrayList<Node>();
            this.isChildless = true;
            this.state = state;
            this.count = 0;
            this.reward = 0;
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
        }
        else{
            current = findMax();
            select(current);
            // update();
        }
    } 

    public void update(){
        for(int i=0; i<NUMITERATIONS; i++){
            double newReward = rollout(current.state) / NUMITERATIONS;
            // System.out.print("rollout result: ");
            // System.out.println(newReward);
            current.reward += newReward;
        }
        // System.out.print("current's reward ");
        // System.out.println(current.reward);
        backpropagate();
    }

    public double rollout(State simState){
        if(isEnd(simState)){
            // System.out.print("rollout result: ");
            // System.out.println(calcReward(simState));
            return calcReward(simState);
        }
        simState = getRandomMove(simState);
        // System.out.println("rollout cont'd");
        return rollout(simState);
    }

    public void backpropagate(){
        while (true) { //! current.equals(gameNode.parent)//! STARTS AT TOP // current.parent != null
            // System.out.println("backprop reached");
            // System.out.println(current.state == null);
            if (!current.equals(root)) {
                // System.out.println("not equals root");
                current.parent.reward += current.reward;
            }
            current.count++;
            if (current.equals(gameNode)) {
                // System.out.println("so:");
                // System.out.println(current.equals(gameNode));
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