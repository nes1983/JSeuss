package usecase.classloading;

import generated.guice.usecase.classloading.IEmail;
import generated.guice.usecase.classloading.IFrenchSpellChecker;

import com.google.inject.Inject;
import com.google.inject.Provider;


public class EmailSender2 {
	
	private IEmail email;
	
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
	
	public void sendEmail(IEmail email) {
		IFrenchSpellChecker fsc = iFrenchSpellCheckerProvider.get();
		fsc.check((Email)email);
	}
	
	public IEmail getEmail() {
		return email;
	}

	public void run() {
		email = iEmailProvider.get();
		sendEmail(email);
	}

	@Inject
	Provider<IFrenchSpellChecker> iFrenchSpellCheckerProvider;
	
	@Inject
	Provider<IEmail> iEmailProvider;
}
