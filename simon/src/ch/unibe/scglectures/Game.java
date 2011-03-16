package ch.unibe.scglectures;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;


public abstract class Game implements Runnable, IGame {

	private PrintStream out;
	@Inject
	private Provider<List<IPlayer>> playerProvider;
	private List<IPlayer> players = playerProvider.get();
	private Iterator<IPlayer> iterator;
	private IPlayer current;

    public IPlayer addPlayer() {
        assert !isRunning();
        IPlayer player = this.makePlayer();
        players.add(player); 
        return player;
    }

	public IPlayer currentPlayer() {
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

    public abstract IPlayer makePlayer();

    public abstract void playNextTurn();

    public void println(Object object) {
        if (out != null) out.println(object);
    }

    public void run() {
	    startGame();
	    while (isRunning()) {
	        playNextTurn();
	        println(this);
	    }
	    println("The winner is " + getWinner());
	}

    public void setOutput(PrintStream out) {
        this.out = out;
    }
    
    public void startGame() {
        assert !isRunning() && hasEnoughPlayers();
        iterator = players.iterator();
        current = iterator.next();
    }
    
    public void switchPlayer() {
        if (!iterator.hasNext()) iterator = players.iterator();
        current = iterator.next();
    }
	
}