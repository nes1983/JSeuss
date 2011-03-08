package ludo;

import com.google.inject.Guice;

/**
 * Runs the ludo board game in auto mode, non-interactively.
 * @author Adrian Kuhn
 * @author Niko Schwarz, 2010 
 *
 */
public class Main {

	static {
		try { assert false; throw new Error("-ea missing!"); } 
		catch (AssertionError ex) { }
	}
	
    public static void main(String... args) {
        Game game = Guice.createInjector(new LudoModule()).getInstance(LudoFactory.class).create(2);
        game.run();
    }
    
}
