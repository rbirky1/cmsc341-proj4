package proj4;

/**
 * @author Rachael Birky
 * @version 04.18.14
 * 
 * <p> Stats.java
 * <p> This class represents an object that
 * 		holds information about a TicTacToe board
 * 		in order to generate statistics about games
 */
public class Stats {

	private int numSeen=0;
	private int numWins=0;
	private int numDraws=0;
	private int numLosses=0;

	private TicTacToe[] successors = new TicTacToe[9];
	private int numSuccessors;

	/**
	* <p> Constructor
	* <p> Description:  creates a new statistics object
	*/
	public Stats(TicTacToe t){
	}

	/**
	* <p> Method:  getPercentWin
	* <p> Description:  returns the % of times this board was in a winning game
	* 
	* @return 100 if an unexplored board, otherwise
	* 		percent of wins compared to number of times seen
	*/
	public int getPercentWin(){
		if (numSeen==0) return 100;
		else return (int) (((double)numWins/(numSeen))*100);
	}
	
	/**
	* <p> Method:  getNumSeen
	* <p> Description:  gives the number of times this board has been seen
	* 
	* @return number of times this board was seen
	*/
	public int getNumSeen(){
		return this.numSeen;
	}

	/**
	* <p> Method:  getNumWins
	* <p> Description:  gives the number of times this board has been in a winning game
	* 
	* @return number of times this board has been in a winning game
	*/
	public int getNumWins(){
		return this.numWins;
	}

	/**
	* <p> Method:  getNumDraws
	* <p> Description:  returns the number of times this board was in a game
	* 		that ended in a draw
	* 
	* @return the number of times this board was in a game
	* 		that ended in a draw
	*/
	public int getNumDraws(){
		return this.numDraws;
	}

	/**
	* <p> Method:  getNumWins
	* <p> Description:  gives the number of times this board has been in a losing game
	* 
	* @return number of times this board has been in a losing game
	*/
	public int getNumLosses(){
		return this.numLosses;
	}

	/**
	* <p> Method:  incrementNumSeen
	* <p> Description:  adds one to the number of times this
	* 			board was seen
	* 
	* 
	*/
	public void incrementNumSeen(){
		this.numSeen++;
	}

	/**
	* <p> Method:  incrementNum
	* <p> Description:  adds one to the number of times this
	* 			board was in a winning game
	* 
	* 
	*/
	public void incrementNumWins(){
		this.numWins++;
	}

	/**
	* <p> Method:  incrementNum
	* <p> Description:  adds one to the number of times this
	* 			board was in a game that ended in a draw
	* 
	* 
	*/
	public void incrementNumDraws(){
		this.numDraws++;
	}

	/**
	* <p> Method:  incrementNum
	* <p> Description:  adds one to the number of times this
	* 			board was in a losing game
	* 
	* 
	*/
	public void incrementNumLosses(){
		this.numLosses++;
	}

	public String toString(){
		String toStr = "Num wins:"+this.getNumWins()+"\nNum draws: "+this.getNumDraws()+"\nNum Losses: "+this.getNumLosses()+" times and has a "+this.getPercentWin()+"% rate";
		return toStr;
	}

}
