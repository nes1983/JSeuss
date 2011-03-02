package ch.unibe.util;


public class All {

    public static boolean notNull(Object... values) {
        for (Object each: values) if (each == null) return false;
        return true;
    }

}
