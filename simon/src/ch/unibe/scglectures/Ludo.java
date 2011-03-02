package ch.unibe.scglectures;

import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;

import ch.unibe.scglectures.SquareGrid.Turtle;
import static ch.unibe.scglectures.SquareGrid.Heading.*;


public class Ludo extends Game {

    private IDice dice;
    private List<StartSquare> starts;
    private Iterator<StartSquare> freeStarts;
    private SquareGrid grid;
    
    @Inject
    public Ludo(IDice dice) {
    	this.dice = dice;
        this.starts = Ludo.makeBoard();
        this.freeStarts = starts.iterator();
        this.grid = Ludo.makeGrid(starts.iterator());
    }
    
    public StartSquare getStartSquare(Color color) {
        return starts.get(color.ordinal());
    }

    @Override
    protected Player makePlayer() {
        assert freeStarts.hasNext();
        return new Player(freeStarts.next());
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
    
    public static List<StartSquare> makeBoard() {
        SquareBuilder board = new SquareBuilder();
        for (Color color: Color.values()) {
            board.branch(color).squares(5).goal().endOfBranch();
            board.squares(2).startHere(color).squares(10);
        }
        return board.closeRing().getStartSquares();
    }
    
    public static SquareGrid makeGrid(Iterator<StartSquare> starts) {
    	SquareGrid grid = new SquareGrid(15,15);
    	quarter(grid.getTurtle(5, 1, DOWN, starts.next()));
    	quarter(grid.getTurtle(1, 9, LEFT, starts.next()));
    	quarter(grid.getTurtle(9, 13, UP, starts.next()));
    	quarter(grid.getTurtle(13, 5, RIGHT, starts.next()));
    	return grid;
    }
    
    private static void quarter(Turtle turtle) {
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
