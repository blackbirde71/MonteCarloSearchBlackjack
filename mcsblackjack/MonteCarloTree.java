/**
* Abstract class for Monte Carlo Tree
*
* Inner class "Node"
*   Needs to store:
*       - State of the table
*       - Reward - r_i - Cumulative rewards of associated leaf nodes
*       - Number of states - n_i - Number of associated leaf nodes
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
        current = new Node(initialState);
        gameNode = new Node(initialState);
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

    abstract String chooseMove();

    public void select() {
        //System.out.println("selection reached:");
        current = gameNode;
        if(current.isChildless){
            if (current.count==0) {
                //System.out.println("count==0");
                update();
            } else {
                //System.out.println("count!=0");
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

                // null check - to not explore the cards that are already in the game
                Node newCurrent;
                while (true) {
                    // sometimes children is null
                    newCurrent = current.children.get(new Random().nextInt(current.children.size()));
                    if (newCurrent != null) break;
                }
                current = newCurrent;
                update();
            }
        }
        else{
            current = findMax();
        }
    } 

    public void update(){
        for(int i=0; i<NUMITERATIONS; i++){
            double newReward = rollout(current.state);
            current.reward += newReward;
            // System.out.print("rollout result: ");
            // System.out.println(newReward);
        }
        backpropagate();
    }

    public double rollout(State simState){
        if(isEnd(simState)){
            // System.out.print("calc reward reached: ");
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
    public Node findMax(){
        double ucb, maxUcb;
        ucb = 0.0;
        maxUcb = 0.0;
        Node maxNode = current.children.get(0);

        // always explore the Stand option
        if (current.count < 1) {
            return current.children.get(52);
        }

        for(Node n : current.children){
            // null check - to not explore the cards that are already in the game
            if (n != null) {
                if (n.count < 1) {
                    maxNode = n;
                    break;
                } else {
                    ucb = n.reward / n.count + EXPLORATION * Math.sqrt(Math.log(n.parent.count)/n.count); //> CATCH COUNT COUNT == 0 AND N== 0
                    if(ucb > maxUcb){
                        maxNode = n;
                        maxUcb = ucb;
                    }
                }
            }
        }
        return maxNode;
    }
    
    // abstract void resetCurrent(); // GO TO CURRENT STATE IN GAME
    abstract State getRandomMove(State state);
    abstract boolean isEnd(State state);
    abstract State[] findMoves(State state);
    abstract double calcReward(State state);
    // public static void main(String[] args){
    // }
}