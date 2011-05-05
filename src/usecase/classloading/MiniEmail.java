package usecase.classloading;


public class MiniEmail {
	public static MiniEmail q;
	private static MiniEmail miniEmail;
	
	public static void main(String... a) {
		run();
	}
	
	public static void run() {
		miniEmail = q;
	}
}
