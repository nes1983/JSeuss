package ch.unibe.scglectures;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

    public static void main(String... args) {
       Injector injector = Guice.createInjector(
    	new AbstractModule() {
    	   public void configure() {
    		   bind(IDice.class).to(Dice.class);
    	   }
       });
       Game game = injector.getInstance(Ludo.class);
        game.addPlayer();
        game.addPlayer();
        game.setOutput(System.out);
        game.run();
    }
    
}
