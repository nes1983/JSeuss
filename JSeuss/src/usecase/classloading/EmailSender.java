package usecase.classloading;

public class EmailSender {
	
	//private Email email;
	
	public static void note() {
		
	}
	
	public void sendEmail() {
		Email email = new Email(); 
		FrenchSpellChecker fsc = new FrenchSpellChecker();
		fsc.check(email);
	}
	
	public Email getEmail() {
		Email email = new Email();
		FrenchSpellChecker fsc = new FrenchSpellChecker();
		fsc.check(email);
		return email;
	}
}
