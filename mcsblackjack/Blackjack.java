/*
* Main file. Will manage:
	- Blackjack player vs computer game
	- Command line interface
*/
package mcsblackjack;
import java.util.*
public class Blackjack{
	Hashtable<Hand[], Long> gameStateTable = new Hashtable<>();
	public Blackjack{
		initHashtable();
	}
	public void initHashtable{

	}
	public Long hashState(Hand[] table){
		Long stateNumber = long(0);
		for(int hi = 0; hi<3; hi++){
			for(int ci = 0; ci<5; ci++){
				stateNumber += table[hi][ci] * Math.pow(13,hi)
			}
		}
		return stateNumber;
	}
}
