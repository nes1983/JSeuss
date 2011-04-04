package usecase;

public class EmailSenderWithInterface {
	
	public static void note() {
		
	}

	public void sendEmail(Email email) {
		IFrenchSpellChecker fsc = new FrenchSpellCheckerWithInterface();
		fsc.check(email);
	}
}
