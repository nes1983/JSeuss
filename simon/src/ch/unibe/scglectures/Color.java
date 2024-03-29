package ch.unibe.scglectures;

/**
 * There shall be four colors.
 *<P> 
 * @author Adrian Kuhn, 2007
 *
 */
public enum Color {

    RED, GREEN, YELLOW, BLUE;

    public Color next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public int length() {
        return values().length;
    }

}
