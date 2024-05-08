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
    public int n = 0;
	public class Node{
        public ArrayList<Node> children;
        public Node parent;
        public boolean isTerminal;
        public long count;
        public long reward;
        public Long state;
        public int action;
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
    }
    public void trackMove(Long newState){
        for(Node n : gameState.children){
            if(n.state == newState){
                gameState = n;
            }
        }
    }
    public Long chooseMove(){
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
            ucb = n.reward / n.count + EXPLORATION * Math.sqrt(Math.log(n)/count);
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