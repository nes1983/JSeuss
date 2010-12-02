package ch.unibnf.scg.sample.emailservice;

import ch.unibnf.scg.sample.model.Email;
import ch.unibnf.scg.sample.spellCheck.GermanSpellChecker;

public class EmailSender {
	
	GermanSpellChecker spellChecker; 
	
	public EmailSender() {
		
	}
	
	public void sendEmail(Email email){
		spellChecker = new GermanSpellChecker();
		
		boolean isValid = spellChecker.check(email);
		if(isValid){
			send(email);
		} else {
			System.out.println("email is not valid");
		}
	}
	
	private void send(Email email){
		System.out.println("Email has been sent!");
	}

}
