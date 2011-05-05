package usecase.classloading;

public class MiniEmail2 implements IMiniEmail {
	public static IMiniEmail q;
	private static IMiniEmail miniEmail;
	
	public static void main(String... a) {
		run();
	}
	
	public static void run() {
		miniEmail = q;
	}
}

interface IMiniEmail {
	
}