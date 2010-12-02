package ch.unibnf.scg.sample.factory;

import ch.unibnf.scg.sample.spellCheck.FrenchSpellChecker;


public class FrenchSpellCheckerFactory {
	
	public static FrenchSpellChecker createInstance(){
		return new FrenchSpellChecker();
	}

}
