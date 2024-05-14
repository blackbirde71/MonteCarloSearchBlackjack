package mcsblackjack;

import java.util.HashMap;
import java.util.ArrayList;

class Card {
	public final int suit;
	public final int rank;
	public final int cardInt;

	public Card(int cardInt) {
		this.cardInt = cardInt;
		this.suit = cardInt % 4; // Convert exact card identification number to suit and rank numbers		
		this.rank = cardInt / 4;
	}
}

enum HandType {
	DEALER,
	PLAYER,
	COMPUTER,
}

class Hand {
	public Card[] cards = new Card[5];
	public HandType type;
	private static HashMap<Integer, String> suit = new HashMap<Integer, String> (); // Hash maps assigning identification numbers to suits and ranks
	private static HashMap<Integer, String> rank = new HashMap<Integer, String> (); //

	public Hand(Card[] cards, HandType type) {
		this.cards = cards;
		this.type = type;

		suit.put(0, "♣");
		suit.put(1, "♦");
		suit.put(2, "♥");
		suit.put(3, "♠");

		rank.put(9, "J");
		rank.put(10, "Q");
		rank.put(11, "K");
		rank.put(12, "A");
	}

	public static String getSuit(int i) {
		return suit.get(i);
	}	

	public static String getRank(int i) {
		if (i < 9) { // Separate cards with face value values and face cards or aces
			return String.valueOf(i+2);
		} else {
			return rank.get(i);
		}
	}

	public void addCard(Card c) {
		int numCards=0;
		for (int i=0; i<5; i++){
			if(cards[i]==null){ // Indicates this card slot is null
				break;
			}
			numCards = i+1;
		}
		if (numCards >= 5) {
			System.out.println("You cannot draw more cards!");
		} else {
			cards[numCards] = c;
		}
	}

	public String toString() {
		// number of cards
		int numCards=0;
		for (int i=0; i<5; i++){
			if(cards[i]==null){
				break;
			}
			numCards = i+1;
		}

		// String hiddenCard = 
		// "*---------*\n" +
		// "|XXXXXXXXX|\n" +
		// "|XXXXXXXXX|\n" +
		// "|XXXXXXXXX|\n" +
		// "|XXXXXXXXX|\n" +
		// "|XXXXXXXXX|\n" +
		// "|XXXXXXXXX|\n" +
		// "*---------*\n";

		String space = "    ";
		String top = "*---------*";
		String bottom = "*---------*";
		String shown = "|         |";
		String hidden = "|XXXXXXXXX|";


		String hand = "";

		// card example:
		// *---------*
		// | 10      |
		// |		 |
		// |    ♣	 |
		// |         |
		// |      10 |
		// *---------*

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
						String symbol;
						// determine what the symbol is
						if (r == 2) {
							symbol = getSuit(cards[c].suit);
						} else {
							symbol = getRank(cards[c].rank);
						}
						// index at which the symbol should be placed
						int s = r==4 && symbol.length()==2 ? 1 + r/2 * 3 : 2 + r/2 * 3;
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
		hand = hand.substring(0, hand.length() - 4); //! removed newline at end to allow for inline handtype labelling in Blackjack

		return hand;			
	}
}
