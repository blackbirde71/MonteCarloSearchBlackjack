/**
* Abstract class for Monte Carlo Tree
*
* Inner class "Node"
*/
package mcsblackjack;
import java.util.*; 
public abstract class MonteCarloTree<State>{
    // the higher, the more likely it is to explore different nodes
    // the lower, the more likely it is to double down on most promising paths
    public final float EXPLORATION;
    // number of rollouts
    public final int NUMITERATIONS;
	public Node root;
    // the currently simulated node;
    public Node current;
    // where the game is at:
    public Node gameNode;

    // backpropagation from either a hitting state or standing state
    enum BackPropType {
        STAND,
        HIT,
    }

	public class Node{
        // all the possible futures from the current node
        public ArrayList<Node> children;
        public Node parent;
        public boolean isChildless;
        // computer's hand
        public State state;

        // how many times was the node simulated, i.e. how many times update() was called
        public long count;
        // total reward = max(rewardHit, rewardStand)
        public double reward;
        // reward that came from simulating hits
        public double rewardHit;
        // reward that came from simulating stands
        public double rewardStand;

        // custom comparison
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

    public void select(Node n) {
        current = n;
        // if terminal, update right away
        if (isEnd(current.state)) {
            update();
        }
        else {
            if(current.isChildless){
                // if no children AND never visited, update right away
               if (current.count==0) {
                   update();
               } else {
                // if no children but visited once, then add children
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
                // if it has children, then find the child that maximizes the upper confidence bound
               current = findMax();
               select(current);
            }
        }
    }

    // complete a single random walk and return the reward once reaching a terminal node
    public double rollout(State simState){
        if(isEnd(simState)){
            return calcReward(simState);
        }
        simState = getRandomMove(simState);
        return rollout(simState);
    }

    // decide the next step in the random walk
    abstract State getRandomMove(State state);

    // perform rollouts NUMITERATIONS amounts of time
    // then backpropagate the rewards
    abstract void update();

    // feeds the updated rewards back to the node's parent chain
    abstract void backpropagate(double addedReward, BackPropType backPropType);

    // finds the child that is most promising through upper confidence bound
    abstract Node findMax();

    // given the rewards from the gameNode's children, decide what 
    // course of actions maximizes the chances of winning
    abstract String chooseMove();

    // determine if the node is terminal
    abstract boolean isEnd(State state);

    // find all the available futures from the given state
    abstract State[] findMoves(State state);

    // calculate a reward from a terminal state
    abstract double calcReward(State state);
}