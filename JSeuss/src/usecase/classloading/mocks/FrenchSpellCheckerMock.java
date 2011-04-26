package usecase.classloading.mocks;

import usecase.classloading.Email;
import generated.guice.usecase.classloading.IEmail;
import generated.guice.usecase.classloading.IFrenchSpellChecker;

public class FrenchSpellCheckerMock implements IFrenchSpellChecker{

	@Override
	public void check(Email email) {
		IEmail e = (IEmail) email;
		e.setLanguage("French [mock]");
	}

}
