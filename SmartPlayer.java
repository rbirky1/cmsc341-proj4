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

	Random generator = new Random();

	//use when calculating win/loss
	private int PLAYER_NUM;

	//private arraylist of boards played this game
	private ArrayList<TicTacToe> thisGame;

	//Stats is value of board, num times seen, numWins, percentWin()
	private HashTable<TicTacToe, Stats> boards;

	public SmartPlayer(int playerNum){
		PLAYER_NUM = playerNum;
		boards = new HashTable<TicTacToe, Stats>(283);
	}

	public void endGame(TicTacToe finalBoard){
		//update numWins of all boards played if this player won 
		if (finalBoard.getWinner() == PLAYER_NUM){
			for (TicTacToe aBoard : thisGame){
				boards.get(aBoard).incrementNumWins();
			}
		}

	}

	private int getNumSuccessors(TicTacToe t){
		Stats thisStat = boards.get(t);

		return thisStat.getNumSuccessors();
	}
	
	private TicTacToe[] getSuccessors(TicTacToe t){
		Stats thisStat = boards.get(t);

		TicTacToe[] successors = thisStat.getSuccessors();

		return successors;
	}

	public void move(TicTacToe t){

		//Check for end of game
		if (t.isFull() || t.isOver()) return;

		if (boards.containsKey(t)){
			Stats thisBoard = boards.get(t);
			thisBoard.incrementNumSeen();
		}
		else{
			boards.put(t, new Stats());
		}

		TicTacToe nextMove = bestMove(t);

		//add move to successors if it doesn't already exist (existence checked in Stats)
		boards.get(t).addSuccessor(nextMove);
		
		//add move (resulting board) to thisGame
		thisGame.add(nextMove);

	}

	public TicTacToe bestMove(TicTacToe t){
		TicTacToe[] moves = getSuccessors(t);
		int numSuccessors = getNumSuccessors(t);
		
		//If no known successors, play randomly
		//Generates random until able to play that spot
		if (numSuccessors == 0){
			int row, col;
			do{
				row = generator.nextInt(3);
				col = generator.nextInt(3);
			} while (!t.move(row, col));
			//return edited board
			return t;
		}

		//successors length > 0...
		TicTacToe bestMove = moves[0];

		//length =/= actual number of items
		for(int i = 0; i<numSuccessors; i++){
			if (boards.get(moves[i]).getPercentWin() > boards.get(bestMove).getPercentWin())
				bestMove = moves[i];
		}

		//if multiple have bestMove %, store in array, generate random, return the board at that number
		ArrayList<TicTacToe> tiedMoves = new ArrayList<TicTacToe>();

		for(int i = 0; i<numSuccessors; i++){
			if (boards.get(moves[i]).getPercentWin() == boards.get(bestMove).getPercentWin())
				tiedMoves.add(moves[i]);
		}

		if (tiedMoves.size()>1){
			int random = generator.nextInt(tiedMoves.size());
			bestMove = tiedMoves.get(random);
		}
		
		//calculate the spot to move, actually move

		return bestMove;
	}

	public void newGame(int player){
		PLAYER_NUM = player;

		//resets arraylist of boards this game
		thisGame.clear();
	}

	public int numberOfTimesSeen(TicTacToe t){
		Stats thisStat = boards.get(t);
		int numSeen = thisStat.getNumSeen();
		return numSeen;
	}

	public String toString(){
		return "Smart Player";
	}


}
