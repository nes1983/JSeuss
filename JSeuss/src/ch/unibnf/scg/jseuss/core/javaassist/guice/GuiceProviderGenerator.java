package ch.unibnf.scg.jseuss.core.javaassist.guice;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;

import com.google.inject.Provider;

public class GuiceProviderGenerator {

	private static ClassPool classPool = ClassPool.getDefault();

	private static CtClass createCtClass(String classFullName) {
		CtClass ctClass = classPool.makeClass(classFullName);
		ctClass.setModifiers(Modifier.PUBLIC);
		return ctClass;
	}

	/**
	 * Generates a Guice Provider Class for the given class (which must
	 * implement the same returned interface). <br>
	 * 
	 * 
	 * If <code>createJar</code> is true, then the Provider will be placed in a
	 * Jar Archive with the same name of the provider class, and the generated
	 * bytecode will be removed automatically.
	 * 
	 * @param concreteClasses
	 * @param returnInterfaceClass
	 * @param providerFullName
	 * @return
	 */
	public static ClassDetails generate(Class<?> concreteClass,
			Class<?> returnInterfaceClass, String providerFullName,
			boolean createJar) {
		ClassDetails generated = null;
		try {
			CtClass ctProvider = createCtClass(providerFullName);
			CtClass iProvider = classPool.get(Provider.class.getName());
			ctProvider.addInterface(iProvider);

			CtConstructor[] constructors = ctProvider.getConstructors();
			if (constructors != null && constructors.length > 0) {
				// System.out.println("existing constructors");
				for (CtConstructor ctConstructor : constructors) {
					ctConstructor.setBody("System.out.println();");
				}
			} else {
				// System.out.println("new constructors");
				String body = "public " + ctProvider.getSimpleName() + "(){}";
				CtConstructor constructor = CtNewConstructor.make(body,
						ctProvider);
				ctProvider.addConstructor(constructor);
			}

			CtMethod[] ctMethods = generateProviderMethod(ctProvider,
					concreteClass, returnInterfaceClass);
			for (CtMethod ctMethod : ctMethods) {
				ctProvider.addMethod(ctMethod);
			}

			ctProvider.rebuildClassFile();
			generated = JSeussUtils.writeCtClass(ctProvider);

			if (createJar) {
				JSeussUtils.createJarArchive(providerFullName);
				// JSeussUtils.cleanupBytecode(providerFullName);
			}
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return generated;
	}

	private static CtMethod[] generateProviderMethod(CtClass ctDeclaring,
			Class<?> concreteClass, Class<?> returnInterface)
			throws NotFoundException, CannotCompileException {

		CtMethod[] ctMethods = new CtMethod[1];
		CtClass ctReturn = classPool.getCtClass(Object.class.getName());

		for (int i = 0; i < ctMethods.length; i++) {
			String fullClassName = concreteClass.getName();
			String methodBody = generateProviderMethodSource(fullClassName);
			String methodName = "get";
			ctMethods[i] = CtNewMethod.make(Modifier.PUBLIC, ctReturn,
					methodName, new CtClass[0], new CtClass[0], methodBody,
					ctDeclaring);
		}

		return ctMethods;
	}

	private static String generateProviderMethodSource(String clazzName) {
		String code = "{";
		code += "return ($r) new " + clazzName + "();";
		code += "}";
		// System.out.println(code);
		return code;
	}

}
