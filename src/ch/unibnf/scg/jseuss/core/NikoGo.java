package ch.unibnf.scg.jseuss.core;

import ch.unibnf.scg.sample.emailservice.EmailSender;
import ch.unibnf.scg.sample.model.Email;

public class NikoGo {
	
	public static void main(String[] args){
		EmailSender sender = new EmailSender();
		sender.sendEmail(new Email());
	}

}
