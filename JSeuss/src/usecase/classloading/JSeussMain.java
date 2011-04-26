package usecase.classloading;

import javassist.ClassPool;
import javassist.Loader;

public class JSeussMain {
	public static void main(String[] args) throws Throwable {
		JSeussTranslator t = new JSeussTranslator();
		ClassPool pool = ClassPool.getDefault();
		Loader cl = new Loader(pool);
		cl.addTranslator(pool, t);
		cl.run("usecase.classloading.EmailApp", args);
	}
}
