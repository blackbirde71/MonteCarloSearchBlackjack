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
    public List<Integer> decklist;
    public Integer[] deck;
    public int deckIndex, dIndex, pIndex;
    public static HashMap<Integer, String> suit;
    public static HashMap<Integer, String> rank;
    public Card[] dHand = new Card[5];
    public Card[] pHand = new Card[5];
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
		suit.put(0, "♣"); //? Necessary?
		suit.put(1, "♦");
		suit.put(2, "♥");
		suit.put(3, "♠");
		rank.put(9, "J");
		rank.put(10, "Q");
		rank.put(11, "K");
		rank.put(12, "A");
	}
	public ArrayList<Integer> dealCards(){
		Integer[] deck = new Integer[52];
		for (int i=0; i<52; i++) {
			deck[i] = i;
		}
		decklist = Arrays.asList(deck);
		Collections.shuffle(decklist);
		ArrayList<Integer> cHand = new ArrayList<Integer>();
		dHand[dIndex++] = new Card(decklist.get(deckIndex++).intValue()); //! check functionality of inline post-increment
		dHand[dIndex++] = new Card(decklist.get(deckIndex++).intValue());
		pHand[pIndex++] = new Card(decklist.get(deckIndex++).intValue());
		pHand[pIndex++] = new Card(decklist.get(deckIndex++).intValue());
		cHand.add(decklist.get(deckIndex++));
		cHand.add(decklist.get(deckIndex++));
		return cHand;
	}
	public int calcScore(Card[] hand){
		ArrayList<Integer> handAL = new ArrayList<Integer>();
		for(Card c : hand){
			if(c == null){
				break;
			}
			handAL.add(4*c.rank+c.suit);
		}
		bjs = new BlackjackState(handAL, false);
		return bjs.score;
	}
	public void dTurn(){
		if(! dDone){
			int total = calcScore(dHand);
			if(total<17){
				print("DEALER HIT");
				dHand[dIndex++] = new Card(decklist.get(deckIndex++).intValue());
				total = calcScore(dHand);
				if(total>21){
				print("DEALER BUST");
				endGame();
				}
			}
			else{
				print("DEALER STAND");
				dDone = true;
			}
		}
	}
	public void cTurn(){
		if(! bjt.current.state.isStanding){
			String cMove = bjt.chooseMove();
			print("COMPUTER " + cMove);
			if(cMove == "STAND"){
				cDone = true;
			}
			else if(bjt.current.state.isEnd && !bjt.current.state.isStanding){
				print("COMPUTER BUST");
				cDone = true;
			}
		}
	}
	public void pTurn(){
		if(! pDone){}
			Scanner readIn = new Scanner(System.in);
			printI("(h)it or (s)tand? ");
			String pMove = readIn.nextLine();
			if(pMove.equals("h")){
				print("PLAYER HIT");
				pHand[pIndex++] = new Card(decklist.get(deckIndex++).intValue()); //> Check for hand of 5 and then announce player win in this case
				int total = calcScore(pHand);
				print(String.valueOf(total));
				if(total>21){
					print("PLAYER BUST");
					pDone = true;
				}
			}
			else{
				print("PLAYER STAND");
				pDone = true;
			}
		}
	public void displayTable(){ //> Combine with displayEndTable()
		Card[] cHandArray = new Card[5];
		for(int i = 0; i<bjt.current.state.cards.size(); i++){
			cHandArray[i] = new Card(bjt.current.state.cards.get(i));
		}
		Hand cCards = new Hand(cHandArray, HandType.COMPUTER);
		Hand dCards = new Hand(dHand, HandType.DEALER);
		Hand pCards = new Hand(pHand, HandType.PLAYER);
		print("\n"+cCards.toString()+"  computer\n");
		print(dCards.toString()+"  dealer\n");
		print(pCards.toString()+"  player\n");
	}
	public void displayEndTable(){
		Card[] cHandArray = new Card[5];
		for(int i = 0; i<bjt.current.state.cards.size(); i++){
			cHandArray[i] = new Card(bjt.current.state.cards.get(i));
		}
		Hand cCards = new Hand(cHandArray, HandType.PLAYER);
		Hand dCards = new Hand(dHand, HandType.PLAYER);
		Hand pCards = new Hand(pHand, HandType.PLAYER);
		print("\n"+cCards.toString()+"  computer\n");
		print(dCards.toString()+"  dealer\n");
		print(pCards.toString()+"  player\n");
	}
	public void displayResults(){
		String dEnd, pEnd, cEnd;
		String pResult, cResult;
		int dTotal = calcScore(dHand);
		int pTotal = calcScore(pHand);
		int cTotal = bjt.current.state.score;
		if(dTotal>21){
			dEnd = "BUSTED";
			dTotal = 0;
		}
		else{
			dEnd = "STANDING";
		}
		if(pTotal>21){
			pEnd = "BUSTED";
			pTotal = -1;
		}
		else{
			pEnd = "STANDING";
		}

		if(bjt.current.state.isEnd && ! bjt.current.state.isStanding){
			cEnd = "BUSTED";
			cTotal = -1;
		}
		else{
			cEnd = "STANDING";
		}
		if(pTotal>dTotal){
			pResult = "WIN";
		}
		else if(pTotal==dTotal){
			pResult = "TIE";
		}
		else{
			pResult = "LOSS";
		}
		if(cTotal>dTotal){
			cResult = "WIN";
		}
		else if(cTotal==dTotal){
			cResult = "TIE";
		}
		else{
			cResult = "LOSS";
		}
		print("DEALER " + dEnd + " ");
		print("COMPUTER " + cEnd + " " + cResult);
		print("PLAYER " + pEnd + " " + pResult);
	}
	public void endGame(){
		print("\ngame over");
		displayEndTable();
		displayResults();
	}
	public void runGame(){
		displayTable();
		/*for(Card c : pHand){
			print(String.valueOf(c.rank));
			print(String.valueOf(c.suit));
		}*/
		print("round " + roundNum++);
		dTurn();
		bjt.play();
		cTurn();
		pTurn();
		if(dDone && cDone && pDone){
			endGame();
		}
		else{
			runGame();
		}
	}
	public void print(String s){
		try{Thread.sleep(500);}catch(InterruptedException e){}
		System.out.println(s);
	}
	public void printI(String s){
		try{Thread.sleep(500);}catch(InterruptedException e){}
		System.out.printf(s);
	}
	public static void main(String[] args){
		Blackjack bj = new Blackjack();
		ArrayList<Integer> cHand = bj.dealCards();
		bj.bjt = new BlackjackTree(bj.dHand[0].cardInt, cHand, bj.EXPLORATION, bj.NUMITERATIONS);
		bj.runGame();
    }
}