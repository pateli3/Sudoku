package tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import main.Cell;

public class UnitTest_Cell {

	@Test
	public void testSetReset() {
		Cell testCell = new Cell();
		assertFalse(testCell.getSet());
		testCell.set();
		assertTrue(testCell.getSet());
		testCell.reset();
		assertFalse(testCell.getSet());
	}
	
	@Test
	public void testList() {
		Cell testCell = new Cell();
		assertTrue(testCell.list().isEmpty());
		testCell.list().add(5);
		testCell.list().push(7);
		assertFalse(testCell.list().isEmpty());
		int result = testCell.list().pop();
		assertEquals(7, result);
		testCell.list().clear();
		assertTrue(testCell.list().isEmpty());
	}

}
