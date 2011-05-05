package usecase.classloading;

import javassist.ClassPool;
import javassist.Loader;

import org.junit.Test;

import ch.unibnf.scg.jseuss.core.javaassist.generic.JSeussClassToInterfaceTranslator;


public class UsecaseScenarioInterfaceGenerator {
	@Test
	public void generateInterfaces() throws Throwable {
		JSeussClassToInterfaceTranslator t = new JSeussClassToInterfaceTranslator();
		ClassPool pool = ClassPool.getDefault();
		Loader cl = new Loader(pool);
		cl.addTranslator(pool, t);
		cl.run("usecase.classloading.EmailApp", null);
	}
}
