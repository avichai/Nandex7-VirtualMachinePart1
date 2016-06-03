package ex1;

/**
 * The Move class represents a move in the Nim game by a player. A move 
 * consists of the row on which it is applied, the left bound (inclusive) 
 * of the sequence of sticks to mark, and the right bound (inclusive) of the 
 * same sequence.
 * @author Avichai
 */
public class Move {
	
	private int inRow; // The row on which the move is performed.
	private int inLeft; // The left bound of the sequence to mark.
	private int inRight; // The right bound of the sequence to mark.
	/**
	 * Constructs a Move object with the given parameters
	 * @param inRow - The row on which the move is performed.
	 * @param inLeft - The left bound of the sequence to mark.
	 * @param inRight - The right bound of the sequence to mark.
	 */
	public Move(int inRow, int inLeft, int inRight) {
		this.inRow = inRow;
		this.inLeft = inLeft;
		this.inRight = inRight;
	}
	
	/**
	 * Overrides toString in class java.lang.Object
	 * @return a string representation of the move. For example, if the row 
	 * is 2, the left bound of the sequence is 3 and the right bound is 5, 
	 * this function will return the string "2:3-5" (without any spaces).
	 */
	public java.lang.String toString(){
		return this.inRow + ":" + this.inLeft + "-" + this.inRight;
	}
	
	/**
	 * 
	 * @return The row on which the move is performed.
	 */
	public int getRow(){
		return this.inRow;
	}
	/**
	 * 
	 * @return The left bound of the stick sequence to mark.
	 */
	public int getLeftBound(){
		return this.inLeft;
	}
	/**
	 * 
	 * @return The right bound of the stick sequence to mark.
	 */
	public int getRightBound(){
		return this.inRight;
	}

}
