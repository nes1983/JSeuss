package usecase;

public class EmailSender {
	
	public static void note() {
		
	}

	public void sendEmail(Email email) {
		FrenchSpellChecker fsc = new FrenchSpellChecker();
		fsc.check(email);
	}
}
