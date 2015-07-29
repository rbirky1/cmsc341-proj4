package proj4;

/**
 * @author Rachael Birky
 * @version 
 * 
 * <p> Project4.java
 * <p> This class simulates TicTacToe games
 * 		between two players (random and learning AI)
 * 		and prints the statistics of the games played.
 */
public class Project4 {

	private int numRandomWins = 0;
	private int numSmartWins = 0;
	private int numDraws = 0;
	private int numGames = 0;

	private RandomAI randomPlayer;
	private SmartPlayer smartPlayer;

	/**
	* <p> Constructor
	* <p> Description: creates a new driver (proj4 object)  
	*/
	public Project4(){
		
	}

	/**
	* <p> Method:  play
	* <p> Description: plays a random player and smart player against
	* 		each other at tic tac toe a specified number of games 
	* @param aRandomPlayer - a random ai player object
	* 		aSmartPlayer - a smart player object
	* 		aNumGames - the number of games to play
	*/
	public void play(RandomAI aRandomPlayer, SmartPlayer aSmartPlayer, int aNumGames){

		numGames = aNumGames;
		randomPlayer = aRandomPlayer;
		smartPlayer = aSmartPlayer;
		int smartNum = smartPlayer.getPlayerNum();


		for (int i =0; i<aNumGames; i++){
			TicTacToe theBoard = new TicTacToe();
			smartPlayer.newGame(smartNum);

			//smart plays first
			if (smartPlayer.getPlayerNum() == 1){
				while(!theBoard.isOver()){
					smartPlayer.move(theBoard);
					randomPlayer.move(theBoard);
				}
			}

			//random plays first
			if(smartPlayer.getPlayerNum() == 2){
				while(!theBoard.isOver()){
					randomPlayer.move(theBoard);
					smartPlayer.move(theBoard);
				}
			}

			smartPlayer.endGame(theBoard);
			
			int winner = theBoard.getWinner();
			if (winner == smartNum) {numSmartWins++;}
			else if (winner == 0) {numDraws++;}
			else {numRandomWins++;}

		}
		printResults();
	}

	/**
	* <p> Method:  printResults
	* <p> Description:  prints a summary of the series of games
	*/
	public void printResults(){
		
		int numSmartCollisions = smartPlayer.getNumCollisions();
		int numSmartEntries = smartPlayer.getNumEntries();
		int numSmartSlots = smartPlayer.getNumSlots();
		
		int percentSmartWins = (int) (((double)numSmartWins/numGames)*100);
		int percentRandomWins = (int) (((double)numRandomWins/numGames)*100);
		
		int percentFull = (int) ((double)(numSmartEntries)/numSmartSlots*100);

		System.out.println("FINAL REPORT:");
		System.out.println("The number of slots is: "+numSmartSlots);
		System.out.println("The number of entries is: "+numSmartEntries);
		System.out.println("The % full is: "+percentFull);
		System.out.println("The number of collisions is: "+numSmartCollisions);

		System.out.println("Smart Player has won " +numSmartWins+ " times, which is "+percentSmartWins+" percent");
		System.out.println("Random AI has won " +numRandomWins+ " times, which is "+percentRandomWins+" percent");

		TicTacToe favFirstMove = smartPlayer.favFirstMove();
		int numFavWon = smartPlayer.numFavWon();
		int numFavPlayed = smartPlayer.numFavPlayed();
		int percentFavWon = (int) ((double)(numFavWon)/numFavPlayed*100);
		System.out.println("My favorite first move is: \n\n"+favFirstMove);
		System.out.println("Won "+numFavWon+" out of "+numFavPlayed+", which is "+percentFavWon+"%");
	}

	/**
	* <p> Method:  main
	* <p> Description: runs the driver by creating a random ai and a smart player ai,
	* 		then passing them to the play function
	* @param args - the command line arguments
	*/
	public static void main(String[] args){
		Project4 test = new Project4();
		RandomAI player1 = new RandomAI();
		SmartPlayer player2 = new SmartPlayer(1);
		test.play(player1, player2, 1000);
	}

}
