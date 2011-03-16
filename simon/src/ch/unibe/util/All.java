package ch.unibe.util;


public class All implements IAll {

	// TODO was static
    public boolean notNull(Object... values) {
        for (Object each: values) if (each == null) return false;
        return true;
    }

}
