package ch.unibnf.scg.jseuss.core.javaassist.guice;

import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import ch.unibnf.scg.jseuss.utils.JSeussConfig;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;

public class JavaInterfaceGenerator {

	private static ClassPool classPool = ClassPool.getDefault();
	private static boolean interfaceTransformationActivated = false;

	private static CtClass createCtInterface(String interfaceFullName) {
		CtClass ctInterface = classPool.makeInterface(interfaceFullName);
		ctInterface.setModifiers(Modifier.PUBLIC | Modifier.INTERFACE);
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
		CtClass ctReturn = getMethodReturnType(method);

		CtClass[] ctParams = getMethodParameters(method);

		CtClass[] ctThrowables = getMethodExceptions(method);

		CtMethod ctMethod = createMethod(method.getName(), ctReturn,
				ctDeclaring, ctParams, ctThrowables, method.getModifiers() | Modifier.ABSTRACT);
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
	 * @throws CannotCompileException 
	 * @throws NotFoundException 
	 * @throws IOException 
	 * @throws ClassNotFoundException
	 */
	public static Class<?> generate(Class<?> inputClass,
			String interfaceFullName)
			throws CannotCompileException, NotFoundException, IOException {
		Class<?> generated = null;

		CtClass ctInterface = createCtInterface(interfaceFullName);

		Method[] methods = inputClass.getDeclaredMethods();
		for (Method method : methods) {
			CtMethod ctMethod = generateInterfaceMethod(ctInterface, method);

			ctInterface.addMethod(ctMethod);
		}

		ctInterface.writeFile(JSeussConfig.OUTPUT_DIR);

		ctInterface.rebuildClassFile();
		generated = ctInterface.toClass();

		return generated;
	}
	
	public static Class<?> generateJar(Class<?> inputClass,
			String interfaceFullName)
			throws CannotCompileException, NotFoundException, IOException {
		 Class<?> tmp = generate(inputClass, interfaceFullName);
		 JSeussUtils.createJarArchive(interfaceFullName);
		 return tmp;
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
	
	/**
	 * used for generating the methods in the interface according to the original class
	 * @return the adjusted interface containing the new methods
	 * @throws NotFoundException 
	 * @throws CannotCompileException 
	 */
	public static CtClass generateMethods(CtClass originalClass, CtClass interfaceClass) throws CannotCompileException, NotFoundException {

		for (CtMethod method : originalClass.getDeclaredMethods()) {
			if(!Modifier.isStatic(method.getModifiers()))
				interfaceClass.addMethod(generateInterfaceMethod(interfaceClass, method));
		}

		return interfaceClass;
	}
	
	/**
	 * same as generateMethods, but converts Parameters, Return types according 
	 * to the newly generated interfaces
	 * @param originalClass
	 * @param interfaceClass
	 * @return
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	public static CtClass generateMethodsWithInterfaceTransformation(CtClass originalClass, CtClass interfaceClass) throws CannotCompileException, NotFoundException {
		interfaceTransformationActivated = true;
		CtClass result = generateMethods(originalClass, interfaceClass);
		interfaceTransformationActivated = false;
		return result;
	}
	
	private static CtMethod generateInterfaceMethod(CtClass ctDeclaring, CtMethod method) throws NotFoundException {
		CtClass ctReturn = getMethodReturnType(method);

		CtClass[] ctParams = getMethodParameters(method);

		CtClass[] ctThrowables = getMethodExceptions(method);

		CtMethod ctMethod = createMethod(method.getName(), ctReturn,
				ctDeclaring, ctParams, ctThrowables, method.getModifiers() | Modifier.ABSTRACT);
		return ctMethod;
	}
	
	private static CtClass getMethodReturnType(CtMethod method) throws NotFoundException {
		CtClass returnType = method.getReturnType();
		String name = returnType.getName();
		if(interfaceTransformationActivated && !returnType.isPrimitive() && !JSeussUtils.isInIgnoreList(name))
			name = JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(name);
		CtClass ctReturn = classPool.get(name);
		return ctReturn;
	}
	
	private static CtClass[] getMethodParameters(CtMethod method)	throws NotFoundException {
		CtClass[] params = method.getParameterTypes();
		CtClass[] ctParams = new CtClass[params.length];
		for (int i = 0; i < params.length; i++) {
			CtClass param = params[i];
			String name = param.getName();
			if(interfaceTransformationActivated && !param.isPrimitive() && !JSeussUtils.isInIgnoreList(name))
				name = JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(name);
			ctParams[i] = classPool.get(name);
		}
		return ctParams;
	}
	
	private static CtClass[] getMethodExceptions(CtMethod method) throws NotFoundException {
		CtClass[] throwables = method.getExceptionTypes();
		CtClass[] ctThrowables = new CtClass[throwables.length];
		for (int i = 0; i < throwables.length; i++) {
			CtClass athrowable = throwables[i];
			ctThrowables[i] = classPool.get(athrowable.getName());
		}
		return ctThrowables;
	}

}
