package proj4;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Rachael Birky
 * @version 04.23.14
 * 
 * <p> SmartPlayer.java
 * <p> This class represents a "smart" TicTacToe player.
 * 		It keeps track of moves it has already made using a hash table,
 * 		and tries to choose the next move that results in the most wins,
 * 		least losses, or random.
 */
public class SmartPlayer {

	boolean debug = false;
	Random generator = new Random();
	private int PLAYER_NUM;

	//stores keys
	private ArrayList<TicTacToe> thisGame = new ArrayList<TicTacToe>();
	public ArrayList<TicTacToe> firstMoves = new ArrayList<TicTacToe>();
	private HashTable<TicTacToe, Stats> boards = new HashTable<TicTacToe, Stats>(283);


	/**
	* <p> Constructor
	* <p> Description:  creates a new smart player object with a given player number
	*/
	public SmartPlayer(int playerNum){
		PLAYER_NUM = playerNum;
	}

	/**
	* <p> Method:  getNumSuccessors
	* <p> Description:  calculates the number of successors a board has,
	* 		determined by the number of empty spaces
	* @param t - the board to analyze
	* @return the number of empty spaces/possible successors
	*/
	private int getNumSuccessors(TicTacToe t){
		return t.numEmpty();
	}
	
	/**
	* <p> Method:  getSuccessors
	* <p> Description:  generates each successor to the given board
	* 		(boards that result from one more move)
	* @param t - the board to analyze
	* @return an array of successor boards
	*/
	private TicTacToe[] getSuccessors(TicTacToe t){
		TicTacToe tCopy1 = new TicTacToe(t);
		TicTacToe[] successors = new TicTacToe[9];

		int index=0;
		for(int i=0; i<tCopy1.length; i++){
			for(int j=0; j<tCopy1.length; j++){
				if (tCopy1.playerAt(i, j) == 0){
					tCopy1.move(i,j);
					TicTacToe tCopy2 = new TicTacToe(t);
					tCopy2.move(i, j);
					successors[index] = tCopy2;
					index++;
				}
			}
		}
		return successors;
	}

	/**
	* <p> Method:  move
	* <p> Description:  makes  a move on the given board using
	* 		past experience
	* @param t - the board on which to move
	* 
	*/
	public void move(TicTacToe t){

		TicTacToe tCopy = new TicTacToe(t);

		int numSuccessors = getNumSuccessors(tCopy);
		TicTacToe[] successors = getSuccessors(tCopy);

		boolean isFirstMove = (numSuccessors > 7);
		
		//if zero successors, game is over, so skip all this!
		if(numSuccessors>0){
			TicTacToe nextMove = null;
			int maxScore = 0;
			ArrayList<TicTacToe> tiedBoards = new ArrayList<TicTacToe>();

			//store successors in hash table to access statistics later
			for(int i=0; i<numSuccessors; i++){
				boards.put(successors[i], new Stats(successors[i]));

				//compare to current max score
				if (boards.get(successors[i]).getPercentWin() > maxScore){
					maxScore = boards.get(successors[i]).getPercentWin();
					nextMove = successors[i];
				}
			}
			
			//put all with same score into an array
			for(int i=0; i<numSuccessors; i++){
				if(boards.get(successors[i]).getPercentWin() == maxScore)
					tiedBoards.add(successors[i]);
			}

			//break tie if need be
			if(tiedBoards.size()>1){
				nextMove = tiedBoards.get(generator.nextInt(tiedBoards.size()));
			}
			else if (tiedBoards.size()==1){
				nextMove = tiedBoards.get(0);
			}
			
			//calculate the spot to move by finding difference, and actually move...
			int row=0; int col=0;
			for (int i=0; i<nextMove.length; i++){
				for (int j=0; j<nextMove.length; j++){
					if (nextMove.playerAt(i, j) != t.playerAt(i, j))
					{row = i; col = j;}
				}
			}
			t.move(row, col);

			//store resulting board in thisGame and hashTable
			TicTacToe result = new TicTacToe(t);
			thisGame.add(result);
			
			if(isFirstMove) if(!firstMoves.contains(result)) firstMoves.add(result);
			
			if(boards.containsKey(result)){
				if (debug) System.out.println("FOUND!");
				boards.get(result).incrementNumSeen();
			}
			else{
				if (debug) System.out.println("Not Found :(!");
				boards.put(result, new Stats(result));
				boards.get(result).incrementNumSeen();
			}
		}
	}

	/**
	* <p> Method: endGame  
	* <p> Description:  updates the value of each board played during the past game
	* 		according to wins, losses and draws
	* @param finalBoard - the board at the end of the game
	* 
	*/
	public void endGame(TicTacToe finalBoard){
		int winner = finalBoard.getWinner();
		if(debug)System.out.println("winner num: "+winner);
		if(debug)System.out.println("this player: "+PLAYER_NUM);

		if (winner == PLAYER_NUM){
			for (TicTacToe aBoard : thisGame){
				boards.get(aBoard).incrementNumWins();
				if (debug) {System.out.println("wins"+boards.get(aBoard).getNumWins());}
			}
		}
		else if(winner == 0){
			for (TicTacToe aBoard : thisGame){
				boards.get(aBoard).incrementNumDraws();
				if (debug) {System.out.println("draws"+boards.get(aBoard).getNumDraws());}
			}
		}
		else{
			for (TicTacToe aBoard : thisGame){
				boards.get(aBoard).incrementNumLosses();
				if (debug) {System.out.println("losses"+boards.get(aBoard).getNumLosses());}
			}
		}

		for (TicTacToe aBoard : thisGame){
			if (debug) {System.out.println("wins,losses,draws,numplayed:"+boards.get(aBoard).getNumWins()+","+boards.get(aBoard).getNumLosses()+","+boards.get(aBoard).getNumDraws()+","+boards.get(aBoard).getNumSeen());}
		}
		
		thisGame = new ArrayList<TicTacToe>();
	}
	
	/**
	* <p> Method:  newGame
	* <p> Description:  tells the player that a new game has started
	* @param player - the player number the player will be for the new game 
	* 
	*/
	public void newGame(int player){
		PLAYER_NUM = player;
		thisGame.clear();
	}

	/**
	* <p> Method:  numberOfTimesSeen
	* <p> Description:  returns the number of times this board has been seen  
	* @param t - the board to access
	* @return numSeen - the number of times this board has been seen  
	*/
	public int numberOfTimesSeen(TicTacToe t){
		Stats thisStat = boards.get(t);
		int numSeen = thisStat.getNumSeen();
		return numSeen;
	}
	
	/**
	* <p> Method:  numSlots
	* <p> Description:  returns the number of slots this smart player has
	* 		for its has table
	* @return numSlots - the number of slots this smart player has
	* 		for its hash table
	*/
	public int getNumSlots(){
		return boards.numSlots();
	}

	/**
	* <p> Method:  numEntries
	* <p> Description:  returns the number of entries this smart player has
	* 		for its hash table
	* @return numEntries - the number of entries this smart player has
	* 		for its hash table
	*/
	public int getNumEntries(){
		return boards.numEntries();
	}

	/**
	* <p> Method:  numCollisions
	* <p> Description:  returns the number of collisions this smart player has
	* 		for its has table
	* @return numSlots - the number of collisions in the smart player's hash table
	*/
	public int getNumCollisions(){
		return boards.numCollisions();
	}

	/**
	* <p> Method:  getPlayerNum
	* <p> Description:  returns the player's player number
	* @return player_num - the players number this game
	*/
	public int getPlayerNum(){
		return this.PLAYER_NUM;
	}

	public String toString(){
		return "Smart Player";
	}

	/**
	* <p> Method:  printHashTable
	* <p> Description:  prints the player's hash table for debugging purposes
	* 
	* 
	*/
	public void printHashTable(){
		boards.printHashTable();
	}

	/**
	* <p> Method:  printThisGame
	* <p> Description:  prints the series of boards played in this game for debugging purposes
	* 
	* 
	*/
	public void printThisGame(){
		for (TicTacToe aBoard : thisGame)
			System.out.println(aBoard);
	}
	
	/**
	* <p> Method:  printFirstMoves
	* <p> Description:  prints all the first moves the player has made and their statistics
	* 		for debugging purposes
	* 
	* 
	*/
	public void printFirstMoves(){
		for (TicTacToe aBoard : firstMoves)
			System.out.println(aBoard+"\n"+boards.get(aBoard).getNumWins()+" "+boards.get(aBoard).getNumSeen());
	}
	
	/**
	* <p> Method:  favFirstMove()
	* <p> Description:  gets the first move that the player plays most often
	* 
	* @return favorite - the player's "favorite" first move
	*/
	public TicTacToe favFirstMove(){
		TicTacToe favorite = firstMoves.get(0);
		for(TicTacToe aBoard : firstMoves){
			if (boards.get(aBoard).getNumSeen()>boards.get(favorite).getNumSeen())
				favorite = aBoard;
		}
		return favorite;
	}

	
	/**
	* <p> Method:  numFavWon
	* <p> Description:  gets the number of times the player's favorite first
	* 		move (most often played first move) was in a winning game
	* 
	* @return number of wins the favorite board generated
	*/
	public int numFavWon(){
		TicTacToe favorite = firstMoves.get(0);
		for(TicTacToe aBoard : firstMoves){
			if (boards.get(aBoard).getNumSeen()>boards.get(favorite).getNumSeen())
				favorite = aBoard;
		}
		return boards.get(favorite).getNumWins();
	}
	
	/**
	* <p> Method:  numFavPlayed
	* <p> Description:  gets the number of times the player's favorite first
	* 		move (most often played first move) was played
	* 
	* @return number of times the favorite board was played
	*/
	public int numFavPlayed(){
		TicTacToe favorite = firstMoves.get(0);
		for(TicTacToe aBoard : firstMoves){
			if (boards.get(aBoard).getNumSeen()>boards.get(favorite).getNumSeen())
				favorite = aBoard;
		}
		return boards.get(favorite).getNumSeen();
	}

}
