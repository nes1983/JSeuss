package ludo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.List;

import com.google.inject.Provider;
/** Builder for lists of linked squares.
 *<P>
 * This class implements a <strong>fluent interface</strong>.
 * Fluent interfaces are a novel way of implementing object oriented APIs, coined by Eric Evans and Martin Fowler.
 * Fluent interfaces use <strong>method chaining</strong>, ie each method returns the receiver of the next method,
 * which might lead to more readable code than the classic Java Beans convention.
 * Typical use cases of fluent interfaces are builders, such as this class,
 * as well as other internal DSLs (domain specific languages). 
 * Please use fluent interfaces with care, most classes are best served with a classic Beans API.
 *<P>
 * The following example creates the default Ludo board:
 *<PRE>
 * SquareBuilder board = Guice.getInjector().getInstance(SquareBuilder.class);
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
 * @author Niko Schwarz, 2010 
 * 
 */
public class SquareBuilder {
	//TODO switch final
	private final Square origin;
    private  Square current, branch; 
    private final GoalSquare goal ;
    private final List<StartSquare> starts ;
	

	private final Provider<Square> squareProvider;
	private final IBranchSquareFactory branchSquareFactory;
    
	/*
	 * Not injected but provided by SquareBuilderProvider
	 */
	SquareBuilder( 
			Square origin, 
			GoalSquare goal,
			List<StartSquare> starts, 
			Provider<Square> squareProvider,
			IBranchSquareFactory branchSquareFactory) {
        this.current = this.origin = checkNotNull(origin);
        this.branch = null;
        this.goal = checkNotNull(goal);
        this.starts = checkNotNull(starts);
        this.squareProvider = checkNotNull(squareProvider);
        this.branchSquareFactory = checkNotNull(branchSquareFactory);
    }

    private void add(Square square) {
        current = current.add(square);
    }
    
    public List<StartSquare> getStarts() {
		return starts;
	}

    public SquareBuilder branch(Color color) {
        assert branch == null;
        add(branch = branchSquareFactory.create(color));
        return this;
    }

    public SquareBuilder closeRing() {
        this.add(getFirst());
        return this;
    }

    public SquareBuilder endOfBranch() {
        assert branch != null;
        current = branch;
        branch = null;
        return this;
    }

    public Square getFirst() {
        return origin.next();
    }
    
    public List<StartSquare> getStartSquares() {
        return Collections.unmodifiableList(starts);
    }

    public StartSquare getStartOf(Color color) {
        return starts.get(color.ordinal());
    }

    public SquareBuilder goal() {
        this.add(goal);
        return this;
    }
  
    public SquareBuilder squares(int times) {
        for (int n = 0; n < times; n++) add(squareProvider.get());
        assert getFirst() != null;
        return this;
    }

    public SquareBuilder startHere(Color color) {
        starts.get(color.ordinal()).add(current);
        return this;
    }


    
}
