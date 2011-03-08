package ludo.test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import ludo.*;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SquareBuilderProviderTest {
	Injector injector = Guice.createInjector(new LudoModule());

	@Test
	public void newSquareBuilderShouldHaveFourStonesOnEachStartingSquare() {
		SquareBuilderProvider sbp = injector
				.getInstance(SquareBuilderProvider.class);
		SquareBuilder sb = sbp.get();
		for (Square s : sb.getStarts()) {
			assertEquals( sizeOf(s.getOccupants().iterator()), 4);
		}
	}

	private static int sizeOf(Iterator i) {
		int ret = 0;
		while (i.hasNext()) {
			ret++;
			i.next();
		}
		return ret;
	}
}
