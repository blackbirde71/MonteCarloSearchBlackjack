/*
* Main file. Will manage:
	- Blackjack player vs computer game
	- Command line interface
*/
//package mcsblackjack;
import java.util.*;
public class Blackjack{
	Hashtable<Integer, int[][]> gameStateTable = new Hashtable<>();
	public Blackjack(){
		initHashtable();
	}
	public void initHashtable(){
		int[] tableMap = new int[15];
		int[][] table = new int[3][5];
		int[] dCards, pCards, cCards;
		long stateNumber;
		int remainder;
		for(int i = 0; i < Math.pow(13,15)-1; i++){
			stateNumber = i;
			//System.out.println(i);
			//System.out.println(Math.round(Math.pow(13,14)));
			for(int p = 14; p >= 0; p--){
				tableMap[14-p] = Math.toIntExact(stateNumber / Math.round(Math.pow(13,p)));
				remainder = Math.toIntExact(stateNumber % Math.round(Math.pow(13,p)));
				stateNumber -= remainder;
			}
			//System.out.println(Arrays.toString(tableMap));
			dCards = Arrays.copyOfRange(tableMap, 10, 14); //! Backwards?
			pCards = Arrays.copyOfRange(tableMap, 5, 9);
			cCards = Arrays.copyOfRange(tableMap, 0, 4);
			table[0] = dCards;
			table[1] = pCards;
			table[2] = cCards;
			gameStateTable.put(i, table);
		}
	}
	public Long hashState(int[][] table){
		Long stateNumber = new Long(0);
		for(int hi = 0; hi<3; hi++){
			for(int ci = 0; ci<5; ci++){
				stateNumber += Math.round(table[hi][ci] * Math.pow(13,5*hi+ci));
			}
		}
		return stateNumber;
	}
	public static void main(String[] args){
		Blackjack test = new Blackjack();
		//int[][] testState = {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
		//test.hashState(testState);
		for(int i = 0; i < Math.pow(13,10)-1; i++){

		}
		System.out.println("finish");
    }
}
