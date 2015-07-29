
package proj4;

import java.util.Random;

/**
 * @author Rachael Birky
 * @version 04.18.14 
 * 
 * <p> RandomAI.java
 * <p> This class represents a random TicTacToe player.
 * 		If it can, it will make some legal move on the board it is given. 
 */
public class RandomAI {

	Random generator = new Random();

	/**
	* <p> Constructor
	* <p> Description: Creates a new RandomAI object with its own random generator
	* 
	*/
	public RandomAI(){

	}

	/**
	* <p> Method:  move
	* <p> Description:  Given a TicTacToe board, makes a random move.
	* 		If the game is over or the board is full, it does nothing.
	* @param t - the TicTacToe board on which to play
	* 
	*/
	public void move(TicTacToe t){

		if (t.isFull() || t.isOver()) return;

		int row, col;

		do{
			row = generator.nextInt(3);
			col = generator.nextInt(3);
		} while (!t.move(row, col));

		//once true; move there
		t.move(row,  col);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "Random AI";
	}
}
