package proj4;

/**
 * @author Rachael Birky
 * @version 04.23.14 
 * 
 * <p> TicTacToe.java
 * <p> This class represents a game of TicTacToe. 
 * 		The first player (X) is player 1, the second player (O) is player 2. 
 * 		Moves are indexed starting at zero.
 */
public class TicTacToe {

	private final int DRAW = 0;
	private final int PLAYER_1 = 1;
	private final int PLAYER_2 = 2;

	private final String PLAYER_1_MARK = "X";
	private final String PLAYER_2_MARK = "O";

	private int[][] board = new int[3][3];
	public int length = 3;

	private int winner = 0;
	private int turnNum = 1;
	private boolean isOver = false;

	public TicTacToe(){
		for (int i=0; i<board.length; i++)
			for (int j=0; j<board.length; j++){
				board[i][j] = 0;
			}
	}

	/**
	 * <p> Method:  move
	 * <p> Description: given a spot on the board,
	 * 			places the appropriate marker 
	 * @param row - row at which to move
	 * @param col - column at which to move
	 * @return true if the move was successful
	 */
	public boolean move(int row, int col){
		if (this.isOver() || board[row][col]!=0)
			//cannot move if the game if over or the spot is taken
			return false;

		int player;

		if (turnNum%2==0)
			//player 2's turn
			player = PLAYER_2;
		else
			player = PLAYER_1;

		board[row][col] = player;

		//evaluate board each time for winner / game over
		evalWinner(row, col, player);

		turnNum++;

		//move successful
		return true;
	}

	/**
	 * <p> Method: evalWinner  
	 * <p> Description:  called ever time a move has been made,
	 * 			evaluated whether the player that last played has won
	 * @param row - row last played
	 * @param col - column last played
	 * @param player - player that last played on the board
	 * 
	 */
	private void evalWinner(int row, int col, int player){

		boolean onDiagonal = ((row==col) || (row==col+2) || (col==row+2));

		boolean winHorizontal = true;
		boolean winVertical = true;
		boolean winDiagonal1 = true;
		boolean winDiagonal2 = true;		

		//check horizontal
		for (int i=0; i<board.length; i++){
			if (board[row][i] != player) winHorizontal = false;
		}

		//check vertical
		for (int i=0; i<board.length; i++){
			if (board[i][col] != player) winVertical = false;
		}

		if(onDiagonal){
			for (int i=0; i<board.length; i++){
				//diagonal to right
				if(board[i][i] != player) winDiagonal1 = false;
				//diagonal to left
				if(board[i][-1*i+(board.length-1)] != player) winDiagonal2 = false;
			}
		}
		else{
			//not on diagonal; be sure to set diagonal win to false!
			winDiagonal1 = false;
			winDiagonal2 = false;
		}

		if (winHorizontal || winVertical || winDiagonal1 || winDiagonal2){
			winner = player;
			isOver = true;
		}

	}

	/**
	 * <p> Method:  getWinner
	 * <p> Description:  returns the number of the player that has
	 * 			won on this board
	 * 
	 * @return	1 - player 1 won
	 * 			2 - player 2 won
	 * 			0 - draw
	 * 		   -1 - the game has not yet ended
	 */
	public int getWinner(){
		if (isOver)	return winner;
		else return -1;
	}

	/**
	 * <p> Method: isOver  
	 * <p> Description:  determines if the game is over,
	 * 			depending on whether the board is full, or
	 * 			if a winner has already been determined
	 * 
	 * @return true if the game has ended
	 */
	public boolean isOver(){
		if (this.isFull() || winner>0) isOver = true; 
		return isOver;
	}

	/**
	 * <p> Method: playerAt  
	 * <p> Description:  returns the number of the player that
	 * 			has played at the given location
	 * @param row - row of the spot
	 * @param col - column of the spot
	 * @return  	1 - player one played
	 * 			2 - player 2 played
	 * 			0 - no one has yet played that spot
	 */
	public int playerAt(int row, int col){
		return board[row][col];
	}

	/**
	 * <p> Method:  isFull
	 * <p> Description:  determines whether this board is full
	 * 
	 * @return true if there are no empty spaces
	 */
	public boolean isFull(){
		int emptySpaces = 0;
		for (int i=0; i<board.length; i++){
			for (int j=0; j<board.length; j++){
				if(board[i][j] == DRAW) emptySpaces++;
			}
		}
		return (emptySpaces==0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode(){
		String hashStr = "";

		for (int i=0; i<board.length; i++){
			for (int j=0; j<board.length; j++){
				hashStr += board[i][j];
			}
		}

		int hashInt = Integer.parseInt(hashStr, 3);

		return hashInt;
	}

	/**
	 * <p> Method:  printBoard
	 * <p> Description:  Prints the board to the screen legibly
	 * 
	 * 
	 */
	public void printBoard(){
		String boardStr = "";

		for (int i=0; i<board.length; i++){
			for (int j=0; j<board.length; j++){
				switch(playerAt(i, j)){
				case DRAW:	boardStr+="-"; break;
				case PLAYER_1: boardStr+=PLAYER_1_MARK; break;
				case PLAYER_2: boardStr+=PLAYER_2_MARK; break;
				}	
			}
			boardStr+="\n";
		}
		System.out.print(boardStr);;
	}

	/**
	 * <p> Method:  equals
	 * <p> Description:  compares the given TicTacToe board with the current board
	 * 						using their unique hashcode values
	 * @param aBoard - the board to which this one is being compared
	 * @return true or false; whether or not they are the same
	 */
	public boolean equals(Object aBoard){
		TicTacToe aBoardNew = (TicTacToe) aBoard;
		return this.hashCode() ==  aBoardNew.hashCode();
	}

	public String toString(){
		String returnStr = "";

		for (int i=0; i<board.length; i++){
			for (int j=0; j<board.length; j++){
				switch(playerAt(i, j)){
				case DRAW:	returnStr+="-"; break;
				case PLAYER_1: returnStr+=PLAYER_1_MARK; break;
				case PLAYER_2: returnStr+=PLAYER_2_MARK; break;
				}	
			}
			returnStr+="\n"; //change back to " "
		}
		return returnStr;
	}


	/**
	* <p> Constructor
	* <p> Description:  creates a "deep copy" of the given board
	* @param t - the board to copy
	*/
	public TicTacToe(TicTacToe t){
		for (int i=0; i<t.length; i++)
			for(int j=0; j<t.length; j++)
				this.board[i][j] = t.playerAt(i,j);

		this.turnNum = t.getTurnNum();
	}

	/**
	* <p> Method:  numEmpty
	* <p> Description:  calculates the number of empty spaces on the board
	* 
	* @return emptySpaces - number of spaces where no player has moved
	*/
	public int numEmpty(){
		int emptySpaces = 0;
		for (int i=0; i<board.length; i++){
			for (int j=0; j<board.length; j++){
				if(board[i][j] == DRAW) emptySpaces++;
			}
		}
		return emptySpaces;
	}

	/**
	* <p> Method:  getTurnNum
	* <p> Description:  returns the turn number of the current board
	* 
	* @return turnNum - the turn number of the current board
	*/
	public int getTurnNum(){
		return this.turnNum;
	}

}
