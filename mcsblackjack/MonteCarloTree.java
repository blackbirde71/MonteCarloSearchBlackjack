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
public abstract class MonteCarloTree{
    public final float EXPLORATION = 2;
	public Node root;
    public Node current;
    public Node gameState;
	public class Node{
        public ArrayList<Node> children;
        public Node parent;
        public boolean isTerminal;
        public long count;
        public long reward;
        public State state;
        public int action;
        public Node(State state){
            this.children = new ArrayList<String>;
            this.isTerminal = true;
            this.state = state;
            this.count = 0;
            this.reward = 0;
        }
    }
    public MonteCarloTree(State initialState){
    	root = new Node(initialState);
        current = root;
    }
    public void trackMove(State newState){
        for(Node n : gameState.children){
            if(n.state.equals(newState)){
                gameState = n;
            }
        }
    }
    public State chooseMove(){
        double score, maxScore;
        Node maxNode;
        for(Node n : gameState.children){
            score = n.reward / n.count;
            if(score > maxScore){
                maxNode = n;
                maxScore = score;
            }
        }
        return maxNode.state;
    }
    public void select(){
        resetCurrent();
        if(current.isTerminal){
            if(current.count==0){
                update();
            }
            else{
                ArrayList<State> availMoves = findMoves(current.state);
                for(State s : availMoves){
                    current.children.add(new Node(s));
                    current.isTerminal = false;
                }
                current = availMoves.get(0); // CHANGE TO RANDOM
                update();
            }
        }
        else{
            current = findMax();
        }
    }   
    public void update(){
        current.reward = rollout(current.state);
        backpropagate();
    }
    public long rollout(State simState){
        if(simState.isGameOver){
            return simState.calcReward;
        }
        simState = getRandomMove(simState);
        return rollout(simState);
    }
    public void backpropagate(){
        while(! current.parent == null){
                current.parent.reward += current.reward;
                current.parent.count++;
                current = current.parent;
        }
    }
    public Node findMax(){
        double ucb, maxUcb;
        Node maxNode;
        for(Node n : current.children){
            ucb = n.reward / n.count + EXPLORATION * Math.sqrt(Math.log(n.parent.count)/n.count); //> CATCH COUNT COUNT == 0 AND N== 0
            if(ucb > maxUcb){
                maxNode = n;
                maxUcb = ucb;
            }
        }
        return maxNode;
    }
    public Node makeState(Long l){ // IGNORE
        return new Node();
    }
    public static STATE getRandomMove(STATE currentState) {
        ArrayList<STATE> availMoves = findMoves(currentState);
        return availMoves.get(new Random().nextInt(availMoves.size()));
    }
    abstract void resetCurrent(); // GO TO CURRENT STATE IN GAME
    abstract ArrayList<STATE> findMoves(STATE currentState);
    abstract boolean isGameOver(State currentState);
    abstract long calcReward(State currentState);
    public static void main(String[] args){
    }
}