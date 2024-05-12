/*
* Main file. Will manage:
	- Blackjack player vs computer game
	- Command line interface
*/

package mcsblackjack;
import java.util.*;

public class Blackjack{
	public BlackjackState state;
	// public Blackjack() {
		
	// }
	public static void main(String[] args){
		System.out.println("START");
		ArrayList<Integer> cards = new ArrayList<Integer>();
		int EXPLORATION = 2;
		int NUMITERATIONS = 100;

		Integer[] deck = new Integer[52];
		for (int i=0; i<52; i++) {
			deck[i] = i;
		}
		List<Integer> decklist = Arrays.asList(deck);
		Collections.shuffle(decklist);

		int index = 0;

		cards.add(decklist.get(index));
		index++;
		cards.add(decklist.get(index));
		index++;

		BlackjackState bjs = new BlackjackState(cards, false);

		BlackjackTree bjt = new BlackjackTree(19, cards, EXPLORATION, NUMITERATIONS);
		int result = -1;
		while (result < 0) {
			result = bjt.play();
			if (result < 0) {
				bjt.updateGameState(decklist.get(index));
				index++;
			}
		}
		System.out.println(result);

    }
}
// class bj extends Blackjack{
// 	public bj(){int state = 2;}
// 	public static void main(String[] args){
// 	System.out.println(state);
//     }
// }