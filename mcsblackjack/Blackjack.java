/*
* Main file. Will manage:
	- Blackjack player vs computer game
	- Command line interface
*/
package mcsblackjack;
import java.util.*
public class Blackjack{
	Hashtable<String[], Long> gameStateTable = new Hashtable<>();
	public Blackjack{
		initHashtable();
	}
	public void initHashtable{
		int[] tableMap = new int[15];
		int[][] table = new int[3][5];
		int[] dCards, pCards, cCards;
		int remainder, stateNumber;
		for(int i = 0; i < Math.pow(13,5)-1; i++){
			stateNumber = i;
			for(p = 14; p >= 0; p--){
				remainder = stateNumber % Math.pow(13,p);
				tableMap[14-p] = remainder;
				stateNumber -= remainder;
			}
			dCards = Collections.reverse(Arrays.asList(Arrays.copyOfRange(tableMap, 10, 5))); //! Backwards? I hope
			pCards = Collections.reverse(Arrays.asList(Arrays.copyOfRange(tableMap, 5, 5)));
			cCards = Collections.reverse(Arrays.asList(Arrays.copyOfRange(tableMap, 0, 5)));
			table[0] = dCards;
			table[1] = pCards;
			table[2] = cCards;
			gameStateTable.put(i, table);
		}
	}
	public Long hashState(int[][] table){
		Long stateNumber = long(0);
		for(int hi = 0; hi<3; hi++){
			for(int ci = 0; ci<5; ci++){
				stateNumber += table[hi][ci] * Math.pow(13,5*hi+ci);
			}
		}
		return stateNumber;
	}
}
