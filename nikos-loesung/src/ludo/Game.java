package ludo;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import com.google.inject.ImplementedBy;
import com.google.inject.internal.Nullable;
/** Runs a game. Subclasses must provide 1) how to create players and 2) how to
 * play the next turn. 
 * 
 * @author Adrian Kuhn, 2008
 * @author Niko Schwarz, 2010 
 *
 */
@ImplementedBy(Ludo.class)
public abstract class Game implements Runnable {

	Game(IDie die, PrintStream out, List<IPlayer> players) {
		this.die = die;
		this.out = out;
		this.players = players;
		this.iterator = null;
		current = null;
	}


	private final IDie die;
	private final PrintStream out;
	private final List<IPlayer> players;
	private @Nullable IPlayer current;
	private Iterator<IPlayer> iterator;
	
	public IDie getDie() {
		return die;
	}
    
	public IPlayer getCurrentPlayer() {
        return current;
    }

    public IPlayer getWinner() {
        for (IPlayer each: players) if (each.isWinner()) return each;
        return null;
    }

	public boolean hasEnoughPlayers() {
        return players.size() >= 2;
    }

    public boolean isRunning() {
        return iterator != null && current != null && getWinner() == null;
    }
	
    public abstract void playNextTurn();

    private void println(Object object) {
         out.println(object);
    }

    @Override
    public void run() {
	    startGame();
	    while (isRunning()) {
	        playNextTurn();
	        println(this);
	    }
	    println("The winner is " + getWinner());
	}
    
    public void startGame() {
        assert !isRunning() && hasEnoughPlayers();
        iterator = players.iterator();
        current = iterator.next();
    }
    
    protected void switchPlayer() {
        if (!iterator.hasNext()) iterator = players.iterator();
        current = iterator.next();
    }

	public List<IPlayer> getPlayers() {
		return players;
	}
	
}