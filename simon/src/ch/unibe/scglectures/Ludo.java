package ch.unibe.scglectures;

import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

import static ch.unibe.scglectures.SquareGrid.Heading.*;


public class Ludo extends Game implements ILudo {

    private IDice dice;
    private List<IStartSquare> starts;
    private Iterator<IStartSquare> freeStarts;
    private ISquareGrid grid;
    @Inject
    private Provider<IPlayer> playerProvider;
    @Inject
	private Provider<ISquareBuilder> squareBuilderProvider;
    @Inject
	private Provider<ISquareGrid> squareGridProvider;
    
    @Inject
    public Ludo(IDice dice) {
    	this.dice = dice;
    	// TODO static call
        this.starts = Ludo.makeBoard();
        this.freeStarts = starts.iterator();
    	// TODO static call
        this.grid = Ludo.makeGrid(starts.iterator());
    }
    
    public IStartSquare getStartSquare(Color color) {
        return starts.get(color.ordinal());
    }

    @Override
	public IPlayer makePlayer() {
        assert freeStarts.hasNext();
        IPlayer p = playerProvider.get();
        p.setStartSquare(freeStarts.next());
        return p;
    }

    public void nextTurn(int steps) {
        this.currentPlayer().move(steps);
        if (steps == 6) return; // play again!
        this.switchPlayer();
    }

    @Override
    public void playNextTurn() {
        int steps = dice.roll();
        nextTurn(steps);
    }
    
    @Override
    public void startGame() {
        super.startGame();
        freeStarts = null;
    }

    @Override
    public String toString() {
        return grid.toString();
    }
    
    // TODO was static method
    public List<IStartSquare> makeBoard() {
        ISquareBuilder board = squareBuilderProvider.get();
        for (Color color: Color.values()) {
            board.branch(color).squares(5).goal().endOfBranch();
            board.squares(2).startHere(color).squares(10);
        }
        return board.closeRing().getStartSquares();
    }
    
    // TODO was static method
    public ISquareGrid makeGrid(Iterator<IStartSquare> starts) {
    	ISquareGrid grid = squareGridProvider.get();
    	grid.setRowsAndColumns(15,15);
    	quarter(grid.getTurtle(5, 1, DOWN, starts.next()));
    	quarter(grid.getTurtle(1, 9, LEFT, starts.next()));
    	quarter(grid.getTurtle(9, 13, UP, starts.next()));
    	quarter(grid.getTurtle(13, 5, RIGHT, starts.next()));
    	return grid;
    }
    
    // TODO was static method
    public void quarter(ITurtle turtle) {
        turtle
        	.move(1)
        	.turnLeft()
        	.move(5)
        	.turnLeft()
        	.skip()
        	.move(5)
        	.turnRight()
        	.move(1)
        	.branchHere()
        	.move(2)
        // branch
        	.gotoBranch()
        	.turnRight()
        	.skip()
	        .move(5)
	        .skip()
	        .move(1);
    }
}
