package mcsblackjack;
 import java.util.*;  

public class BlackjackState {
	public ArrayList<Integer> cards = new ArrayList<Integer>();
	public boolean isEnd = false;
	public int score;
	public boolean isStanding;
	public int numCards; 

	public int calcScore(int numCards) {
		// account for different values of aces
		ArrayList<Integer> possibleScores = new ArrayList<Integer>();
		// launch recursion
		helper(cards, numCards, 0, 0, possibleScores);
		// selecting the max score
		int maxScore = 0;
		for (Integer i : possibleScores) {
			// greater than the prev. maxScore but not exceeding 21
			if (i > maxScore && i <= 21) {
				maxScore = i;
			}
		}

		// checking if you have 5 cards that do not exceed 21
		if (numCards > 4) {
			maxScore = 21;
		}

		return maxScore;
	}

	public void helper(ArrayList<Integer> cards, int numCards, int currScore, int index, ArrayList<Integer> possibleScores) {
		// System.out.println(numCards);
		

		// if the current card is an ace
		if (cards.get(index) > 47) {
			// if it is the last card
			if (index == numCards-1) {
				// ace is 1 OR 11
				possibleScores.add(currScore+1);
				possibleScores.add(currScore+11);
			} else {
				// ace is 1 OR 11
				helper(cards, numCards, currScore+11, index+1, possibleScores);
				helper(cards, numCards, currScore+1, index+1, possibleScores);
			}
		} else {
			int value = cards.get(index) / 4 + 2;

			if (value > 10) value = 10;

			if (index == numCards-1) {
				possibleScores.add(currScore+value);
			} else {
				helper(cards, numCards, currScore+value, index+1, possibleScores);
			}
		}
	}

	public boolean equals(BlackjackState a) {
		if (this.cards.equals(a.cards) && this.isStanding == a.isStanding) return true;
		return false;
	}

	BlackjackState(ArrayList<Integer> arrayOfCards, boolean isStanding) {
		for (int c : arrayOfCards) {
			cards.add(c);
		}
		numCards = arrayOfCards.size();

		score = calcScore(numCards);

		this.isStanding = isStanding;

		if (isStanding) isEnd = true;
		if (numCards >= 5) isEnd = true;
		if (score > 20) isEnd = true;
		if (score == 0) isEnd = true;


		// System.out.println(cards.toString());
		// System.out.println(score);
		// System.out.println(isEnd);
		if (isEnd) {
			// System.out.println("end score: ");
			// System.out.println(score);

			// System.out.println(cards.toString());
		}
	}
}