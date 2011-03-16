package ch.unibe.niko;

import generated.guice.IFrenchSpellChecker;

public class GermanSpellChecker implements IFrenchSpellChecker {
	public void check(Email email) {
		email.language = "German";
	}

}
