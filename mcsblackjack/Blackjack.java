/*
* Main file. Will manage:
	- Blackjack player vs computer game
	- Command line interface
*/
//package mcsblackjack;
import java.util.*;
abstract class Blackjack{
	public int state;
	public Blackjack(){int state;}
	public static void main(String[] args){
	System.out.println(state);
    }
}
class bj extends Blackjack{
	public bj(){int state = 2;}
	public static void main(String[] args){
	System.out.println(state);
    }
}