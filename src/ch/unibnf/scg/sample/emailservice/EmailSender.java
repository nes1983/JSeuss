package ch.unibnf.scg.sample.emailservice;

import ch.unibnf.scg.sample.model.Email;
import ch.unibnf.scg.sample.spellCheck.GermanSpellChecker;

/**
 * This is the EmailSender class in which the DI should take place. There are
 * certain limitations such that variable should be declared as an instance
 * variable; not inside a method.
 * 
 * Initialization of the instance variable is not important, it can be in a
 * constructor, or inside the method.
 * 
 * @author tgdmoah1
 * 
 */
public class EmailSender {
	GermanSpellChecker spellChecker;

	public void sendEmail(Email email) {
		spellChecker = new GermanSpellChecker();

		boolean isValid = spellChecker.check(email);
		if (isValid) {
			send(email);
		} else {
			System.out.println("email is not valid");
		}
	}

	private void send(Email email) {
		System.out.println("Email has been sent!");
	}

}
