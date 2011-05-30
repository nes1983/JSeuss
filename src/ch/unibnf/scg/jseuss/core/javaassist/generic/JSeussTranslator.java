package ch.unibnf.scg.jseuss.core.javaassist.generic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
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
		if(JSeussUtils.isInIgnoreList(className))
			return;
		
		CtClass currentClass = pool.get(className);
		CtClass correspondingInterface = null;
		
		System.out.println("******* CLASS " + className + " *******");
		
//		currentClass.instrument(new FieldAccessRewriter());
		
		if(!currentClass.isInterface()) {
			correspondingInterface = pool.get(JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(className));
			currentClass.addInterface(correspondingInterface);
			System.out.println("ADDED INTERFACE:\t" + correspondingInterface.getName());
			
			JSeussJavaassist.guicify(currentClass);
		}
		
		//XXX Start ReEnable after Demo
//		JSeussJavaassist.changeCasts(currentClass);
//		JSeussJavaassist.changeFields(currentClass);
//		JSeussJavaassist.changeMethods(currentClass);
		//XXX End ReEnable after Demo
		
//		ConstPool cp = currentClass.getClassFile().getConstPool();
//		List<String> alreadyChecked = new ArrayList<String>();
//		List<String> classesToReplace = new ArrayList<String>();
//		for(int i=0; i<cp.getSize(); i++) {
//			try {
//				String utf8Info = cp.getUtf8Info(i);
////				System.out.println(i + ": " + utf8Info);
//				for(String c : JSeussUtils.getQualifiedClassNames(utf8Info)) {
//					if(!alreadyChecked.contains(c)) {
////						System.out.println("================> " + c);
//						CtClass clazz = pool.get(c);
//						if(!JSeussUtils.isInIgnoreList(c) && !c.startsWith(JSeussConfig.GENERATED_PACKAGE_PREFIX) && !clazz.isInterface()) {
//							classesToReplace.add(c);
//						}
//						alreadyChecked.add(c);
//					}
//				}
//			} catch(Exception e) {
////				System.out.println(i);
//			}
//		}
//		for(String c : classesToReplace) {
//			String qualifiedInterfaceName = JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(c);
//			System.out.println("REPLACING:\t" + c + " with " + qualifiedInterfaceName);
//			CtClass clazz = pool.get(qualifiedInterfaceName);
//			if(clazz.isFrozen()) {
//				clazz.defrost();
//			}
//			currentClass.replaceClassName(c, qualifiedInterfaceName);
//		}
		
		
		//debug
		System.out.print("DECLARED METHODS:\t");
		for(CtMethod m : currentClass.getDeclaredMethods())
			System.out.print(m.getName() + " | ");
		System.out.println();
		
		//debug reason (to have a look into the generated classes)
		try {
			if(!currentClass.isInterface())
				System.out.println(currentClass.getDeclaredFields()[0]);
			JSeussJavaassist.writeCtClass(currentClass, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("--------------------------------------------------------------------------");
	}
}
