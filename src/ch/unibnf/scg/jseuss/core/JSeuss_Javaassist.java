package ch.unibnf.scg.jseuss.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.AnnotationsWriter;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class JSeuss_Javaassist {

	private static ClassPool classPool = ClassPool.getDefault();

	public static boolean factorizeLocalVariable(Class<?> containerClass,
			Class<?> localVariableClass, Class<?> newVariableClass,
			Class<?> factoryClass, String factoryMethod, boolean createJar)
			throws NotFoundException, ClassNotFoundException,
			CannotCompileException, IOException {
		boolean done = false;
		final CtClass ctContainer = classPool.getCtClass(containerClass
				.getName());
		// ctContainer.stopPruning(true);
		CtClass ctCurrentType = classPool.getCtClass(localVariableClass
				.getName());
		// ctContainer.stopPruning(true);
		CtClass ctNewType = classPool.getCtClass(newVariableClass.getName());
		// ctContainer.stopPruning(true);
		CtClass ctFactory = classPool.getCtClass(factoryClass.getName());
		// ctContainer.stopPruning(true);

		CtField[] fields = ctContainer.getDeclaredFields();
		CtField field = null;
		String name = "";
		for (int i = 0; i < fields.length; i++) {
			field = fields[i];
			name = field.getName();
			if (field.getType().equals(ctCurrentType)) {
				field.setType(ctNewType);
				// ctContainer.removeField(field);
				break;
			}
		}
		CtField newField = new CtField(ctNewType, name + 2, ctContainer);
		newField.setModifiers(Modifier.PUBLIC);
		ctContainer.addField(newField,
				CtField.Initializer.byCall(ctFactory, factoryMethod, null));
		// // redirect all current methods to the new methods
		// CtMethod[] ctCurrents = ctCurrentType.getDeclaredMethods();
		// for (int i = 0; i < ctCurrents.length; i++) {
		// CtMethod currMethod = ctCurrents[i];
		// String mname = currMethod.getName();
		// CtMethod newMethod = ctNewType.getDeclaredMethod(mname);
		// if(newMethod != null){
		// // CodeConverter converter = new CodeConverter();
		// // converter.replaceNew(ctCurrentType, ctNewType);
		// // ctContainer.instrument(converter);
		// ctContainer.instrument(new NewExprEditor(ctCurrentType, ctNewType));
		// }
		// }

		CtMethod[] ctMethods = ctContainer.getDeclaredMethods();
		for (int i = 0; i < ctMethods.length; i++) {
			CtMethod ctmethod = ctMethods[i];
			ctmethod.instrument(new NewExprEditor(ctCurrentType, ctNewType, ctFactory));
		}

		ctContainer.writeFile(".");

		if (createJar) {
			JSeussUtils.createJarArchive(containerClass.getName());
			JSeussUtils.cleanupBytecode(containerClass.getName());
		}

		done = true;
		return done;
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
			Class<?> theInterface, boolean createJar)
			throws ClassNotFoundException, IOException {

		// TODO: needs to change internal class implementation here
	}

	private static CtMethod[] generateFactoryMethods(CtClass ctDeclaring,
			Class<?>[] toFactoryClasses, Class<?> returnInterface)
			throws NotFoundException, CannotCompileException {
		CtMethod[] ctMethods = new CtMethod[toFactoryClasses.length];
		CtClass ctReturn = classPool.getCtClass(returnInterface.getName());

		for (int i = 0; i < ctMethods.length; i++) {
			String fullClassName = toFactoryClasses[i].getName();
			String simpleClassName = toFactoryClasses[i].getSimpleName();
			String methodName = "create" + simpleClassName;
			String methodBody = generateFactorySourceCode(fullClassName);
			ctMethods[i] = CtNewMethod.make(Modifier.PUBLIC + Modifier.STATIC,
					ctReturn, methodName, new CtClass[0], new CtClass[0],
					methodBody, ctDeclaring);
		}

		return ctMethods;
	}

	private static String generateFactorySourceCode(String clazzName) {
		String code = "{";

		code += "return new " + clazzName + "();";

		code += "}";
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
	public static boolean generateJavaFactory(Class<?>[] toFactoryClasses,
			Class<?> returnInterfaceClass, String factoryFullName,
			boolean createJar) {
		boolean generated = false;
		try {
			CtClass ctFactory = createCtClass(factoryFullName);

			CtMethod[] ctMethods = generateFactoryMethods(ctFactory,
					toFactoryClasses, returnInterfaceClass);
			for (CtMethod ctMethod : ctMethods) {
				ctFactory.addMethod(ctMethod);
			}

			ctFactory.writeFile(".");

			if (createJar) {
				JSeussUtils.createJarArchive(factoryFullName);
				JSeussUtils.cleanupBytecode(factoryFullName);
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
				generateClassImplements(inputClass, ctInterface.toClass(),
						createJar);
			}

			if (createJar) {
				JSeussUtils.createJarArchive(interfaceFullName);
				JSeussUtils.cleanupBytecode(interfaceFullName);
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
