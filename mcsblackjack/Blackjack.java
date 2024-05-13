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
    public int deckIndex, dIndex, pIndex;
    public static HashMap<Integer, String> suit;
    public static HashMap<Integer, String> rank;
    public Card[] dHand, pHand;
    public int roundNum;
    public boolean dDone, cDone, pDone;
	public Blackjack() {
		this.suit = new HashMap<Integer, String> ();
		this.rank = new HashMap<Integer, String> ();
		learnCards();
		this.EXPLORATION = 2;
		this.NUMITERATIONS = 100;
		this.deckIndex = 0;
		this.dIndex = 0;
		this.pIndex = 0;
		this.roundNum = 1;
		this.dDone = false;
		this.cDone = false;
		this.pDone = false;
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
	public Card[][] dealCards(){
		Integer[] deck = new Integer[52];
		for (int i=0; i<52; i++) {
			deck[i] = i;
		}
		List<Integer> decklist = Arrays.asList(deck);
		Collections.shuffle(decklist);
		ArrayList<Integer> cHand = new ArrayList<Integer>();
		dHand[dIndex++] = new Card(Integer.intValue(decklist.get(deckIndex++))); //! check functionality of inline post-increment
		dHand[dIndex++] = new Card(Integer.intValue(decklist.get(deckIndex++)));
		pHand[pIndex++] = new Card(Integer.intValue(decklist.get(deckIndex++)));
		pHand[pIndex++] = new Card(Integer.intValue(decklist.get(deckIndex++)));
		cHand.add(decklist.get(deckIndex++));
		cHand.add(decklist.get(deckIndex++));
	}
	public void dTurn(){
		if(! dDone){
			int total = 0;
			for(card c : dHand){
				total += Card.getRank(c.rank);
			}
			if(total<17){
				System.out.println("DEALER HIT");
				dHand[dIndex++] = new Card(Integer.intValue(decklist.get(deckIndex++)));
			}
			else{
				System.out.println("DEALER STAND");
				dDone = true;
			}
			if(total + Card.getRank(dHand[dIndex].rank) > 21){
				System.out.println("DEALER BUST");
				endGame();
			}
		}
	}
	public void cTurn(){
		if(! bjt.current.isStanding){
			String cMove = bjt.chooseMove(); //> IMPORTANT: Change .chooseMove() to return "HIT" or "STAND" string
			System.out.println("COMPUTER " + cMove);
			if(cMove == "STAND"){
				cDone = true;
			}
			else if(bjt.current.isEnd){
				System.out.println("COMPUTER BUST"); //> IMPORTANT: Change .isEnd to only be true when the computer busts, not when standing
				cDone = true;
			}
		}
	}
	public void pTurn(){
		if(! pDone){}
			Scanner readIn = new Scanner(System.in);
			System.out.println("(h)it or (s)tand? ");
			char pMove = readIn.nextLine();
			if(pMove == 'h'){
				System.out.println("PLAYER HIT");
				pHand[pIndex++] = new Card(Integer.intValue(decklist.get(deckIndex++)));
				int total = 0;
				for(card c : dHand){
					total += Card.getRank(c.rank);
				}
				if(total>21){
					System.out.println("PLAYER BUST");
					pDone = true;
				}
			}
			else{
				System.out.println("PLAYER STAND");
				pDone = true;
			}
		}
	}
	public void displayTable(){
		//> IMPLEMENT: Print all hands, with secret cards displaying their backside
	}
	public void displayEndTable(){
		//> IMPLEMENT: Print all hands, with all cards face-up (game is over)
	}
	public void displayResults(){
		String dEnd, pEnd, cEnd;
		String pResult, cResult;
		int dTotal = pTotal = 0;
		int cTotal = bjt.current.score;
		for(card c : dHand){
			dTotal += Card.getRank(c.rank);
		}
		for(card c : pHand){
			pTotal += Card.getRank(c.rank);
		}
		if(dTotal>21){
			dEnd = "BUSTED"
			dTotal = 0;
		}
		else{
			dEnd = "STANDING"
		}
		if(pTotal>21){
			pEnd = "BUSTED"
			pTotal = -1;
		}
		else{
			pEnd = "STANDING"
		}

		if(bjs.current.isEnd){
			cEnd = "BUSTED"
			cTotal = -1;
		}
		else{
			cEnd = "STANDING"
		}
		if(pTotal>dTotal){
			pResult = "WIN"
		}
		else if(pTotal=dTotal){
			pResult = "TIE"
		}
		else{
			pResult = "LOSS"
		}
		if(cTotal>dTotal){
			cResult = "WIN"
		}
		else if(cTotal=dTotal){
			cResult = "TIE"
		}
		else{
			cResult = "LOSS"
		}
		System.out.println("DEALER " + dEnd + " ");
		System.out.println("COMPUTER " + cEnd + " " + cResult);
		System.out.println("PLAYER " + pEnd + " " + pResult);
	}
	public void endGame(){
		System.out.println("game over");
		displayEndTable();
		displayResults();
	}
	public void runGame(){
		System.out.println("round " + roundNum);
		dTurn();
		bjt.play();
		cTurn();
		pTurn();
		displayTable();
		if(dDone && cDone && pDone){
			endGame();
		}
		else{
			runGame();
		}
	}
	public static void main(String[] args){
		dealCards();
		this.bjs = new BlackjackState(cHand, false); //? What is this needed for?
		this.bjt = new BlackjackTree(dHand[0].cardInt, cHand, this.EXPLORATION, this.NUMITERATIONS);
		runGame();
    }
}