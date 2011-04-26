package obsolete;

import usecase.Email;
import usecase.IFrenchSpellChecker;

public class FrenchSpellCheckerWithInterface implements IFrenchSpellChecker {

	public void check(Email email) {
		email.language = "French";
	}
	

}
