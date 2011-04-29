package usecase.classloading;


public class EmailSender {
	
	private Email email;
	
//	new EmailSender(argl, bargl, globlyf);
//	--->
//	emailSenderProvider.get(argl, bargl, globglyf);
//	
//	public EmailSender(Argl, Bargl, Globglyf) {
//		email = new Email();
//	}
//	
//	public void initialize(Argl, Bargl, Globglyf) {
//		//email = new Email();
////		email = emailProvider.get();
//	}
	
	public static void note() {
		
	}
	
	public void sendEmail(Email email) {
		FrenchSpellChecker fsc = new FrenchSpellChecker();
		fsc.check(email);
	}
	
	public Email getEmail() {
		return email;
	}

	public void run() {
		email = new Email();
		sendEmail(email);
	}
}
