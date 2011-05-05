package usecase.classloading.mocks;

import generated.guice.usecase.classloading.IEmail;

public class EmailMock implements IEmail {

	public String language;

	@Override
	public void setLanguage(String value) {
		language = value;
	}

}
