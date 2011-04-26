package usecase.classloading;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;
import ch.unibnf.scg.jseuss.core.javaassist.generic.JSeussJavaassist;
import ch.unibnf.scg.jseuss.utils.JSeussConfig;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;

public class JSeussTranslator implements Translator {

	@Override
	public void start(ClassPool pool) throws NotFoundException,
			CannotCompileException {
	}

	@Override
	public void onLoad(ClassPool pool, String className)
			throws NotFoundException, CannotCompileException {
		// don't treat classes which are in ignorelist
		if(JSeussUtils.isInIgnoreList(className) || className.startsWith("usecase.classloading.EmailApp"))
			return;
		
		CtClass currentClass = pool.get(className);
		CtClass correspondingInterface = null;
		
		System.out.println("--- " + className + " ---");
		
		if(!currentClass.isInterface()) {
			correspondingInterface = pool.get(JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(className));
			currentClass.addInterface(correspondingInterface);
			System.out.println("added interface: " + correspondingInterface.getName());
			
			JSeussJavaassist.guicify(currentClass);
		}
		
		for(CtMethod m : currentClass.getDeclaredMethods())
			System.out.print(m.getName() + " | ");
		System.out.println();
		//debug reason:
		JSeussUtils.writeCtClass(currentClass);
		System.out.println("******************************************************************");
	}
}
