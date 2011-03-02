package ch.unibnf.scg.jseuss.core.javaassist.guice.test;

import ch.unibnf.scg.sample.emailservice.EmailSender;
import generated.guice.SpellCheckerBinder;
import generated.guice.SpellCheckerProvider;

public class GeneratedCodeTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpellCheckerBinder binder = new SpellCheckerBinder();
		SpellCheckerProvider provider = new SpellCheckerProvider();
		EmailSender sender = new EmailSender();
		System.out.println(binder.toString());
		System.out.println(provider.toString());
		System.out.println(sender.toString());
	}

}
