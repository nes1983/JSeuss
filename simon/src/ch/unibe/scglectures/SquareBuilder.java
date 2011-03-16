package ch.unibe.scglectures;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

/** Creates lists of linked squares.
 *<P>
 * This class implements a <i>fluent interface</i>.
 * Fluent interfaces are a novel way of implementing object oriented APIs, coined by Eric Evans and Martin Fowler.
 * Fluent interfaces use <i>method chaining</i>, ie each method returns the receiver of the next method,
 * which might lead to more readable code than the classic Java Beans convention.
 * Typical use cases of fluent interfaces are builders, such as this class,
 * as well as other internal DSLs (domain specific languages). 
 * Please use fluent interfaces with care, most classes are best served with a classic Beans API.
 *<P>
 * The following example creates the default Ludo board:
 *<PRE>
 * SquareBuilder board = new SquareBuilder();
 * for (Color color: Color.values()) board
 *         .branch(color)
 *         .squares(5)
 *         .goal()
 *         .endOfBranch()
 *         .squares(2)
 *         .startHere(color)
 *         .squares(10);
 * board.closeRing();
 *</PRE>
 * This class implements a simple fluent interface where all methods returns the same chaining context.
 * We could, for example, improve the design such that the starting interface includes a <tt>#branch</tt>
 * but no <tt>#endOfBranch</tt> method, and only upon a call to <tt>#branch</tt> and interface that includes
 * <tt>#endOfBranch</tt> (but no <tt>#branch</tt>) is returned, which in turn returns an object of the initial type.
 * This setup would ensure (at compile time!) that branches are opened and closed in correct order only.
 * The current implementation enforces this constraint at runtime, using an assertion.
 *<P>
 * @see <A HREF="http://martinfowler.com/bliki/FluentInterface.html">Fluent Interface</A> by Martin Fowler
 *<P> 
 * @author Adrian Kuhn, 2009
 * 
 * 
 */
public class SquareBuilder implements ISquareBuilder {

    private ISquare current, origin, branch;
    @Inject
    private IGoalSquare goal;
    private List<IStartSquare> starts = makeStartSquares();
    @Inject
    private Provider<IBranchSquare> branchSquareProvider;
    @Inject
	private Provider<List<IStartSquare>> listStartSquareProvider;
    @Inject
	private Provider<IStartSquare> startSquareProvider;
    
    public SquareBuilder() {
        origin = current = new Square();
        branch = null;
    }

    public void add(ISquare square) {
        current = current.add(square);
    }

    public ISquareBuilder branch(Color color) {
        assert branch == null;
        IBranchSquare bs = branchSquareProvider.get();
        bs.setColor(color);
        add(branch = bs);
        return this;
    }

    public ISquareBuilder closeRing() {
        this.add(getFirst());
        return this;
    }

    public ISquareBuilder endOfBranch() {
        assert branch != null;
        current = branch;
        branch = null;
        return this;
    }

    public ISquare getFirst() {
        return origin.next();
    }
    
    public List<IStartSquare> getStartSquares() {
        return Collections.unmodifiableList(starts);
    }

    public IStartSquare getStartOf(Color color) {
        return starts.get(color.ordinal());
    }

    public ISquareBuilder goal() {
        this.add(goal);
        return this;
    }
  
    public ISquareBuilder squares(int times) {
        for (int n = 0; n < times; n++) add(new Square());
        return this;
    }

    public ISquareBuilder startHere(Color color) {
        starts.get(color.ordinal()).add(current);
        return this;
    }

    // TODO was static
    public List<IStartSquare> makeStartSquares() {
        List<IStartSquare> starts = listStartSquareProvider.get();
        for (Color color: Color.values()) {
            IStartSquare start = startSquareProvider.get();
            for (int n = 0; n < 4; n++) start.makeStone(color);
            starts.add(start);
        }
        return starts;
    }
    
}
