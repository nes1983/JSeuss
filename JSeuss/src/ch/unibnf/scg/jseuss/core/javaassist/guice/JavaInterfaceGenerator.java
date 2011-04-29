package ch.unibnf.scg.jseuss.core.javaassist.guice;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

public class JavaInterfaceGenerator {

	private static ClassPool classPool = ClassPool.getDefault();

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
		CtClass ctReturn = classPool.get(name);
		return ctReturn;
	}
	
	private static CtClass[] getMethodParameters(CtMethod method)	throws NotFoundException {
		CtClass[] params = method.getParameterTypes();
		CtClass[] ctParams = new CtClass[params.length];
		for (int i = 0; i < params.length; i++) {
			CtClass param = params[i];
			String name = param.getName();
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
