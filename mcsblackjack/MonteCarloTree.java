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
	public Node root;
    public Node current;
    public int n;
	public class Node{
        public ArrayList<Node> children;
        public Node parent;
        public boolean isTerminal;
        public long count;
        public long reward;
        public Long state;
        public Node(Node state){
            this.children = new ArrayList<String>;
            this.isTerminal = true;
            this.state = state;
            this.count = 0;
            this.reward = 0;
        }
    }
    public MonteCarloTree(){
    	root = new Node();
        current = root;
        n = 0;
    }
    public simulate(){
        select();
        update();
        n++;
    }
    public void select(){
        resetCurrent();
        if(current.isTerminal){
            if(current.count==0){
                rollout();
            }
            else{
                ArrayList<Long> availMoves = findMoves(current.state);
                for(Long l : availMoves){
                    current.children.add(new Node(l));
                    current.isTerminal = false;
                }
                current = availMoves.get(0);
                rollout();
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
    public long rollout(Long simState){
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
            double ucb = // ! insert formula ;
            if(ucb > maxUcb){
                maxNode = n;
                maxUcb = ucb;
            }
        }
        return maxNode;
    }
    public Node makeState(Long l){
        return new Node();
    }
    public static Long getRandomMove(Node currentState) {
        ArrayList<Long> availMoves = findMoves(currentState);
        return availMoves.get(new Random().nextInt(availMoves.size()));
    }
    abstract void resetCurrent();
    abstract ArrayList<Long> findMoves(Long currentState);
    abstract boolean isGameOver(Long currentState);
    abstract long calcReward(Long currentState);
    public static void main(String[] args){
    }
}