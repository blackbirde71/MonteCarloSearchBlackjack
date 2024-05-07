/**
* Abstract class for Monte Carlo Search Tree
*
* Inner class "Node"
*	Needs to store:
*		- State of the table
*		- Reward - r_i - Cumulative rewards of associated leaf nodes
*		- Number of states - n_i - Number of associated leaf nodes
*/
package mcsblackjack;
import java.util.*; 
public class MonteCarloSearchTree{
	public Node root;
    public Long state;
    public Node current;
	public class Node{
        public ArrayList<Node> children;
        public boolean isTerminal;
        public long count;
        public long reward;
        public Node(){
            this.children = new ArrayList<String>;
            this.isTerminal = false;
            this.count = 0;
            this.reward = 0;
        }
    }
    public MonteCarloSearchTree(){
    	root = new Node();
        current = root;
    }
    public selectExpand(){
        resetCurrent();
        if(current.isTerminal){
            if(current.count==0){
                rollout();
            }
            else{
                ArrayList<Long> availMoves = findMoves();
                for(Long l : availMoves){
                    addState(l);
                }
                current = availMoves.get(0);
                rollout();
            }
        }
        else{
            current = findMax();
        }
    }
    public void rollout(){
        rolloutRecurse(current);
    }
    public Node rollout
    public void backpropagate(){

    }
    public Node findMax(){

    }
    public addState(Long l){

    }
    abstract void resetCurrent();
    abstract ArrayList<Long> getMoves();
    abstract ArrayList<Long> getMoves();
    /*public boolean addWord(String word){
        // 🟢TODO: Finish this method
        // Iterate over letters
        // For each letter:
            // set current node to root
            // Check if it is in current node's children
            // if not
                // add it [and the rest of the word]
            // if is
                // set that to the current node and move on to the next letter
        Node currentNode = root;
        int index;
        boolean added = true;
        for(int i=0, n=word.length(); i<n; i++) {
            index = word.charAt(i)-'a';
            if(currentNode.children[index]==null){
                currentNode.children[index] = new Node();
            }
            if(i==n-1){
                if(currentNode.children[index].isTerminal){
                    added = false;
                }
                else{
                    currentNode.children[index].isTerminal = true;}
                }
            //System.out.println(Arrays.toString(root.children));
            currentNode = currentNode.children[index];
        }
        return added; 
        }
        public boolean removeWord(String word){
        // 🟢TODO: Finish this method
        Node currentNode = root;
        int index;
        boolean removed = true;
        boolean removeNext = false;
        for(int i=0, n=word.length(); i<n; i++) { 
            index = word.charAt(i)-'a';
            if(currentNode.children[index]==null){
                return false;
            }
            if(i == n-1){
                currentNode.children[index].isTerminal = false;
            }
            currentNode = currentNode.children[index];
        }
        return removed; 
    }
    public boolean containsWord(String word){
    	// 🟢TODO: Finish this method
        int index;
        Node currentNode = root;
        for(int i=0, n=word.length(); i<n; i++) {
            index = word.charAt(i)-'a';
            if(currentNode.children[index]==null){
                return false;
            }
            currentNode = currentNode.children[index];
        }
        if (currentNode.isTerminal){
            return true; 
        }
        return false;
    }
    public ArrayList<String> getAllWords() {
        return recurse(root, "", 0, new ArrayList<String>());
    }
    private ArrayList<String> recurse(Node currentNode, String word, int index, ArrayList<String> allWords) {
        if(currentNode.isTerminal) {
            word = word.substring(0,index);
            allWords.add(word);
        }
        for(int i = 0; i < 26; i++) {
            if(currentNode.children[i] != null) {
                recurse(currentNode.children[i], (word.substring(0, index) + Character.toString((char) (i + 'a'))+ (word+" ").substring(index+1)).replaceAll(" ", ""), index+1, allWords);
            }
        }
        return allWords;
    }*/
    /**
     * This returns a Hash Table where the keys are the targets (target words) 
     * and the value for each key is an ArrayList of all words in the lexicon 
     * whose Hamming distance to the target word is less than or equal to the given `maxDistance`.
     * 
     * Example: 
     *  The trie has words ["a", "are", "as", "new", "no", "not", "zen"] 
     * 
     *  suggestCorrections({"ben"}, 1) -> {"ben": {"zen"}}
     *  suggestCorrections({"nat"}, 2) -> {"nat": {"new", "not"}}
     *  suggestCorrections({"ben", nat"}, 1) -> {"ben": {"zen"}, nat": {not"}}
     */
    public Hashtable<String, ArrayList<String>> suggestCorrections(String[] targets, int maxDistance){
       // ✨ Extension TODO:Finish this method
       return null;    
    }

    public static void main(String[] args){
        // 🟢TODO: Write some tests that test your implementation
        LexiconTrie testTrie = new LexiconTrie();
        testTrie.addWord("app");
        testTrie.addWord("apple");
        testTrie.addWord("speech");
        testTrie.removeWord("apple");
        System.out.println(testTrie.getAllWords());
        //assert(testTrie.containsWord("banana"));
    }
}