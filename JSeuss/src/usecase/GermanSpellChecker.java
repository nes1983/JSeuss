package usecase;

import generated.guice.usecase.IFrenchSpellChecker;

public class GermanSpellChecker implements IFrenchSpellChecker {
	public void check(Email email) {
		email.language = "German";
	}

}
