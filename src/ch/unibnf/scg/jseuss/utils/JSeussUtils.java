package ch.unibnf.scg.jseuss.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

public class JSeussUtils {

	private static ClassPool classPool = ClassPool.getDefault();

	/**
	 * deletes the generated directories and class files for cleanup purposes as
	 * the .class files should be included in the jar file.
	 * 
	 * @param clazzFullName
	 */
	private static void cleanupBytecode(String clazzFullName) {

		File filez = new File(clazzFullName.replace('.', '/') + ".class");
		boolean isDeleted = filez.delete();
		if (isDeleted) {
			while ((filez = filez.getParentFile()) != null) {
				isDeleted = filez.delete();
				if (!isDeleted) {
					break;
				}
			}
		}
	}

	private static CtClass createCtClass(String factoryFullName) {
		CtClass ctInterface = classPool.makeClass(factoryFullName);
		ctInterface.setModifiers(Modifier.PUBLIC);
		return ctInterface;
	}

	private static CtClass createCtInterface(String interfaceFullName) {
		CtClass ctInterface = classPool.makeInterface(interfaceFullName);
		ctInterface.setModifiers(Modifier.PUBLIC + Modifier.INTERFACE);
		return ctInterface;
	}

	/**
	 * creates a Jar file for any CtClass entity, based on the full class and
	 * package path.<br>
	 * Jar file is generated in the same directory location.<br>
	 * Jar file name will be the same as givenClass or interface
	 * 
	 * @param ctclass
	 * @param clazzFullName
	 *            for example, com.unibnf.sample.MyClassOrInterface
	 * @throws IOException
	 * @throws CannotCompileException
	 */
	private static void createJarArchive(String clazzFullName)
			throws CannotCompileException, IOException {

		boolean isPackaged = false;
		String jarName = "";
		String jaringPath = "";
		if (clazzFullName.contains(".")) {
			// having a dot means we have package(s)
			isPackaged = true;
			jarName = clazzFullName
					.substring(clazzFullName.lastIndexOf('.') + 1) + ".jar";
			jaringPath = clazzFullName.substring(0,
					clazzFullName.lastIndexOf('.')).replace('.', '/');
		} else {
			isPackaged = false;
			jarName = clazzFullName + ".jar";
			jaringPath = clazzFullName;
		}
		// this path will contain which directories to generate the jar for.
		File jaringDir = new File(jaringPath);

		int packageIndex = 0;
		String outputJarPath = "";
		if (isPackaged) {
			packageIndex = jaringDir.getAbsolutePath().indexOf(
					jaringPath.substring(0, jaringPath.indexOf('/')));
			outputJarPath = jaringDir.getAbsolutePath().substring(0,
					packageIndex);
		} else {
			outputJarPath = jaringDir.getAbsolutePath();
			if (!outputJarPath.endsWith("/")) {
				outputJarPath += "/";
			}
		}
		// this is the full path where to generate the jar
		File outputJar = new File(outputJarPath + jarName);

		// this is the jar command to execute
		String command = "jar cf " + outputJar.getAbsolutePath() + " "
				+ jaringDir.getPath();

		// System.out
		// .println("Executing command: [" + "jar cvf "
		// + outputJar.getAbsolutePath() + " "
		// + jaringDir.getPath() + "]");

		Process process = Runtime.getRuntime().exec(command);

		try {
			int wait = process.waitFor();
			if (wait != 0 || process.exitValue() != 0) {
				System.out
						.println("jaring process didn't terminate gracefully");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// System.out.println(outputJar.getAbsolutePath() + " file created in: "
		// + outputJar.getAbsolutePath());
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

	/**
	 * This method modifies an existing class to make it implement a given
	 * interface. This method doesn't add/create any new methods in the existing
	 * class, it assumes that the interface already contains one or methods with
	 * the same signature of the input class
	 * 
	 * @param theClass
	 * @param theInterface
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static void generateClassImplements(Class<?> theClass,
			Class<?> theInterface) throws ClassNotFoundException, IOException {

		// TODO: needs to change internal class implementation here
	}

	private static CtMethod[] generateFactoryMethod(CtClass ctDeclaring,
			Class<?>[] toFactoryClasses, Class<?> returnInterface)
			throws NotFoundException, CannotCompileException {
		CtMethod[] ctMethods = new CtMethod[toFactoryClasses.length];
		CtClass ctReturn = classPool.get(returnInterface.getName());

		for (int i = 0; i < ctMethods.length; i++) {
			String className = toFactoryClasses[i].getName();
			String methodName = "create" + className;
			String methodBody = generateFactorySourceCode(className);
			ctMethods[i] = CtNewMethod.make(Modifier.PUBLIC + Modifier.STATIC,
					ctReturn, methodName, new CtClass[0], new CtClass[0],
					methodBody, ctDeclaring);
		}

		return ctMethods;
	}

	private static String generateFactorySourceCode(String clazzName) {
		String code = "{";

		code += "return new " + clazzName + "();";
		return code;
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
	 * Generates a Java Factory Class for the given classes (which all must
	 * implement the same returned interface). <br>
	 * 
	 * The Java Factory will contain <code>public static</code> methods for the
	 * given <code>toFactoryClasses</code>. <br>
	 * 
	 * If <code>createJar</code> is true, then the Factory will be placed in a
	 * Jar Archive with the same name of the factory class, and the generated
	 * bytecode will be removed automatically.
	 * 
	 * @param toFactoryClasses
	 * @param returnInterfaceClass
	 * @param factoryFullName
	 * @return
	 */
	public static boolean generateJavaFactoryClass(Class<?>[] toFactoryClasses,
			Class<?> returnInterfaceClass, String factoryFullName,
			boolean createJar) {
		boolean generated = false;
		try {
			CtClass ctFactory = createCtClass(factoryFullName);

			CtMethod[] ctMethods = generateFactoryMethod(ctFactory,
					toFactoryClasses, returnInterfaceClass);
			for (CtMethod ctMethod : ctMethods) {
				ctFactory.addMethod(ctMethod);
			}

			ctFactory.writeFile(".");

			if (createJar) {
				createJarArchive(factoryFullName);
				cleanupBytecode(factoryFullName);
			}

			generated = true;
		} catch (Exception e) {
			e.printStackTrace();
			generated = false;
		}
		return generated;
	}

	/**
	 * Generates an interface based on a given class.<br>
	 * Interface methods are those directly declared as PUBLIC in the given
	 * class. <br>
	 * The <code>implementInterface</code> will indicate whether to modify the
	 * <code>inputClass</code> to implement the newly generated interface or
	 * not. <br>
	 * if <code>createJar</code> is true, then the interface is placed into a
	 * Jar Archive with the same name of the interface, and the generated
	 * bytecode will be removed automatically.
	 * 
	 * @param inputClass
	 * @param interfaceFullName
	 * @param implementInterface
	 * @param createJar
	 * @throws ClassNotFoundException
	 */
	public static boolean generateJavaInterface(Class<?> inputClass,
			String interfaceFullName, boolean implementInterface,
			boolean createJar) {
		boolean generated = false;
		try {
			CtClass ctInterface = createCtInterface(interfaceFullName);

			Method[] publicMethods = inputClass.getDeclaredMethods();
			for (Method method : publicMethods) {
				CtMethod ctMethod = generateInterfaceMethod(ctInterface, method);

				ctInterface.addMethod(ctMethod);
			}

			ctInterface.writeFile(".");

			if (implementInterface) {
				generateClassImplements(inputClass, ctInterface.toClass());
				cleanupBytecode(inputClass.getName());
			}

			if (createJar) {
				createJarArchive(interfaceFullName);
				cleanupBytecode(interfaceFullName);
			}

			generated = true;
		} catch (Exception e) {
			e.printStackTrace();
			generated = false;
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
