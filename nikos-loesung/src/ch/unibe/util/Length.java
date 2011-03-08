package ch.unibe.util;

public class Length {

    public static final <T> int of(Iterable<T> iter) {
        int size = 0;
        for (@SuppressWarnings("unused") T each: iter) size++;
        return size;
    }
    
}
