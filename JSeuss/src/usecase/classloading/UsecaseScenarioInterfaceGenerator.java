package usecase.classloading;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.Loader;
import javassist.NotFoundException;

import org.junit.Before;
import org.junit.Test;


public class UsecaseScenarioInterfaceGenerator {
	@Before
	public void generateInterfaces() throws Throwable {
		JSeussClassToInterfaceTranslator t = new JSeussClassToInterfaceTranslator();
		ClassPool pool = ClassPool.getDefault();
		Loader cl = new Loader(pool);
		cl.addTranslator(pool, t);
		cl.run("usecase.classloading.EmailApp", null);
	}
	
	@Test
	public void adjustGeneratedInterfaces() throws NotFoundException, CannotCompileException {
//		JSeussInterfaceTransformer t = new JSeussInterfaceTransformer();
//		t.transform();
	}
}
