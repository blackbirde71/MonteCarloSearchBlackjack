/**
* Abstract class for Monte Carlo Tree
*
* Inner class "Node"
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

    }

    abstract String chooseMove();

    public void select(Node n) {
        current = n;
        if (isEnd(current.state)) {
            update();
        }
        else { 
            if(current.isChildless){
               if (current.count==0) {
                   update();
               } else {
                   State[] availMoves = findMoves(current.state);
                   for(State s : availMoves){
                       Node newNode;
                       if (s != null) {
                           newNode = new Node(s);
                           newNode.parent = current;
                       } else {
                           newNode = null;
                       }
                       current.children.add(newNode); 
                   }
                   current.isChildless = false;
   
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

    abstract void update();

    public double rollout(State simState){
        if(isEnd(simState)){
            return calcReward(simState);
        }
        simState = getRandomMove(simState);
        return rollout(simState);
    }

    abstract void backpropagate(double addedReward, BackPropType backPropType);

    abstract Node findMax();
    
    abstract State getRandomMove(State state);
    abstract boolean isEnd(State state);
    abstract State[] findMoves(State state);
    abstract double calcReward(State state);
}