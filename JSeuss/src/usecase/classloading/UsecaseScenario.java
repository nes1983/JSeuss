package usecase.classloading;

import javassist.ClassPool;
import javassist.Loader;

import org.junit.Test;

import ch.unibnf.scg.jseuss.core.javaassist.generic.JSeussTranslator;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;

/**
 * use -noverify vm argument to overcome verifyerrors
 *
 */
public class UsecaseScenario {
	
	String mainClass = "usecase.classloading.EmailApp";
	String mocks = "usecase.classloading.mocks.";

	@Test
	public void testUsecaseScenario() throws Throwable {
		// first add to ignore list "main class" and "mocks"
		JSeussUtils.addToIgnoreList(mainClass);
		JSeussUtils.addToIgnoreList(mocks);
		
		JSeussTranslator t = new JSeussTranslator();
		ClassPool pool = ClassPool.getDefault();
		Loader cl = new Loader(pool);
		cl.addTranslator(pool, t);
		cl.run(mainClass, null);
	}
	
}
