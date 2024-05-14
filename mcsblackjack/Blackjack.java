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
		int EXPLORATION = 1;
		int NUMITERATIONS = 120;

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
		// cards.add(30);
		// index++;
		// cards.add(22);
		// index++;

		BlackjackState bjs = new BlackjackState(cards, false);

		int dealerCard = decklist.get(index);
		// dealerCard = 42;
		index++;
		System.out.print("dealer: ");
		System.out.println(dealerCard);
		BlackjackTree bjt = new BlackjackTree(dealerCard, cards, EXPLORATION, NUMITERATIONS);
		int result = -1;
		while (true) {
			System.out.println("____________________________");
			result = bjt.play();
			System.out.println("____________________________");
			if (result < 0) {
				int newCard = decklist.get(index);
				bjt.updateGameState(newCard);
				index++;
				System.out.print("newCard: ");
				System.out.println(newCard);
			} else {
				break;
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