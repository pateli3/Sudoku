import java.util.*;

// Represents a cell in the sudoku board
public class Cell {
	// "list" stores the value in a set cell or the options a cell can have
	private LinkedList<Integer> list;
	
	// Records whether a cell has been set or not
	private boolean set;

	public Cell () {
		list = new LinkedList<Integer>();
		set = false;
	}
	
	// Just allow the user class to play with the object's list
	public LinkedList<Integer> list() {
		return list;
	}
	
	public void set() {
		set = true;
	}
	
	public void reset() {
		set = false;
	}
	
	public boolean getSet() {
		return set;
	}
}