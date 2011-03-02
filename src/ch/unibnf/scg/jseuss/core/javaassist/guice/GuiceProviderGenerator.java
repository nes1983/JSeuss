package ch.unibnf.scg.jseuss.core.javaassist.guice;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
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
	public static Class<?> generateGuiceProvider(Class<?> concreteClass,
			Class<?> returnInterfaceClass, String providerFullName,
			boolean createJar) {
		Class<?> generated = null;
		try {
			CtClass ctProvider = createCtClass(providerFullName);
			CtClass iProvider = classPool.get(Provider.class.getName());
			ctProvider.addInterface(iProvider);

			CtMethod[] ctMethods = generateProviderMethod(ctProvider,
					concreteClass, returnInterfaceClass);
			for (CtMethod ctMethod : ctMethods) {
				ctProvider.addMethod(ctMethod);
			}

			ctProvider.writeFile("./output/");

			if (createJar) {
				JSeussUtils.createJarArchive(providerFullName);
				JSeussUtils.cleanupBytecode(providerFullName);
			}
			// generated = true;
			generated = ctProvider.toClass();
		} catch (Exception e) {
			e.printStackTrace();
			// generated = false;
			generated = null;
		}
		return generated;
	}

	private static CtMethod[] generateProviderMethod(CtClass ctDeclaring,
			Class<?> concreteClass, Class<?> returnInterface)
			throws NotFoundException, CannotCompileException {

		CtMethod[] ctMethods = new CtMethod[1];
		CtClass ctReturn = classPool.getCtClass(returnInterface.getName());

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

		code += "return new " + clazzName + "();";

		code += "}";
		return code;
	}

}
