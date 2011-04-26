package usecase.classloading;

import javassist.ClassPool;
import javassist.Loader;

import org.junit.Test;

/**
 * use -noverify vm argument to overcome verifyerrors
 *
 */
public class UsecaseScenario {

	@Test
	public void testUsecaseScenario() throws Throwable {
		JSeussTranslator t = new JSeussTranslator();
		ClassPool pool = ClassPool.getDefault();
		Loader cl = new Loader(pool);
		cl.addTranslator(pool, t);
		cl.run("usecase.classloading.EmailApp", null);
	}
	
}
