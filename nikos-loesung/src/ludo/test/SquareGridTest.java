package ludo.test;

import static org.junit.Assert.*;
import ludo.BranchSquare;
import ludo.Ludo;
import ludo.LudoModule;
import ludo.LudoFactory;
import ludo.Square;
import ludo.SquareGrid;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class SquareGridTest {

	private SquareGrid grid;
	private int row, column;
	private final Injector injector = Guice.createInjector(new LudoModule());

	@Test
	public void testRedQuarter() {
		grid = injector.getInstance(SquareGrid.class);
		assertCell(5, 1); // read start
		assertNextCell(6, 1);
		assertNextCell(6, 2);
		assertNextCell(6, 3);
		assertNextCell(6, 4);
		assertNextCell(6, 5);
		// skip cell (6, 6)
		assertNextCell(5, 6);
		assertNextCell(4, 6);
		assertNextCell(3, 6);
		assertNextCell(2, 6);
		assertNextCell(1, 6);
		assertNextCell(0, 6);
		assertNextCell(0, 7); // passing branch
		assertNextCell(0, 8);
		assertNextCell(1, 8); // passing green start
		assertNextCell(2, 8);
		assertNextCell(3, 8);
		assertNextCell(4, 8);
		assertNextCell(5, 8);
		// etc...
	}

	@Test
	public void testRedStairwaysToHeaven() {
		grid = injector.getInstance(SquareGrid.class);
		// branch
		assertCell(7, 0); 
		assertNextBranch(7, 1);
		assertNextCell(7, 2);
		assertNextCell(7, 3);
		assertNextCell(7, 4);
		assertNextCell(7, 5);
		// skip cell (7, 6)
		assertNextCell(7, 7);  
		// non-branch
		assertCell(7, 0); 
		assertNextCell(6, 0);
		assertNextCell(6, 1);
		assertNextCell(6, 2);
		// etc...
	}
	
	
	private void assertNextCell(int row, int column) {
		Square square = grid.at(this.row, this.column);
		Square next = grid.at(this.row = row, this.column = column);
		assertEquals(next, square.next());
	}

	private void assertNextBranch(int row, int column) {
		Square square = grid.at(this.row, this.column);
		Square next = grid.at(this.row = row, this.column = column);
		assertTrue(square instanceof BranchSquare);
		assertEquals(next, ((BranchSquare) square).branch());
	}
	
	private void assertCell(int row, int column) {
		assertNotNull(grid.at(this.row = row, this.column = column));
	}
	
}
