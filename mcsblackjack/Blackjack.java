/*
* Main file; runs the game
* Implements computer algorithmic choice, dealer deterministic choice, and player input choice
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
    public Card[] dHand = new Card[5];
    public Card[] pHand = new Card[5];
    public int roundNum;
    public boolean dDone, cDone, pDone;
	public Blackjack() {
		this.EXPLORATION = 2;
		this.NUMITERATIONS = 10000;
		this.deckIndex = 0;
		this.dIndex = 0;
		this.pIndex = 0;
		this.roundNum = 1;
		this.dDone = false;
		this.cDone = false;
		this.pDone = false;
	}
	public ArrayList<Integer> dealCards(){
		Integer[] deck = new Integer[52];
		for (int i=0; i<52; i++) {
			deck[i] = i;
		}
		decklist = Arrays.asList(deck);
		Collections.shuffle(decklist);
		ArrayList<Integer> cHand = new ArrayList<Integer>();
		dHand[dIndex++] = new Card(decklist.get(deckIndex++).intValue());
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
				if(total==0){
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
		if(!cDone){
			String cMove = bjt.play();
			print("COMPUTER " + cMove);
			if(cMove == "STAND"){
				cDone = true;
			} else {
				bjt.updateGameState(decklist.get(deckIndex++).intValue());
			}
			if(bjt.gameNode.state.score==0){
				print("COMPUTER BUST");
				cDone = true;
			}
		}
	}
	public void pTurn(){
		if(! pDone){
			Scanner readIn = new Scanner(System.in);
			printI("(h)it or (s)tand? ");
			String pMove = readIn.nextLine();
			if(pMove.equals("h")){
				print("PLAYER HIT");
				pHand[pIndex++] = new Card(decklist.get(deckIndex++).intValue());
				int total = calcScore(pHand);
				if(total==0){
					print("PLAYER BUST");
					pDone = true;
				}
			}
			else{
				print("PLAYER STAND");
				pDone = true;
			}
		}
	}
	public void displayTable(boolean gameIsOver){
		Card[] cHandArray = new Card[5];
		for(int i = 0; i<bjt.gameNode.state.cards.size(); i++){
			cHandArray[i] = new Card(bjt.gameNode.state.cards.get(i));
		}
		Hand cCards, pCards, dCards;
		if(gameIsOver){
			cCards = new Hand(cHandArray, HandType.PLAYER);
			dCards = new Hand(dHand, HandType.PLAYER);
			pCards = new Hand(pHand, HandType.PLAYER);
		}
		else{
			cCards = new Hand(cHandArray, HandType.COMPUTER);
			dCards = new Hand(dHand, HandType.DEALER);
			pCards = new Hand(pHand, HandType.PLAYER);
		}
		System.out.println();
		print(dCards.toString()+"  dealer\n");
		print(cCards.toString()+"  computer\n");
		print(pCards.toString()+"  player\n");
	}
	public void displayResults(){
		String dEnd, pEnd, cEnd;
		String pResult, cResult;
		int dTotal = calcScore(dHand);
		int pTotal = calcScore(pHand);
		int cTotal = bjt.gameNode.state.score;
		if(dTotal==0){
			dEnd = "BUSTED";
			dTotal = 0;
		}
		else{
			dEnd = "STANDING";
		}
		if(pTotal==0){
			pEnd = "BUSTED";
			pTotal = -1;
		}
		else{
			pEnd = "STANDING";
		}
		if(cTotal==0){
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
		displayTable(true);
		displayResults();
		System.exit(0);
	}
	public void runGame(){
		displayTable(false);
		print("round " + roundNum++);
		dTurn();
		bjt.play();
		cTurn();
		pTurn();
		if(dDone && cDone && pDone){
			endGame();
		}
		runGame();
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