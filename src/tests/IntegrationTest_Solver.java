package tests;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.concurrent.TimeUnit;

import main.Solver;
import main.Coord;
import main.Cell;

public class IntegrationTest_Solver {

	@Test
	public void testInitialize() {
		try {
			Solver j = new Solver("false");
			assertNotNull(j);
		}
		catch(Exception e) {
			assert(false);
		}
	}
	
	@Test
	public void testSetCells() {
		Solver board = new Solver("false");
		
		board.setCell(new Coord(0, 0), 1);
		board.setCell(new Coord(1, 1), 2);
		board.setCell(new Coord(2, 2), 3);
		board.setCell(new Coord(3, 3), 4);
		board.setCell(new Coord(4, 4), 5);
		board.setCell(new Coord(5, 5), 6);
		board.setCell(new Coord(6, 6), 7);
		board.setCell(new Coord(7, 7), 8);
		board.setCell(new Coord(8, 8), 9);
		board.setCell(new Coord(5, 4), 50);
		board.setCell(new Coord(20, 20), 5);
		board.setCell(new Coord(20, 20), 50);
		
		Cell[][] table = board.getTable();
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				if (i == j) {
					assertTrue(table[i][j].getSet());
					int currentVal = table[i][j].list().peek();
					assertEquals(i+1, currentVal);
				}
				else {
					assertFalse(table[i][j].getSet());
				}
			}
		}
	}
	
	@Test
	public void testClearCells() {
		Solver board = new Solver("false");
		
		board.setCell(new Coord(0, 0), 1);
		board.clearCell(new Coord(0, 0));
		board.clearCell(new Coord(0, 0));
		board.clearCell(new Coord(20, 20));
		
		Cell[][] table = board.getTable();
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				assertFalse(table[i][j].getSet());
			}
		}
	}
	
	@Test
	public void testGraphic() {
		Solver board = new Solver("false");
		
		board.setCell(new Coord(0, 0), 1);
		board.setCell(new Coord(1, 1), 2);
		board.setCell(new Coord(2, 2), 3);
		board.setCell(new Coord(3, 3), 4);
		board.setCell(new Coord(4, 4), 5);
		board.setCell(new Coord(5, 5), 6);
		board.setCell(new Coord(6, 6), 7);
		board.setCell(new Coord(7, 7), 8);
		board.setCell(new Coord(8, 8), 9);
		board.repainting();
		
		// VISUAL INSPECTION
		// The diagonal should contain numbers 1-9
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			assert(false);
		}
		assert(true);
		
		board.toggleHelp();
		board.repainting();
		
		// VISUAL INSPECTION
		// The diagonal should contain numbers 1-9 and hints should be shown
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			assert(false);
		}
		assert(true);
	}
	
	@Test
	public void testReset() {
		Solver board = new Solver("false");
		
		board.setCell(new Coord(0, 0), 1);
		board.setCell(new Coord(1, 1), 2);
		board.setCell(new Coord(2, 2), 3);
		board.setCell(new Coord(3, 3), 4);
		board.setCell(new Coord(4, 4), 5);
		board.setCell(new Coord(5, 5), 6);
		board.setCell(new Coord(6, 6), 7);
		board.setCell(new Coord(7, 7), 8);
		board.setCell(new Coord(8, 8), 9);
		
		board.reset();
		
		Cell[][] table = board.getTable();
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				assertFalse(table[i][j].getSet());
				assertEquals(0, table[i][j].list().size());
			}
		}
	}
	
	@Test
	public void testSolve() {
		Solver board = new Solver("false");
		
		board.setCell(new Coord(0, 0), 1);
		board.setCell(new Coord(1, 1), 2);
		board.setCell(new Coord(2, 2), 3);
		board.setCell(new Coord(3, 3), 4);
		board.setCell(new Coord(4, 4), 5);
		board.setCell(new Coord(5, 5), 6);
		board.setCell(new Coord(6, 6), 7);
		board.setCell(new Coord(7, 7), 8);
		board.setCell(new Coord(8, 8), 9);
		
		board.solve();
		
		Cell[][] table = board.getTable();
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				assertTrue(table[i][j].getSet());
				assertEquals(1, table[i][j].list().size());
			}
		}
		
		board.repainting();
		// VISUAL INSPECTION
		// The diagonal should contain numbers 1-9, rest of the board should be solved
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			assert(false);
		}
		assert(true);
	}
}
