package jseuss.lombok;

public class Cup {
	
	private int state;
	private static final int FULL = 100;
	private static final int DRINK = 20;
	private static final int EMPTY = 0;

	public void fill() {
		state = FULL;
	}
	
	public void drink() {
		if(state >= DRINK)
			state -= DRINK;
		else
			state = EMPTY;
	}
	
	public boolean isEmpty() {
		return state == 0 ? true : false;
	}

}
