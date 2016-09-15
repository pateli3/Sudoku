import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class Solver{
	boolean debug = true;		// prints lots of messages of what's happening

	int mazesize;
	int mazescale;
	int xoffset;
	int yoffset;
	
	// The GUI and class to handle GUI actions
	JFrame frame = new JFrame("Sudoku Solver");
	BoardPanel boardPanel;
	
	Coord currentSquare;			// record square mouse is currently in
	Coord pickedSquare;				// record the square the user last clicked
	boolean solved = false;			// is the puzzle solved or not
	boolean toggleHelp = true;		// a toggle to show possible options for a cell
	LinkedList<Move> history;		// a history (used as a stack) of all actions that occur while solving
	TreeNode<Move> guessHistory;	// root of a general tree of all guesses that occur
	TreeNode<Move> currentNode;		// used to move around the general tree
	int iterations = 0;
	boolean started = false;
	
	// The board's representation
	private Cell[][] table = new Cell[9][9];
	
	// Detects mouse actions
	MouseAdapter mousey = new MouseAdapter() {
		//For selecting a square to put a number in
		public void mousePressed(MouseEvent e)
		{
			if (SwingUtilities.isLeftMouseButton(e))
			{
				int x = (int)Math.floor((e.getX()-74)/60);
				int y = (int)Math.floor((e.getY()-96)/60);
			
				if (x < 0 || x > 8 || y < 0 || y > 8) {
					pickedSquare = null;
				}
				else {
					pickedSquare = new Coord(x, y);
				}
			}
		}
		
		//For highlighting what square the mouse is in
		public void mouseMoved(MouseEvent e)
		{
			int x = (int)(e.getX()-74)/60;
			int y = (int)(e.getY()-96)/60;
			
			if (x < 0 || x >8 || y < 0 || y > 8) {
				currentSquare = null;
			}
			else {
				currentSquare = new Coord(x, y);
				repainting();
			}
		}
	};

	// Detects keyboard actions
	KeyListener keys = new KeyAdapter()	{
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// Solve
				if (!solved) {
					if (!started) {
						saveState("temp.txt");
						started = true;
					}
					solve();
				}
			}
			else if (e.getKeyCode() == KeyEvent.VK_H) {
				// Toggle the helpful little numbers
				toggleHelp = !toggleHelp;
			}
			else if (e.getKeyCode() == KeyEvent.VK_U) {
				// Update the help thingys
				search();
			}
			else if (e.getKeyCode() == KeyEvent.VK_S) {
				// Save the current board
				saveState("save.txt");
			}
			else if (e.getKeyCode() == KeyEvent.VK_L) {
				// Load in a saved board
				loadState("save.txt");
			}
			else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				// Reset the board
				started = false;
				reset();
			}
			if (!solved) {
				// Placing a number in a selected cell
				if (pickedSquare != null) {
					if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
						table[pickedSquare.X()][pickedSquare.Y()].list().clear();				
						table[pickedSquare.X()][pickedSquare.Y()].list().add(1);
						table[pickedSquare.X()][pickedSquare.Y()].set();
					}
					else if (e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
						table[pickedSquare.X()][pickedSquare.Y()].list().clear();				
						table[pickedSquare.X()][pickedSquare.Y()].list().add(2);
						table[pickedSquare.X()][pickedSquare.Y()].set();
					}
					else if (e.getKeyCode() == KeyEvent.VK_3 || e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
						table[pickedSquare.X()][pickedSquare.Y()].list().clear();				
						table[pickedSquare.X()][pickedSquare.Y()].list().add(3);
						table[pickedSquare.X()][pickedSquare.Y()].set();
					}
					else if (e.getKeyCode() == KeyEvent.VK_4 || e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
						table[pickedSquare.X()][pickedSquare.Y()].list().clear();				
						table[pickedSquare.X()][pickedSquare.Y()].list().add(4);
						table[pickedSquare.X()][pickedSquare.Y()].set();
					}
					else if (e.getKeyCode() == KeyEvent.VK_5 || e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
						table[pickedSquare.X()][pickedSquare.Y()].list().clear();				
						table[pickedSquare.X()][pickedSquare.Y()].list().add(5);
						table[pickedSquare.X()][pickedSquare.Y()].set();
					}
					else if (e.getKeyCode() == KeyEvent.VK_6 || e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
						table[pickedSquare.X()][pickedSquare.Y()].list().clear();
						table[pickedSquare.X()][pickedSquare.Y()].list().add(6);
						table[pickedSquare.X()][pickedSquare.Y()].set();
					}
					else if (e.getKeyCode() == KeyEvent.VK_7 || e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
						table[pickedSquare.X()][pickedSquare.Y()].list().clear();				
						table[pickedSquare.X()][pickedSquare.Y()].list().add(7);
						table[pickedSquare.X()][pickedSquare.Y()].set();
					}
					else if (e.getKeyCode() == KeyEvent.VK_8 || e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
						table[pickedSquare.X()][pickedSquare.Y()].list().clear();				
						table[pickedSquare.X()][pickedSquare.Y()].list().add(8);
						table[pickedSquare.X()][pickedSquare.Y()].set();
					}
					else if (e.getKeyCode() == KeyEvent.VK_9 || e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
						table[pickedSquare.X()][pickedSquare.Y()].list().clear();				
						table[pickedSquare.X()][pickedSquare.Y()].list().add(9);
						table[pickedSquare.X()][pickedSquare.Y()].set();
					}
					else if (e.getKeyCode() == KeyEvent.VK_0 || e.getKeyCode() == KeyEvent.VK_NUMPAD0) {
						// Clear the selected cell
						table[pickedSquare.X()][pickedSquare.Y()].list().clear();
						table[pickedSquare.X()][pickedSquare.Y()].reset();
					}
				}
			}
			
			// Update GUI
			repainting();
		}
	};
	
	// Constructor, start a new board and GUI
	public Solver(String debug) {
		// Use the command line argument to set the mode
		this.debug = Boolean.parseBoolean(debug);
		
		// I determined this stuff from a previous project, the maze generator
		mazesize = 9;
		mazescale = 60;
		yoffset = 0;
		xoffset = 0;
	    
		//Instantiates all the Cells in the table.
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				table[i][j] = new Cell();
			}
		}
		history = new LinkedList<Move>();
		guessHistory = new TreeNode<Move>();
		currentNode = guessHistory;
		
		// New GUI
		boardPanel = new BoardPanel();
		
		//Create main frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.white);
		frame.setForeground(Color.black);
		frame.setLayout(new FlowLayout());
		frame.add(boardPanel);
		frame.pack();
		frame.setVisible(true);
		frame.addMouseListener(mousey);
		frame.addMouseMotionListener(mousey);
		frame.addKeyListener(keys);
		
		// Print some instructions
		System.out.println("INSTRUCTIONS");
		System.out.println("Click a square and type a number to enter in a sudoku.");
		System.out.println("A cell that is already set can be changed.");
		System.out.println("'0' can be used to clear a cell.");
		System.out.println("'Enter' will solve the puzzle. 'Space' will reset it.");
		System.out.println("Use 's' to save the current state of the grid.");
		System.out.println("Use 'l' to load in a saved grid.");
		System.out.println("Use space before loading though, cause I'm lazy.");
		System.out.println("'h' shows hints, the possible options for each square.");
		System.out.println("Use 'u' to update the hints after you enter numbers yourself.");
	}
	
	// Solve the sudoku
	public void solve() {
		// Loop until done
		while (!solved) {
			if (debug)
				System.out.println("run");
			
			// Count the number of times this loop runs. Will reset board if iterations get too high
			iterations++;
			int counter = 0;
			boolean undoNeeded = false;
			boolean placedSomething = false;
			
			// Determine options for the current board
			search();
			
			// Place and routing
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (table[i][j].getSet())
						counter++;
					else {
						if (table[i][j].list().size() == 0) {
							undoNeeded = true;
						}
						else if (table[i][j].list().size() == 1) {
							// Then set this square, because it only has 1 possible option and thus is known
							table[i][j].set();
							counter++;	
							if (debug)
								System.out.println("Placing " + table[i][j].list().getFirst() + " at location " + i + ", " + j);
							placedSomething = true;
							
							// Add to move history
							history.push(new Move(new Coord(i, j), table[i][j].list().getFirst(), false));
							search();		// update other cells before another placement occurs
						}
					}
				}
			}
			
			// Human-like checks, as in within this column/row/box a number can ONLY go in 1 location
			// Column check
			for (int i = 0; i < 9; i++) {
				// Find values already set in column
				LinkedList<Integer> setValues = new LinkedList<Integer>();
				for (int j = 0; j < 9; j++) {
					if (table[i][j].getSet())
						setValues.add(table[i][j].list().getFirst());
				}
				
				// For each number 1-9, only work with numbers not already in the column
				for (int n = 1; n <= 9; n++) {
					if (!setValues.contains(n)) {
						// Count how many boxes in the column have n as a possible option
						int timesFound = 0;
						Coord first = null;
						for (int j = 0; j < 9; j++) {
							if (table[i][j].list().contains(n)) {
								timesFound++;
								if (first == null)
									first = new Coord(i, j);
							}
						}
						
						// If only 1 box has n as an option, set that box
						if (timesFound == 1) {
							table[first.X()][first.Y()].set();
							table[first.X()][first.Y()].list().clear();
							table[first.X()][first.Y()].list().add(n);
							counter++;	
							if (debug)
								System.out.println("Placing " + n + " via column intelligence at location " + first.X() + ", " + first.Y());
							placedSomething = true;
							history.push(new Move(new Coord(first.X(), first.Y()), n, false));	// save the placement
						}
					}
				}				
			}
			
			// Row check
			for (int j = 0; j < 9; j++) {
				// Find values already set in row
				LinkedList<Integer> setValues = new LinkedList<Integer>();
				for (int i = 0; i < 9; i++) {
					if (table[i][j].getSet())
						setValues.add(table[i][j].list().getFirst());
				}
								
				// For each number 1-9, only work with numbers not already in the column
				for (int n = 1; n <= 9; n++) {
					if (!setValues.contains(n)) {
						// Count how many boxes in the row have n as a possible option
						int timesFound = 0;
						Coord first = null;
						for (int i = 0; i < 9; i++) {
							if (table[i][j].list().contains(n)) {
								timesFound++;
								if (first == null)
									first = new Coord(i, j);
							}
						}
						
						// If only 1 box has n as an option, set that box
						if (timesFound == 1) {
							table[first.X()][first.Y()].set();
							table[first.X()][first.Y()].list().clear();
							table[first.X()][first.Y()].list().add(n);
							counter++;	
							if (debug)
								System.out.println("Placing " + n + " via row intelligence at location " + first.X() + ", " + first.Y());
							placedSomething = true;
							history.push(new Move(new Coord(first.X(), first.Y()), n, false));
						}
					}
				}				
			}
			
			// Box check
			for (int startX = 0; startX < 9; startX += 3) {
				for (int startY = 0; startY < 9; startY += 3) {
					// Find values already set in box
					LinkedList<Integer> setValues = new LinkedList<Integer>();
					for (int i = startX; i < startX+3; i++) {
						for (int j = startY; j < startY+3; j++) {
							if (table[i][j].getSet())
								setValues.add(table[i][j].list().getFirst());
						}
					}
					
					// For each number 1-9, only work with numbers not already in the column
					for (int n = 1; n <= 9; n++) {				
						if (!setValues.contains(n)) {
						// Count how many cells in the box have n as a possible option
							int timesFound = 0;
							Coord first = null;
							for (int i = startX; i < startX+3; i++) {
								for (int j = startY; j < startY+3; j++) {
									if (table[i][j].list().contains(n)) {
										timesFound++;
										if (first == null)
											first = new Coord(i, j);
									}
								}
							}
													
							// If only 1 box has n as an option, set that box
							if (timesFound == 1) {
								table[first.X()][first.Y()].set();
								table[first.X()][first.Y()].list().clear();
								table[first.X()][first.Y()].list().add(n);
								counter++;	
								if (debug)
									System.out.println("Placing " + n + " via box intelligence at location " + first.X() + ", " + first.Y());
								placedSomething = true;
								history.push(new Move(new Coord(first.X(), first.Y()), n, false));
							}
						}
					}
				}
			}
			
			// Determine what action to take next
			if (undoNeeded) {
				undo();				// undoing takes priority
			}
			else if (!placedSomething)
				guess();			// couldn't make any cells certain on this iteration, so make a guess
			else if (counter == 81)	{
				solved = true;		// done
				if (debug)
					System.out.println("Solved! " + iterations + " loops needed.");
				else
					System.out.println("Solved!");
			}
			// Else placements were made and we're not done, so continue loop
			
			// Sometimes the guessing just starts off badly. Try again from beginning if it goes on too long.
			if (iterations == 500) {
				reset();
				loadState("temp.txt");
			}
			if (debug)
				break;	// only do 1 iteration per press of the Enter key
		}
	}
	
	// Look through grid to determine possibilities for each box
	public void search() {
		// Go through each cell
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (!table[x][y].getSet()) {
					LinkedList<Integer> invalids = new LinkedList<Integer>();	// a list of numbers that CANNOT go in this cell
				
					// Check row
					for (int i = 0; i < 9; i++) {
						if (table[i][y].getSet()) {
							invalids.add(table[i][y].list().getFirst());
						}
					}
					
					// Check column
					for (int j = 0; j < 9; j++) {
						if (table[x][j].getSet()) {
							invalids.add(table[x][j].list().getFirst());
						}
					}
					
					// Check box
					for (int i = x-x%3; i < x-x%3+3; i++) {
						for (int j = y-y%3; j < y-y%3+3; j++) {
							if (table[i][j].getSet()) {
								invalids.add(table[i][j].list().getFirst());
							}
						}
					}
				
					// Generate list of possible values for this cell based on invalid values
					table[x][y].list().clear();
					for (int n = 1; n <= 9; n++) {
						if (!invalids.contains(n))
							table[x][y].list().add(n);
					}
				}
			}
		}
	}
	
	// Pick a random square that is not set and give it a value from its options
	public void guess() {
		// Remove bad guesses at current level from guesses
		LinkedList<TreeNode<Move>> badGuesses = currentNode.children();
		for (int z = 0; z < badGuesses.size(); z++) {
			Move currentMove = badGuesses.get(z).data();
			int index = table[currentMove.location().X()][currentMove.location().Y()].list().indexOf(currentMove.value());
			table[currentMove.location().X()][currentMove.location().Y()].list().remove(index);
		}
		
		// Check if there are any cell with no options
		boolean undoNeeded = false;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (table[i][j].list().size() == 0) {
					undoNeeded = true;
				}
			}
		}
		
		// Call undo if there is a cell with no options
		if (undoNeeded) {
			undo();
		}
		// Otherwise continue to make a guess. Only 1 guess will occur per round
		else {
			Random rn = new Random();
			boolean done = false;
			while (!done) {
				// Pick random square, repeat if square chosen already set
				int x = rn.nextInt(9) + 0;
				int y = rn.nextInt(9) + 0;
				if (!table[x][y].getSet()) {
					int rand = rn.nextInt(table[x][y].list().size()) + 0;
					int value = table[x][y].list().get(rand);
					table[x][y].list().clear();
					table[x][y].list().add(value);
					table[x][y].set();
					if (debug)
						System.out.println("Guessing " + value + " at location " + x + ", " + y);

					// add to move history and exit loop
					Move newMove = new Move(new Coord(x, y), table[x][y].list().getFirst(), true);
					history.push(newMove);
					currentNode.add(newMove);
					currentNode = currentNode.child();
					done = true;
				}
			}
		}
	}
	
	// Go backwards through "history" to undo moves. Stop at the latest guess
	public void undo() {
		Move currentMove = history.pop();
		// Erase placements in the stack, if any
		while (!currentMove.guess()) {
			Coord location = currentMove.location();
			table[location.X()][location.Y()].reset();
			table[location.X()][location.Y()].list().clear();
			if (debug)
				System.out.println("Erasing placement " + currentMove.value() + " from location " + location.X() + ", " + location.Y());
			currentMove = history.pop();
		}
		
		// "currentMove" is the latest guess right now, so erase it from the board, then move up 1 in the tree of guesses
		Coord location = currentMove.location();
		table[location.X()][location.Y()].reset();
		table[location.X()][location.Y()].list().clear();
		if (debug)
			System.out.println("Erasing guess " + currentMove.value() + " from location " + location.X() + ", " + location.Y());
		
		// Move up 1 level in the tree of guesses
		currentNode = currentNode.parent();
	}
	
	// Clear out all numbers on the board
	public void reset() {
		// Reset each cell
		for (int i = 0; i < mazesize; i++) {
			for (int j = 0; j < mazesize; j++) {
				table[i][j].list().clear();
				table[i][j].reset();
			}
		}
		
		// Reset variables
		solved = false;
		history.clear();
		guessHistory = new TreeNode<Move>();
		currentNode = guessHistory;
		iterations = 0;
	}
	
	// Save the current state of the board to a text file
	public void saveState(String filename) {
		PrintWriter out;
		try {
			out = new PrintWriter(new File(filename));
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					// Only write out set cells
					if (table[i][j].getSet()) {
						// For each cell write out a line of location and value to the file
						out.println(i + " " + j + " " + table[i][j].list().get(0));
					}
				}
			}
			out.close();
		}
		catch (IOException e) {
			System.out.println("The principal is death!");
		}
		
	}
	
	// Load a saved board state
	public void loadState(String filename) {
		Scanner scan;
		try {
			scan = new Scanner(new File(filename));
			while (scan.hasNext()) {
				// Read the file and set the board based on these values
				String temp = scan.nextLine();
				int i = temp.charAt(0)-48;
				int j = temp.charAt(2)-48;
				int value = temp.charAt(4)-48;
				table[i][j].set();
				table[i][j].list().clear();
				table[i][j].list().add(value);
			}
		}
		catch (IOException e) {
			System.out.println("No saved file.");
		}
	}
	
	//Small method to update the main frame
	public void repainting() {
		frame.repaint(1, -100, -100, 1000, 1000);
	}
	
	//Inner class for the Sudoku Board Frame
	class BoardPanel extends JPanel {
		public Dimension getPreferredSize() {
			return new Dimension(680, 650);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			//Draw square mouse is hovering in
			if (currentSquare != null) {
				g2.setColor(new Color(255, 0, 175, 100)); //pink
				g2.fillRect((int)(currentSquare.X()*60+60), (int)currentSquare.Y()*60+60, 60, 60);
			}

			// Draw horizontal lines in a stupid fashion
			for (int i = 0; i <= mazesize; i++) {
				for (int j = 1; j <= mazesize; j++) {			
					g2.setColor(Color.black);
					if (i%3 == 0)
						g2.setStroke(new BasicStroke(5));
					else
						g2.setStroke(new BasicStroke(1));
					g2.drawLine(mazescale+mazescale*i+xoffset, mazescale*j+yoffset, mazescale+mazescale*i+xoffset, mazescale*j+mazescale+yoffset);
				}
			}
			
			// Draw vertical lines in a stupid fashion
			for (int i = 1; i <= mazesize; i++) {
				for (int j = 0; j <= mazesize; j++) {		
					if (j%3 == 0)
						g2.setStroke(new BasicStroke(5));
					else
						g2.setStroke(new BasicStroke(1));
					g2.setColor(Color.black);
					g2.drawLine(mazescale*i+xoffset, mazescale+mazescale*j+yoffset, mazescale*i+mazescale+xoffset,mazescale+mazescale*j+yoffset);    
				}
			}
						
			// Draw numbers
			for (int i = 0; i < mazesize; i++) {
				for (int j = 0; j < mazesize; j++) {
					// Draw the numbers for cells that are set
					if (table[i][j].getSet()) {
						Font font = new Font("Serif", Font.PLAIN, 48);
						g2.setFont(font);
						g2.drawString(String.valueOf(table[i][j].list().getFirst()), mazescale+mazescale*i+18, mazescale+mazescale*j+47);
					}
					else {
						if (toggleHelp) {
							// If help is on, draw possibility numbers for cells that are not set
							Font font = new Font("Serif", Font.PLAIN, 12);
							g2.setFont(font);
							int xCoord = mazescale+mazescale*i+12;
							int yCoord = mazescale+mazescale*j+22;
							int tempCount = 0;
							for (int k = 0; k < table[i][j].list().size(); k++) {
								g2.drawString(String.valueOf(table[i][j].list().get(k)), xCoord, yCoord);
								tempCount++;
								if (tempCount == 3) {
									// reset to left edge
									xCoord = mazescale+mazescale*i+12;
									yCoord += 15;
									tempCount = 0;
								}
								else {
									// move to right
									xCoord += 15;
								}
							}
						}
					}
				}
			}
		}
	}
}