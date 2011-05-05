package usecase.classloading;

import javassist.ClassPool;
import javassist.Loader;
import ch.unibnf.scg.jseuss.core.javaassist.generic.JSeussClassToInterfaceTranslator;
import ch.unibnf.scg.jseuss.core.javaassist.generic.JSeussTranslator;

public class MiniEmailApp {
	public static void main(String[] args) throws Throwable {
		
//		JSeussClassToInterfaceTranslator t = new JSeussClassToInterfaceTranslator();
//		ClassPool pool = ClassPool.getDefault();
//		Loader cl = new Loader(pool);
//		cl.addTranslator(pool, t);
//		cl.run("usecase.classloading.MiniEmail", null);
		
		JSeussTranslator t1 = new JSeussTranslator();
		ClassPool pool = ClassPool.getDefault();
		Loader cl = new Loader(pool);
		cl.addTranslator(pool, t1);
		cl.run("usecase.classloading.MiniEmail", null);
	}
}
