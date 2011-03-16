package ch.unibe.scglectures;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;


/**
 * Stones of a given color are redirected to the stairways to heaven.
 *<P> 
 * @author Adrian Kuhn, 2007
 *
 */
public class BranchSquare extends Square implements IBranchSquare {

    private ISquare branch;
    public Color color;
    
    @Inject
    /** Creates a new square, but does <em>not</em> establish the invariant! 
     * Branch and next square remain unset.
     * Please call <tt>#add</tt> twice to establish the invariant.
     * 
     * @see #add(Square)
     */
    public BranchSquare(@Assisted Color color) {
        this.color = color;
    }

    public BranchSquare() {
	}

	/** First adds the branch, then the next square. Establishes the invariant.
     *<P>
     * To establish the invariant, this method must be called twice. <em>Caution:</em> Please beware that the behavior
     * of this method changes between the first and the second method call. The first call links the receiver to the
     * given branch, the second call links the receiver with the given next square. Any further call fails with an
     * exception.
     * 
     * @throws AssertionError when called more than twice.
     */
    @Override
	public ISquare add(ISquare next) {
        return branch == null ? this.branch = next : super.add(next);
    }
    
    public ISquare branch() {
        return branch;
    }

    /** Returns either the branch or the next square, depending on the given color.
      *
      */
    @Override
	public ISquare chooseNext(Color color) {
        return this.color == color ? branch() : next();
    }
    
    public Color color() {
        return color;
    }

    @Override
	public boolean invariantOfLinks() {
        return branch != null && super.invariantOfLinks();
    }

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

}
