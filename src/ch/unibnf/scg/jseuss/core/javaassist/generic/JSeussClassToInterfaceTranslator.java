package ch.unibnf.scg.jseuss.core.javaassist.generic;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.Translator;
import ch.unibnf.scg.jseuss.core.javaassist.guice.JavaInterfaceGenerator;
import ch.unibnf.scg.jseuss.utils.JSeussConfig;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;

public class JSeussClassToInterfaceTranslator implements Translator {

	@Override
	public void start(ClassPool pool) throws NotFoundException,
			CannotCompileException {
	}

	@Override
	public void onLoad(ClassPool pool, String classname)
			throws NotFoundException, CannotCompileException {
		// don't treat classes which are in ignorelist
		if(JSeussUtils.isInIgnoreList(classname))
			return;
		
		CtClass currentClass = pool.get(classname);
		CtClass createdInterface = null;
		
		System.out.println("*** " + classname + " ***");
		
		createdInterface = pool.makeInterface(JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(classname));
		createdInterface = JavaInterfaceGenerator.generateMethods(currentClass, createdInterface);
		try {
			JSeussJavaassist.writeCtClass(createdInterface, false);
			System.out.println("created interface and wrote to disk: " + createdInterface.getName());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
