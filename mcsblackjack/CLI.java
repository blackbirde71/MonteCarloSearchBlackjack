// package msblackjack;

import java.util.HashMap;
import java.util.ArrayList;

class Card {
	public final int suit;
	public final int rank;

	public Card(int suit, int rank) {
		this.suit = suit;			
		this.rank = rank;
	}
}

enum HandType {
	DEALER,
	PLAYER,
	COMPUTER,
}

class Hand {
	public ArrayList<Card> cards;
	public HandType type;

	public Hand(ArrayList<Card> cards, HandType type) {
		this.cards = cards;
		this.type = type;

		suit.put(0, "♣");
		suit.put(1, "♦");
		suit.put(2, "♥");
		suit.put(3, "♠");

		rank.put(11, "J");
		rank.put(12, "Q");
		rank.put(13, "K");
		rank.put(14, "A");
	}

	private static HashMap<Integer, String> suit = new HashMap<Integer, String> ();

	private static HashMap<Integer, String> rank = new HashMap<Integer, String> ();

	public static String getSuit(int i) {
		return suit.get(i);
	}

	

	public static String getRank(int i) {
		if (i < 11) {
			return String.valueOf(i);
		} else {
			return rank.get(i);
		}
	}

	// public void addCard(Card c) {
	// 	cards.add(c);
	// }

	public String toString() {
		// number of cards
		int numCards = cards.size();

		// String hiddenCard = 
		// "┌---------┐\n" +
		// "|XXXXXXXXX|\n" +
		// "|XXXXXXXXX|\n" +
		// "|XXXXXXXXX|\n" +
		// "|XXXXXXXXX|\n" +
		// "|XXXXXXXXX|\n" +
		// "|XXXXXXXXX|\n" +
		// "└---------┘\n";

		String space = "    ";
		String top = "┌---------┐";
		String bottom = "└---------┘";
		String shown = "|         |";
		String hidden = "|XXXXXXXXX|";


		String hand = "";

		// card example:
		// ┌---------┐
		// | K       |
		// |		 |
		// |    ♣	 |
		// |         |
		// |       K |
		// └---------┘

		// filling the card tops
		for (int j=0; j<numCards; j++) {
			hand += top + space;	
		}

		// removes the trailing space
		hand = hand.substring(0, hand.length() - 4) + "\n";


		// filling the card middles
		for (int r=0; r<5; r++) {
			for (int c=0; c<numCards; c++) {
				if (type == HandType.PLAYER || (type == HandType.DEALER && c==0)) {
					// adding card symbols if applicable
					if (r%2 == 0) {
						// index at which the symbol should be placed
						int s = 2 + r/2 * 3;
						String symbol;
						// determine what the symbol is
						if (r == 2) {
							symbol = getSuit(cards.get(c).suit);
						} else {
							symbol = getRank(cards.get(c).rank);
						}
						hand += shown.substring(0, s) + symbol + shown.substring(s+symbol.length(), 11) + space;
					} else {
						// lines that don't contain symbols
						hand += shown + space;
					} 
				} else {
					hand += hidden + space;
				}
			}
			hand = hand.substring(0, hand.length() - 4) + "\n";
		}

		// filling the card bottoms
		for (int j=0; j<numCards; j++) {
			hand += bottom + space;	
		}

		// removes the trailing space
		hand = hand.substring(0, hand.length() - 4) + "\n";

		return hand;			
	}
}

public class CLI {

	public ArrayList<Card> dealer = new ArrayList<Card> ();
	public ArrayList<Card> player = new ArrayList<Card> ();
	public ArrayList<Card> computer = new ArrayList<Card> ();

	public static void main(String[] args){
		ArrayList<Card> a = new ArrayList<Card> ();
		Card c1 = new Card(0, 10);
		Card c2 = new Card(1, 11);
		Card c3 = new Card(2, 2);
		a.add(c1);
		a.add(c2);
		a.add(c3);

        Hand h1 = new Hand(a, HandType.PLAYER);
        Hand h2 = new Hand(a, HandType.DEALER);
        Hand h3 = new Hand(a, HandType.COMPUTER);
        System.out.println(h1.toString());
        System.out.println(h2.toString());
        System.out.println(h3.toString());

    }
}
