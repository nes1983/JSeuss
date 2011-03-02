package ch.unibnf.scg.jseuss.core.javaassist.guice;

import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;

public class JavaInterfaceGenerator {

	private static ClassPool classPool = ClassPool.getDefault();

	private static CtClass createCtInterface(String interfaceFullName) {
		CtClass ctInterface = classPool.makeInterface(interfaceFullName);
		ctInterface.setModifiers(Modifier.PUBLIC + Modifier.INTERFACE);
		return ctInterface;
	}

	private static CtMethod createMethod(String methodName,
			CtClass ctReturnType, CtClass ctDeclaring, CtClass[] ctParams,
			CtClass[] ctExceptions, int modifiers) throws NotFoundException {
		if (ctParams == null) {
			ctParams = new CtClass[0];
		}
		CtMethod ctMethod = new CtMethod(ctReturnType, methodName, ctParams,
				ctDeclaring);
		ctMethod.setModifiers(modifiers);
		if (ctExceptions != null && ctExceptions.length > 0) {
			ctMethod.setExceptionTypes(ctExceptions);
		}
		return ctMethod;
	}

	private static CtMethod generateInterfaceMethod(CtClass ctDeclaring,
			Method method) throws NotFoundException {
		// construct the return type
		CtClass ctReturn = getMethodReturnType(method);

		// construct method parameters
		CtClass[] ctParams = getMethodParameters(method);

		// construct throwables
		CtClass[] ctThrowables = getMethodExceptions(method);

		// construct the ctMethod
		CtMethod ctMethod = createMethod(method.getName(), ctReturn,
				ctDeclaring, ctParams, ctThrowables, Modifier.PUBLIC
						+ Modifier.ABSTRACT);
		return ctMethod;
	}

	/**
	 * Generates an interface based on a given class.<br>
	 * Interface methods are those directly declared as PUBLIC in the given
	 * class. <br>
	 * if <code>createJar</code> is true, then the interface is placed into a
	 * Jar Archive with the same name of the interface, and the generated
	 * bytecode will be removed automatically.
	 * 
	 * @param inputClass
	 * @param interfaceFullName
	 * @param createJar
	 * @throws ClassNotFoundException
	 */
	public static Class<?> generate(Class<?> inputClass,
			String interfaceFullName, boolean createJar) {
		Class<?> generated = null;
		try {
			CtClass ctInterface = createCtInterface(interfaceFullName);

			Method[] publicMethods = inputClass.getDeclaredMethods();
			for (Method method : publicMethods) {
				CtMethod ctMethod = generateInterfaceMethod(ctInterface, method);

				ctInterface.addMethod(ctMethod);
			}

			ctInterface.writeFile("./output/");

			if (createJar) {
				JSeussUtils.createJarArchive(interfaceFullName);
				// JSeussUtils.cleanupBytecode(interfaceFullName);
			}
			// generated = true;
			ctInterface.rebuildClassFile();
			generated = ctInterface.toClass();
		} catch (Exception e) {
			e.printStackTrace();
			// generated = false;
			generated = null;
		}
		return generated;
	}

	private static CtClass[] getMethodExceptions(Method method)
			throws NotFoundException {
		Class<?>[] throwables = method.getExceptionTypes();
		CtClass[] ctThrowables = new CtClass[throwables.length];
		for (int i = 0; i < throwables.length; i++) {
			Class<?> athrowable = throwables[i];
			ctThrowables[i] = classPool.get(athrowable.getName());
		}
		return ctThrowables;
	}

	private static CtClass[] getMethodParameters(Method method)
			throws NotFoundException {
		Class<?>[] params = method.getParameterTypes();
		CtClass[] ctParams = new CtClass[params.length];
		for (int i = 0; i < params.length; i++) {
			Class<?> param = params[i];
			ctParams[i] = classPool.get(param.getName());
		}
		return ctParams;
	}

	private static CtClass getMethodReturnType(Method method)
			throws NotFoundException {
		CtClass ctReturn = classPool.get(method.getReturnType().getName());
		return ctReturn;
	}

}
