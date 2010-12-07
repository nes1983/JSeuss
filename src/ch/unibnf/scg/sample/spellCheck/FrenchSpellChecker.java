package ch.unibnf.scg.sample.spellCheck;

import generated.guice.SpellCheckerInterface;
import ch.unibnf.scg.sample.model.Email;

/**
 * This class is used for testing, it should be written by developer B; who
 * requested to extract public methods of GermanSpellChecker in order to inject
 * it later.
 * 
 * @author tgdmoah1
 * 
 */

public class FrenchSpellChecker implements SpellCheckerInterface {

	public boolean check(Email arg0) {
		System.out.println("French spell checking happens here!");
		return true;
	}

}
