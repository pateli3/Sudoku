import java.util.*;

// Represents a move that occurs while solving the sudoku
// Primarily used to keep a history of what happens during the program's operation
public class Move {
	// Each move consists of the cell being set, the value put in the cell, and whether the value was a guess or not
	private boolean guess;
	private Coord location;
	private int value;

	public Move (Coord location, int value, boolean guess) {
		this.location = location;
		this.value = value;
		this.guess = guess;
	}
	
	public Coord location() {
		return location;
	}
	
	public int value() {
		return value;
	}
	
	public boolean guess() {
		return guess;
	}
}