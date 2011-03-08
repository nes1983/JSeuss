package ludo;

import java.util.LinkedList;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Stones of a given color are redirected to the stairways to heaven.
 *<P> 
 * @author Adrian Kuhn 2007
 * @author Niko Schwarz, 2010 
 *
 */
public class BranchSquare extends Square {

    private Square branch;
    private final Color color;
    
    /** Creates a new square, but does <em>not</em> establish the invariant! 
     * Branch and next square remain unset.
     * Please call <tt>#add</tt> twice to establish the invariant.
     * 
     * @see #add(Square)
     */
    @Inject
    BranchSquare(@Assisted Color color, LinkedList<Stone> occupants) {
        super(occupants);
    		this.color = color;
    }

    /** First adds the branch, then the next square. Establishes the invariant when called twice.
     *<P>
     * To establish the invariant, this method must be called twice. <em>Caution:</em> Please beware that the behavior
     * of this method changes between the first and the second method call. The first call links the receiver to the
     * given branch, the second call links the receiver with the given next square. Any further call fails with an
     * exception.
     * 
     */
    @Override
    protected Square add(Square next) {
        return branch == null ? this.branch = next : super.add(next);
    }
    
    public Square branch() {
        return branch;
    }

    /** Returns either the branch or the next square, depending on the given color.
      *
      */
    @Override
    protected Square chooseNext(Color color) {
        return this.color == color ? branch() : next();
    }
    
    public Color getColor() {
        return color;
    }

    @Override
    protected boolean invariantOfLinks() {
        return branch != null && super.invariantOfLinks();
    }



}
