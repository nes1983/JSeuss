package ch.unibnf.scg.sample.spellCheck;

import generated.ch.unibnf.SpellCheckerInterface;
import ch.unibnf.scg.sample.model.Email;

public class FrenchSpellChecker implements SpellCheckerInterface {

	@Override
	public boolean check(Email arg0) {
		System.out.println("I speak Francais!");
		return true;
	}

}
