package ex1;
import java.util.Scanner;

/**
 * The Competition class represents a Nim competition between two players, 
 * consisting of a given number of rounds. 
 * It also keeps track of the number of victories of each player.
 * @author Avichai
 */
public class Competition {
	
	/* You need to implement this class */
	private Player player1;
	private Player player2;
	/*
	 * a flag indicating whether gameplay messages 
	 * should be printed to the console.
	 */
	private boolean displayMessage;
	private static final int FIRST_PLAYER = 1; // position of first player.
	private static final int SECOND_PLAYER = 2; // position of second player.
	private int numWinsOfFirstPlayer; // total number winnings for first player.
	private int numWinsOfSecondPlayer; // total number winnings for second player.
	//initial wins in the beginning of the game.
	private static final int INITIAL_NUM_OF_WINS = 0; 

	//constant for illegal position of a player.
	private static final int ILLEGAL_PLAYER_POSITION = -1;

	//The index in the array of arguments for the type of player1.
	private static final int INDEX_ARGU_TYPE_OF_FIRST_PLAYER = 0;

	//The index in the array of arguments for the type of player2.
	private static final int INDEX_ARGU_TYPE_OF_SECOND_PLAYER = 1;

	//The index in the array of arguments for number of rounds.
	private static final int INDEX_ARGU_NUM_OF_ROUNDS = 2;
	// Sum the rounds that played already.	
	private int roundsMade;
	// Initial the sum the rounds that played already.	
	private static final int INITIAL_ROUNDS_MADE = 0;
	// when there are no more sticks on the board.
	private static final int NO_MORE_STICKS = 0;
	//constant for player making a legal move.
	private static final int LEGAL_MOVE = 1;
	// A constant for starting a game message.
	private static final String STARTING_GAME_MESSAGE_PART1 = 
			"Starting a Nim competition of ";
	// A constant for starting a game message.
	private static final String STARTING_GAME_MESSAGE_PART2 = " rounds between a ";
	// A constant for starting a game message.
	private static final String STARTING_GAME_MESSAGE_PART3 = " player and a ";
	// A constant for starting a game message.
	private static final String STARTING_GAME_MESSAGE_PART4 = " player.";
	// A constant for the results of the game message.
	private static final String RESULTS_MESSAGE = "The results are ";
	// A constant for colon.
	private static final String COLON = ":";
	// A constant for welcoming the players to the game message.
	private static final String WELCOM_MESSAGE = "Welcome to the sticks game!";
	// A constant for inserting an invalid move message.
	private static final String INVALID_MOVE_MESSAGE = "Invalid move. Enter another:";
	// A constant for string that we use multiple times.
	private static final String PLAYER = "Player ";
	// A constant for winning a game message.
	private static final String WON_MESSAGE = " won!";
	// A constant for telling a human player that it is his turn message.
	private static final String IT_IS_YOUR_TURN_MESSAGE = ", it is now your turn!";
	// A constant for a move that occurred message.
	private static final String MADE_THE_MOVE_MESSAGE = " made the move: ";
	
	private static final int INITIAL_NUM_OF_INVALID_MOVE = 0;
	
	/**
	 * Receives two Player objects, representing the two competing opponents, 
	 * and a flag determining whether messages should be displayed.
	 * @param player1 - The Player objects representing the first player.
	 * @param player2 - The Player objects representing the second player.
	 * @param displayMessage - a flag indicating whether gameplay messages 
	 * should be printed to the console.
	 */
	public Competition(Player player1, Player player2, boolean displayMessage){
		this.player1 = player1;
		this.player2 = player2;
		this.displayMessage = displayMessage;
		this.numWinsOfFirstPlayer = INITIAL_NUM_OF_WINS;
		this.numWinsOfFirstPlayer = INITIAL_NUM_OF_WINS;
		this.roundsMade = INITIAL_ROUNDS_MADE;
	}
	/**
	 * If playerPosition = 1, the results of the first player is returned. 
	 * If playerPosition = 2, the result of the second player is returned. 
	 * If playerPosition equals neiter, -1 is returned.
	 * @param playerPosition - 1 or 2.
	 * @return the number of victories of a player.
	 */
	public int getPlayerScore(int playerPosition){
		if (playerPosition == FIRST_PLAYER){
			return this.numWinsOfFirstPlayer;
		}
		else if (playerPosition == SECOND_PLAYER){
			return this.numWinsOfSecondPlayer;
		}
		else{
			return ILLEGAL_PLAYER_POSITION;
		}
	}
	
	/**
	 * Run the game for the given number of rounds.
	 * @param numberOfRounds - number of rounds to play.
	 */
	public void playMultipleRounds(int numberOfRounds){
		while (this.roundsMade < numberOfRounds){
			playRound();
		}
		System.out.println(RESULTS_MESSAGE + this.numWinsOfFirstPlayer + 
				COLON + this.numWinsOfSecondPlayer);		
	}
	/*
	 * Help method for play multipleRounds.
	 * This method play one round.
	 */
	private void playRound(){
		Player currentPlayer = this.player1;
		Board myBoard = new Board();
		if (this.displayMessage){
			System.out.println(WELCOM_MESSAGE);
		}
		while (myBoard.getNumberOfUnmarkedSticks() > NO_MORE_STICKS){
			currentPlayer = playTurn(currentPlayer, myBoard, INITIAL_NUM_OF_INVALID_MOVE);
		}
		if (currentPlayer == this.player1){
			this.numWinsOfFirstPlayer++;
		}
		else{
			this.numWinsOfSecondPlayer++;
		}
		if (this.displayMessage){
			System.out.println(PLAYER +currentPlayer.getPlayerId() +
					WON_MESSAGE);
		}
		this.roundsMade++;
	}
	
	/*
	 * This method helps to the method play round.
	 * This method play one turn.
	 * @param player - the player that will play his turn.
	 * @param myboard - the board on which the game is played.
	 * @return the next player to play.
	 */
	private Player playTurn(Player player, Board myBoard, int numInvalidMove){
		if (this.displayMessage && numInvalidMove == INITIAL_NUM_OF_INVALID_MOVE){
			System.out.println(PLAYER + player.getPlayerId() + 
					IT_IS_YOUR_TURN_MESSAGE);
		}
		Move playerMove = player.produceMove(myBoard);
		int is_move_ocurred = myBoard.markStickSequence(playerMove);
		if (is_move_ocurred != LEGAL_MOVE){
			if (this.displayMessage){
				System.out.println(INVALID_MOVE_MESSAGE);
			}
			numInvalidMove++;
			playTurn(player, myBoard, numInvalidMove);
		}
		else if (this.displayMessage){
			System.out.println(PLAYER +player.getPlayerId() +
					MADE_THE_MOVE_MESSAGE + playerMove);
		}
		if (player == this.player1){
			player = this.player2;
		}
		else{
			player = this.player1;
		}
		return player;
	}
	/*
	 * Returns the integer representing the type of the player; returns -1 
	 * on bad input.
	 */
	private static int parsePlayerType(String[] args,int index){
		try{
			return Integer.parseInt(args[index]);
		} catch (Exception E){
			return -1;
		}
	}
	
	/*
	 * Returns the integer representing the number of rounds; returns -1 on bad
	 * input.
	 */
	private static int parseNumberOfGames(String[] args){
		try{
			return Integer.parseInt(args[INDEX_ARGU_NUM_OF_ROUNDS]);
		} catch (Exception E){
			return -1;
		}
	}

	/**
	 * The method runs a Nim competition between two players according to 
	 * the three user-specified arguments. 
	 * (1) The type of the first player, which is a positive integer between 
	 * 1 and 4: 1 for a Random computer
	 *     player, 2 for a Heuristic computer player, 3 for a Smart computer 
	 *     player and 4 for a human player.
	 * (2) The type of the second player, which is a positive integer between 
	 * 1 and 4.
	 * (3) The number of rounds to be played in the competition.
	 * @param args an array of string representations of the three input 
	 * arguments, as detailed above.
	 */
	public static void main(String[] args) {
		
		int p1Type = parsePlayerType(args,INDEX_ARGU_TYPE_OF_FIRST_PLAYER);
		int p2Type = parsePlayerType(args,INDEX_ARGU_TYPE_OF_SECOND_PLAYER);
		int numGames = parseNumberOfGames(args);
		Scanner scanner = new Scanner(System.in);
		Player player1 = new Player(p1Type, FIRST_PLAYER, scanner);
		Player player2 = new Player(p2Type, SECOND_PLAYER, scanner);
		boolean displayMessage;
		if(player1.getPlayerType() == Player.HUMAN || 
				player2.getPlayerType() == Player.HUMAN){
			displayMessage = true;
		}
		else{
			displayMessage = false;
		}
		Competition myCompetition = new Competition(player1, player2, displayMessage);
		System.out.println(STARTING_GAME_MESSAGE_PART1 + numGames + 
						STARTING_GAME_MESSAGE_PART2 + player1.getTypeName() + 
						STARTING_GAME_MESSAGE_PART3 + player2.getTypeName() + 
						STARTING_GAME_MESSAGE_PART4);
		myCompetition.playMultipleRounds(numGames);
	}	
}
