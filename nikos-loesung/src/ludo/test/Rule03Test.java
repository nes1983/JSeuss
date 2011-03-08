package ludo.test;

import ludo.*;
import static org.junit.Assert.*;
import ludo.LudoModule;
import ludo.NoStartSquaresModule;
import ludo.Square;
import ludo.SquareBuilder;
import ludo.Stone;
import ludo.StoneFactory;

import org.junit.Before;
import org.junit.Test;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

import org.junit.runner.RunWith;

/**
 * Each player rolls and then moves his piece clockwise along the squares.
 * 
 * 
 */
public class Rule03Test {

	private final Injector injector = Guice.createInjector(Modules.override(new LudoModule()).with(new NoStartSquaresModule()));
	
	private final StoneFactory stoneFactory = injector
			.getInstance(StoneFactory.class);

	@Test
	public void shouldMoveStoneByOneTwoAndThreeFields() {
		Square board = injector.getInstance(SquareBuilder.class).squares(99)
				.getFirst();
		Stone stone = stoneFactory.create(board);
		assertEquals(0, board.indexOf(stone));
		stone.move(1);
		assertEquals(1, board.indexOf(stone));
		stone.move(2);
		assertEquals(3, board.indexOf(stone));
		stone.move(3);
		assertEquals(6, board.indexOf(stone));
	}

	


}
