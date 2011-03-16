package ch.unibe.util;

public class Length implements ILength {

	//TODO was static and final
    public <T> int of(Iterable<T> iter) {
        int size = 0;
        for (@SuppressWarnings("unused") T each: iter) size++;
        return size;
    }
    
}
