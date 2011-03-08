package ludo;


/**
 * There shall be four colors.
 *<P> 
 * @author Adrian Kuhn, 2007
 * @author Niko Schwarz, 2010 
 *
 */
public enum Color {

    RED, GREEN, YELLOW, BLUE;

    public Color next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public static int length() {
        return values().length;
    }

}
