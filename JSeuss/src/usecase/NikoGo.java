package usecase;

public class NikoGo {
	
	public  Email go(){
		EmailSender sender = new EmailSender();
		Email email = new Email();
		sender.sendEmail(email);
		return email;
	}

}
