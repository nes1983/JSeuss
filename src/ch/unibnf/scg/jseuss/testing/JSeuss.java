package ch.unibnf.scg.jseuss.testing;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.Loader;
import javassist.NotFoundException;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import ch.unibnf.scg.jseuss.core.javaassist.generic.JSeussTranslator;
import ch.unibnf.scg.jseuss.utils.JSeussConfig;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;

public class JSeuss extends BlockJUnit4ClassRunner {
	final String mainClass = "usecase.activerecord.ActiveRecordApp";
	final String mocks = "usecase.activerecord.mocks.";
	public static JSeuss currentJseuss;
	static Loader cl = makeClassLoader();
	
	{		
		JSeussUtils.addToIgnoreList(mocks);
	}


	public JSeuss(Class<?> klass) throws InitializationError, ClassNotFoundException {
			super(cl.loadClass(addToIgnoreList(klass.getName())));
	}
	
	private static String addToIgnoreList(String name) {
		JSeussUtils.addToIgnoreList(name);
		return name;
	}

	static Loader makeClassLoader() {
		JSeussTranslator t = new JSeussTranslator();
		ClassPool pool = ClassPool.getDefault();
		Loader cl = new Loader(ClassLoader.getSystemClassLoader(),pool);
		setIgnoreList(cl);
		try {
			cl.addTranslator(pool, t);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		}
		return cl;
	}

	static void setIgnoreList(Loader cl) {
		for(String s : JSeussConfig.PACKAGE_IGNORE_LIST)
			cl.delegateLoadingOf(s);
	}
}