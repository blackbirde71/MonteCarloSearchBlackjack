package mcsblackjack;
 import java.util.*; 

public class BlackjackState {
	public int[] cards = new int[5];
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
			if (i > maxScore) {
				maxScore = i;
			}
		}
		return maxScore;
	}

	public void helper(int[] cards, int numCards, int currScore, int index, ArrayList<Integer> possibleScores) {
		// System.out.println(numCards);
		// checking if you have 5 cards
		if (numCards > 4) {
			possibleScores.add(21);
			return;
		}
		if (index == numCards-1) {
			possibleScores.add(currScore);
				// the last four cards are aces
				if (cards[index] > 47) {
				// ace is 11
				possibleScores.add(currScore+1);
				// ace is 1
				possibleScores.add(currScore+11);
			}
			return;
		}
		// the last four cards are aces



		if (cards[index] > 47) {
			// ace is 11
			helper(cards, numCards, currScore+11, index+1, possibleScores);
			// ace is 1
			helper(cards, numCards, currScore+1, index+1, possibleScores);
		}

		int value = cards[index] / 13 + 2;

		if (value > 10) {
			helper(cards, numCards, currScore+10, index+1, possibleScores);
		} else {
			helper(cards, numCards, currScore+value, index+1, possibleScores);
		}
	}

	public boolean equals(BlackjackState a) {
		if (Arrays.equals(this.cards, a.cards) && this.isStanding == a.isStanding) return true;
		return false;
	}

	BlackjackState(int[] arrayOfCards, boolean isStanding) {
		int i = 0;
		int score = 0;
		for (int c : arrayOfCards) {
			cards[i] = c;
			i++;
		}
		int numCards = arrayOfCards.length;

		score = calcScore(numCards);
		this.isStanding = isStanding;
		if (isStanding) isEnd = true;
		if (numCards == 5) isEnd = true;
		if (score > 20) isEnd = true;
	}
}