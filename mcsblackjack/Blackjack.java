/*
* Main file. Will manage:
	- Blackjack player vs computer game
	- Command line interface
*/
package mcsblackjack;
import java.util.*;
public class Blackjack{
	public int EXPLORATION;
    public int NUMITERATIONS;
    public BlackjackState bjs;
    public BlackjackTree bjt;
    public Integer[] deck;
    public static HashMap<Integer, String> suit;
    public static HashMap<Integer, String> rank;
	public Blackjack() {
		this.suit = new HashMap<Integer, String> ();
		this.rank = new HashMap<Integer, String> ();
		learnCards();
		this.EXPLORATION = 2;
		this.NUMITERATIONS = 100;
	}
	public void learnCards(){
		suit.put(0, "♣");
		suit.put(1, "♦");
		suit.put(2, "♥");
		suit.put(3, "♠");
		rank.put(9, "J");
		rank.put(10, "Q");
		rank.put(11, "K");
		rank.put(12, "A");
	}
	public void dealCards(){
		Integer[] deck = new Integer[52];
		for (int i=0; i<52; i++) {
			deck[i] = i;
		}
		List<Integer> decklist = Arrays.asList(deck);
		Collections.shuffle(decklist);
		Card[] dHand, pHand;
		ArrayList<Integer> cHand = new ArrayList<Integer>();
		int index = 0;
		dHand[0] = new Card(Integer.intValue(decklist.get(index++))); //! check functionality of inline post-increment
		dHand[1] = new Card(Integer.intValue(decklist.get(index++)));
		pHand[0] = new Card(Integer.intValue(decklist.get(index++)));
		pHand[1] = new Card(Integer.intValue(decklist.get(index++)));
		cHand.add(decklist.get(index++));
		cHand.add(decklist.get(index++));
		this.bjs = new BlackjackState(cHand, false);
		this.bjt = new BlackjackTree(dHand[0].cardInt, cHand, this.EXPLORATION, this.NUMITERATIONS);
	}
	public void runGame(){
		dealerDraw();
	}
	public static void main(String[] args){
		dealCards();
		runGame();
    }
}