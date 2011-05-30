package jseuss.lombok;

import lombok.JSeuss;

public class Dummy {
	private Cup cup;
	
	public Dummy() {
		this.cup = new Cup();
	}
	
	public void fillCup() {
		this.cup.fill();
	}
}
