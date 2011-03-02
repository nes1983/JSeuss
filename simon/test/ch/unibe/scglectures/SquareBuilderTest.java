package ch.unibe.scglectures;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

@RunWith(JExample.class)
public class SquareBuilderTest {

    private BranchSquare fromStartToBranch(Square start) {
        Square curr = start;
        assertEquals(StartSquare.class, curr.getClass());
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 2        
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 3        
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 4   
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 5       
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 6       
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 7       
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 8       
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 9       
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 10       
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 11       
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 12       
        // GREEN branch
        assertEquals(BranchSquare.class, (curr = curr.next()).getClass());
        return (BranchSquare) curr;
    }
    
    @Test
    public List<StartSquare> newLudoBoard() {
        List<StartSquare> starts = Ludo.makeBoard();
        assertEquals(4, starts.size());
        return starts;
    }
    
    @Test
    @Given("newLudoBoard")
    public BranchSquare fromRedStartToGreenBranch(List<StartSquare> starts) {
        return fromStartToBranch(starts.get(Color.RED.ordinal()));
    }

    @Test
    @Given("newLudoBoard")
    public BranchSquare fromGreenStartToYellowBranch(List<StartSquare> starts) {
        return fromStartToBranch(starts.get(Color.GREEN.ordinal()));
    }

    @Test
    @Given("newLudoBoard")
    public BranchSquare fromYellowStartToBlueBranch(List<StartSquare> starts) {
        return fromStartToBranch(starts.get(Color.YELLOW.ordinal()));
    }

    @Test
    @Given("newLudoBoard")
    public BranchSquare fromBlueStartToRedBranch(List<StartSquare> starts) {
        return fromStartToBranch(starts.get(Color.BLUE.ordinal()));
    }

    
    @Test
    @Given("fromRedStartToGreenBranch")
    public GoalSquare fromGreenBranchToGoal(BranchSquare branch) {
        assertEquals(Color.GREEN, branch.color);
        return fromBranchToGoal(branch);
    }
        
    @Test
    @Given("fromGreenStartToYellowBranch")
    public GoalSquare fromYellowBranchToGoal(BranchSquare branch) {
        assertEquals(Color.YELLOW, branch.color);
        return fromBranchToGoal(branch);
    }
        
    @Test
    @Given("fromYellowStartToBlueBranch")
    public GoalSquare fromBlueBranchToGoal(BranchSquare branch) {
        assertEquals(Color.BLUE, branch.color);
        return fromBranchToGoal(branch);
    }
        
    @Test
    @Given("fromBlueStartToRedBranch")
    public GoalSquare fromRedBranchToGoal(BranchSquare branch) {
        assertEquals(Color.RED, branch.color);
        return fromBranchToGoal(branch);
    }
        
    private GoalSquare fromBranchToGoal(BranchSquare branch) {
        Square curr;
        assertEquals(Square.class, (curr = branch.branch()).getClass()); // 1        
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 2        
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 3   
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 4       
        assertEquals(Square.class, (curr = curr.next()).getClass()); // 5       
        assertEquals(GoalSquare.class, (curr = curr.next()).getClass()); // 6   
        return (GoalSquare) curr;
    }
    
}
