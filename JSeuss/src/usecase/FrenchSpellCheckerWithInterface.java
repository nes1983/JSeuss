package usecase;

public class FrenchSpellCheckerWithInterface implements IFrenchSpellChecker {

	public void check(Email email) {
		email.language = "French";
	}
	

}
