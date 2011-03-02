package ch.unibe.scglectures;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public abstract class Game implements Runnable {

	private PrintStream out;
	private List<Player> players = new LinkedList<Player>();
	private Iterator<Player> iterator;
	private Player current;

    public Player addPlayer() {
        assert !isRunning();
        Player player = this.makePlayer();
        players.add(player); 
        return player;
    }

	public Player currentPlayer() {
        return current;
    }

    public Player getWinner() {
        for (Player each: players) if (each.isWinner()) return each;
        return null;
    }

	public boolean hasEnoughPlayers() {
        return players.size() >= 2;
    }

    public boolean isRunning() {
        return iterator != null && current != null && getWinner() == null;
    }

    protected abstract Player makePlayer();

    public abstract void playNextTurn();

    private void println(Object object) {
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
    
    protected void switchPlayer() {
        if (!iterator.hasNext()) iterator = players.iterator();
        current = iterator.next();
    }
	
}