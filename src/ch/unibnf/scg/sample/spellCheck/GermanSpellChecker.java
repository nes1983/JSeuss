package ch.unibnf.scg.sample.spellCheck;

import ch.unibnf.scg.sample.model.Email;

/**
 * This class is the original in our Email Service example, used inside
 * EmailSender and at which developer B wishes to override with his
 * FrenchSpellChecker.
 * 
 * @author tgdmoah1
 * 
 */
public class GermanSpellChecker {

	public boolean check(Email email) {
		System.out.println("German Spell checking happens here!");
		return true;
	}

}
