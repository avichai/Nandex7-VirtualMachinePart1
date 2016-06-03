package ex1;

import java.util.Random;
import java.util.Scanner;

/**
 * The Player class represents a player in the Nim game, producing Moves as a 
 * response to a Board state. Each player is initialized with a type, either 
 * human or one of several computer strategies, which defines the move he 
 * produces when given a board in some state. The heuristic strategy of the 
 * player is already implemented. You are required to implement the rest of 
 * the player types according to the exercise description.
 * @author Avichai
 */
public class Player {

	//Constants that represent the different players.
	/** The constant integer representing the Random player type. */
	public static final int RANDOM = 1;
	/** The constant integer representing the Heuristic player type. */
	public static final int HEURISTIC = 2;
	/** The constant integer representing the Smart player type. */
	public static final int SMART = 3;
	/** The constant integer representing the Human player type. */
	public static final int HUMAN = 4;
	
	//Used by produceHeuristicMove() for binary representation of board rows.
	private static final int BINARY_LENGTH = 3;
	
	// The player type(human random ..)
	private final int playerType;
	// The player Id (1 or 2).
	private final int playerId;
	//The Scanner object through which to get user input for the Human player type. 
	private Scanner scanner;

	//The first question for the human player when it is his turn.
	private static final String QUES1_FOR_HUMAN_PLAYER = 
			"Press 1 to display the board. Press 2 to make a move:";
	// The second question for the human player when it is his turn.
	private static final String QUES2_FOR_HUMAN_PLAYER = "Enter the row number:";
	private static final String QUES3_FOR_HUMAN_PLAYER = 
			"Enter the index of the leftmost stick:";
	// The third question for the human player when it is his turn.
	private static final String QUES4_FOR_HUMAN_PLAYER = 
			"Enter the index of the rightmost stick:";
	// The message when a human player enters unknown input.
	private static final String UNKNOWN_INPUT = "Unknown input.";
	
	// constant a human enters when he wants to see the current board.
	private static final int SHOW_BOARD = 1;
	// constant a human enter when he to make a move.
	private static final int MAKE_A_MOVE = 2;
	
	// constant for when the program don't get the expected player type.
	private static final String UNKNOWN_PLAYER_TYPE = "Received an unknown "
			+ "player type as a parameter in Player constructor. Terminating.";
	// constant for a string that represent a random player.
	private static final String RANDOM_STRING = "Random";
	// constant for a string that represent a smart player.
	private static final String SMART_STRING = "Smart";
	// constant for a string that represent a heuristic player.
	private static final String Heuristic_STRING = "Heuristic";
	// constant for a string that represent a human player.
	private static final String HUMAN_STRING = "Human";
	/* 
	 * another constant for when the program don't get the expected player type.
	 * we don't actually use this constant since we already validate we
	 * know the type of the player in the constructor.
	 */
	private static final String UNKNOWN_PLAYER_TYPE_NOT_USED = "UnkownPlayerType";
	// default for a row with stick used by the smart player.
	private static int DEFAULT_ROW_WITH_STICKS = 1;
	// constant for a when there are even unmarked sticks left on the board.
	private static int EVEN_UNMARKED_STICKS = 0;
	// default for a unmarked stick used by the smart player.
	private static int DEFAULT_SURE_STICK = 1;

	

	
	/**
	 * Initializes a new player of the given type and the given id, and an 
	 * initialized scanner.
	 * @param type The type of the player to create.
	 * @param id The id of the player (either 1 or 2).
	 * @param inputScanner The Scanner object through which to get user input
	 * for the Human player type. 
	 */
	public Player(int type, int id, Scanner inputScanner){		
		// Check for legal player type (we will see better ways to do this in the future).
		if (type != RANDOM && type != HEURISTIC 
				&& type != SMART && type != HUMAN){
			System.out.println(UNKNOWN_PLAYER_TYPE);
			System.exit(-1);
		}		
		this.playerType = type;	
		this.playerId = id;
		this.scanner = inputScanner;
	}
	
	/**
	 * @return an integer matching the player type.
	 */	
	public int getPlayerType(){
		return this.playerType;
	}
	
	/**
	 * @return the players id number.
	 */	
	public int getPlayerId(){
		return this.playerId;
	}
	
	
	/**
	 * @return a String matching the player type.
	 */
	public String getTypeName(){
		switch(this.playerType){
			
			case RANDOM:
				return RANDOM_STRING;			    
	
			case SMART: 
				return SMART_STRING;	
				
			case HEURISTIC:
				return Heuristic_STRING;
				
			case HUMAN:			
				return HUMAN_STRING;
		}
		//Because we checked for legal player types in the
		//constructor, this line shouldn't be reachable.
		return UNKNOWN_PLAYER_TYPE_NOT_USED;
	}
	
	/**
	 * This method encapsulates all the reasoning of the player about the game. 
	 * The player is given the board object, and is required to return his next 
	 * move on the board. The choice of the move depends on the type of the 
	 * player: a human player chooses his move manually; the random player 
	 * should return some random move; the Smart player can represent any 
	 * reasonable strategy; the Heuristic player uses a strong heuristic to 
	 * choose a move. 
	 * @param board - a Board object representing the current state of the game.
	 * @return a Move object representing the move that the current player will 
	 * play according to his strategy.
	 */
	public Move produceMove(Board board){
		
		switch(playerType){
		
			case RANDOM:
				return produceRandomMove(board);				
				    
			case SMART: 
				return produceSmartMove(board);
				
			case HEURISTIC:
				return produceHeuristicMove(board);
				
			case HUMAN:
				return produceHumanMove(board);

			//Because we checked for legal player types in the
			//constructor, this line shouldn't be reachable.
			default: 
				return null;			
		}
	}
	
	/*
	 * Produces a random move.
	 * board - a nim board to make the random move on.
	 */
	private Move produceRandomMove(Board board){
		Random myRandom = new Random();
		int numRows =  board.getNumberOfRows();
		while (true){
			int randRow = myRandom.nextInt(numRows) + 1;
			int randLeftStick = myRandom.nextInt(board.getRowLength(randRow)) + 1;
			int randRightStick = myRandom.nextInt(board.getRowLength(randRow)) + 1;
			if (randLeftStick > randRightStick){
				randRightStick = randLeftStick;
			}
			if (chekIfValidMove(randLeftStick, randRightStick, randRow, board)){
				return new Move(randRow, randLeftStick, randRightStick);
			}
			else{
				return produceRandomMove(board);
			}
		}
	}
	
	/*
	 * Checking if the Move instance that will be produced 
	 * of the given arguments is valid in the given board.
	 */
	private boolean chekIfValidMove(int leftBoubd, int rightBound, int row, Board board){
		for (int i=leftBoubd; i<=rightBound; i++){
			if(!board.isStickUnmarked(row, i)){
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Produce some intelligent strategy to produce a move
	 * board - a nim board to make the smart move on.
	 */
	private Move produceSmartMove(Board board){
		int onlyRowWithSticks = DEFAULT_ROW_WITH_STICKS;
		for (int i=1; i<=board.getNumberOfRows();i++){
			if (!isEmptyRow(board, i)){
				onlyRowWithSticks = i;
			}
		}
		int numOfUnmarketStickes = board.getNumberOfUnmarkedSticks();
		if (numOfUnmarketStickes%2 == EVEN_UNMARKED_STICKS || numOfUnmarketStickes == 1){
			return pruduceSmartMoveForEvenSticksOnBoard(board, onlyRowWithSticks);
		}
		else{
			return pruduceSmartMoveForOddSticksOnBoard(board, onlyRowWithSticks);
		}	
	}
	
	/*
	 * creates a smart move when there is an even number of sticks on the board.
	 * board - a nim board to make the smart move on.
	 * row - a row on the board with remaining sticks.
	 */
	private Move pruduceSmartMoveForEvenSticksOnBoard(Board board, int row){
		for (int i=1; i<=board.getRowLength(row);i++){
			if (board.isStickUnmarked(row, i)){
				return new Move(row, i, i);
			}
		}
		return null;
	}
	/*
	 * creates a smart move when there is an odd number of sticks on the board.
	 * board - a nim board to make the smart move on.
	 * row - a row on the board with remaining sticks.
	 */
	private Move pruduceSmartMoveForOddSticksOnBoard(Board board, int row){
		int sureStick = DEFAULT_SURE_STICK;
		for (int i=1; i<=board.getRowLength(row);i++){
			if (board.isStickUnmarked(row, i)){
				sureStick = i;
				if(board.isStickUnmarked(row, i+1)){
					return new Move(row, i, i+1);
				}
			}
		}
		return new Move(row, sureStick, sureStick);
	}
	/*
	 * checks if a row on a board has no sticks.
	 * board - a nim board.
	 * row - a row on the board that needs to be checked.
	 */
	private boolean isEmptyRow(Board board, int row){
		for (int i=1; i<=board.getRowLength(row);i++){
			if (board.isStickUnmarked(row, i)){
				return false;
			}
		}
		return true;
	}

	
	/*
	 * Interact with the user to produce his move.
	 * board - a nim board to make the Human move on.
	 */
	private Move produceHumanMove(Board board){
		while (true){
			System.out.println(QUES1_FOR_HUMAN_PLAYER);
			int board_or_move = scanner.nextInt();
			if (board_or_move == SHOW_BOARD){
				System.out.println(board);
			}
			else if (board_or_move == MAKE_A_MOVE){
				System.out.println(QUES2_FOR_HUMAN_PLAYER);
				int rowNum = scanner.nextInt();
				System.out.println(QUES3_FOR_HUMAN_PLAYER);
				int leftMostStick = scanner.nextInt();
				System.out.println(QUES4_FOR_HUMAN_PLAYER);
				int rightMostStick = scanner.nextInt();
				return new Move(rowNum, leftMostStick, rightMostStick);
			}
			else{
				System.out.println(UNKNOWN_INPUT);
				continue;
			}
		}
	}
	
	/*
	 * Uses a winning heuristic for the Nim game to produce a move.
	 * board - a nim board to make the Heuristic move on.
	 */
	private Move produceHeuristicMove(Board board){

		if(board == null){
			return null;
		}
	
		int numRows = board.getNumberOfRows();
		int[][] bins = new int[numRows][BINARY_LENGTH];
		int[] binarySum = new int[BINARY_LENGTH];
		int bitIndex,higherThenOne=0,totalOnes=0,lastRow=0,lastLeft=0,lastSize=0,lastOneRow=0,lastOneLeft=0;
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
			binarySum[bitIndex] = 0;
		}
		
		for(int k=0;k<numRows;k++){
			
			int curRowLength = board.getRowLength(k+1);
			int i = 0;
			int numOnes = 0;
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				bins[k][bitIndex] = 0;
			}
			
			do {
				if(i<curRowLength && board.isStickUnmarked(k+1,i+1) ){
					numOnes++;
				} else {
					
					if(numOnes>0){
						
						String curNum = Integer.toBinaryString(numOnes);
						while(curNum.length()<BINARY_LENGTH){
							curNum = "0" + curNum;
						}
						for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
							bins[k][bitIndex] += curNum.charAt(bitIndex)-'0'; //Convert from char to int
						}
						
						if(numOnes>1){
							higherThenOne++;
							lastRow = k +1;
							lastLeft = i - numOnes + 1;
							lastSize = numOnes;
						} else {
							totalOnes++;
						}
						lastOneRow = k+1;
						lastOneLeft = i;
						
						numOnes = 0;
					}
				}
				i++;
			}while(i<=curRowLength);
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				binarySum[bitIndex] = (binarySum[bitIndex]+bins[k][bitIndex])%2;
			}
		}
		
		
		//We only have single sticks
		if(higherThenOne==0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//We are at a finishing state				
		if(higherThenOne<=1){
			
			if(totalOnes == 0){
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1) - 1);
			} else {
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1)-(1-totalOnes%2));
			}
			
		}
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH-1;bitIndex++){
			
			if(binarySum[bitIndex]>0){
				
				int finalSum = 0,eraseRow = 0,eraseSize = 0,numRemove = 0;
				for(int k=0;k<numRows;k++){
					
					if(bins[k][bitIndex]>0){
						eraseRow = k+1;
						eraseSize = (int)Math.pow(2,BINARY_LENGTH-bitIndex-1);
						
						for(int b2 = bitIndex+1;b2<BINARY_LENGTH;b2++){
							
							if(binarySum[b2]>0){
								
								if(bins[k][b2]==0){
									finalSum = finalSum + (int)Math.pow(2,BINARY_LENGTH-b2-1);
								} else {
									finalSum = finalSum - (int)Math.pow(2,BINARY_LENGTH-b2-1);
								}
								
							}
							
						}
						break;
					}
				}
				
				numRemove = eraseSize - finalSum;
				
				//Now we find that part and remove from it the required piece
				int numOnes=0,i=0;
				//while(numOnes<eraseSize){
				while(numOnes<numRemove && i<board.getRowLength(eraseRow)){

					if(board.isStickUnmarked(eraseRow,i+1)){
						numOnes++;
					} else {
						numOnes=0;
					}
					i++;
					
				}
				
				//This is the case that we cannot perform a smart move because there are marked
				//Sticks in the middle
				if(numOnes == numRemove){
					return new Move(eraseRow,i-numOnes+1,i-numOnes+numRemove);
				} else {
					return new Move(lastRow,lastLeft,lastLeft);
				}
				
			}
		}
		
		//If we reached here, and the board is not symmetric, then we only need to erase a single stick
		if(binarySum[BINARY_LENGTH-1]>0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//If we reached here, it means that the board is already symmetric,
		//and then we simply mark one stick from the last sequence we saw:
		return new Move(lastRow,lastLeft,lastLeft);		
	}		
}
