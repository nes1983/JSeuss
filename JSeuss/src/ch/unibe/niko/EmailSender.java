package ch.unibe.niko;

public class EmailSender {
	FrenchSpellChecker fsc = new FrenchSpellChecker();
	
	public static void note() {
		
	}

	public void sendEmail(Email email) {
		fsc.check(email);
	}
}
