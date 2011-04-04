package ch.unibnf.scg.jseuss.core.javaassist.generic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

import javassist.CannotCompileException;
import javassist.ClassMap;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationsWriter;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import ch.unibe.jseuss.core.JSeussByteChanger;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;

import com.google.inject.Provider;

public class JSeussJavaassist {

	private static final String OUTPUT_DIR = "C:/cygwin/home/vs/students-ahmed-JSeuss/JSeuss/output/";
	private static ClassPool classPool = ClassPool.getDefault();

	public static CtClass factorizeToGuice(Class<?> containerClass, Class<?> currentClassType, Class<?> newInterfaceType) 
			throws NotFoundException, ClassNotFoundException, CannotCompileException, IOException {
		
		CtClass ctContainer = classPool.getCtClass(containerClass.getName());
		CtClass ctCurrentType = classPool //Remove variable
				.getCtClass(currentClassType.getName());
		CtClass ctNewInterfaceType = classPool.getCtClass(newInterfaceType.getName());
		CtClass providerClass = classPool
				.getCtClass(Provider.class.getName());

		String instanceVarName = newInterfaceType.getSimpleName().toLowerCase()
				+ "Provider";
		CtField providerField;

		try {
			providerField = ctContainer.getField(instanceVarName);
		} catch (NotFoundException e) {
			providerField = null;
		}
		
		if(providerField != null){
			if (providerField.getType().equals(providerClass)) {
				throw new RuntimeException(
						"instance variable with same name and [Guice type] exists, name: "
								+ instanceVarName);
			} else {
				throw new RuntimeException( //XXX Eigene Exception
						"instance variable with same name exists, name: "
								+ instanceVarName);
			}
		}
		
		// no instance variables exist with above name, good

		providerField = new CtField(providerClass, instanceVarName,
				ctContainer);
		providerField.setModifiers(Modifier.PROTECTED);
		ConstPool constPool = ctContainer.getClassFile().getConstPool();
		AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		Annotation a = new Annotation("com.google.inject.Inject", constPool);
		attr.setAnnotation(a);
		providerField.getFieldInfo().addAttribute(attr);
		//XXX Second L might have to be an [ in case of providers that provide arrays.
		SignatureAttribute sa = new SignatureAttribute(constPool, "Lcom/google/inject/Provider<L" + newInterfaceType.getName().replace('.', '/') +";>;");
		providerField.getFieldInfo().addAttribute(sa);
		
		ctContainer.addField(providerField);
		
		// look for any instance variables of type ctCurrentType
		CtField[] fields = ctContainer.getFields();
		boolean instanceVarFound = false;
		for (CtField ctField : fields) {
			System.out.println("inst. var: " + ctField.getType().getName());
			System.out.println("curr. type: " + ctCurrentType.getName());
			if (ctField.getType().equals(ctCurrentType)){
				ctField.setType(ctNewInterfaceType);
				instanceVarFound = true;
				break;
			}
		}
		
		if(instanceVarFound){
			CodeConverter converter = new CodeConverter();
			converter.replaceNew(ctCurrentType, ctNewInterfaceType);
			ctContainer.instrument(converter); 
		}

		// for all methods declared in ctContainer
		for (CtMethod method : ctContainer.getDeclaredMethods()) {
			System.out.println("method: " + method.getName());
			// first instrument the old data type
			CodeConverter converter = new CodeConverter();
			converter.replaceNew(ctCurrentType, ctNewInterfaceType);
			method.instrument(converter);
			
			//SV
			System.out.println("sign: " + method.getSignature());
			method.instrument(new ExprEditor() {
				public void edit(MethodCall methodCall)
				throws CannotCompileException {
					System.out.println("meth. name: " + methodCall.getMethodName());
					System.out.println("meth. class name: " + methodCall.getClassName());
					methodCall.changeBytes(new JSeussByteChanger());
				}
			});
			//SV
			
			// second replace the new expr with Guice provider get()
			method.instrument(new NewGuiceExprEditor(ctNewInterfaceType, providerField));
		}
		
		// for all constructors declared in ctContainer
		for(CtConstructor constructor : ctContainer.getConstructors()) {
			System.out.println("constructor: " + constructor.getName());
			// first instrument the old data type
			CodeConverter converter = new CodeConverter();
			converter.replaceNew(ctCurrentType, ctNewInterfaceType);
			constructor.instrument(converter);
			
			// second replace the new expr with Guice provider get()
			constructor.instrument(new NewGuiceExprEditor(ctNewInterfaceType, providerField));
		}
		
		return ctContainer;
	}
	
	public static void writeCtClass(CtClass ctClass) throws IOException, CannotCompileException {
		// write the modified class
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(ctClass.toBytecode()));
		ClassFile cf = new ClassFile(dis);
		cf.setVersionToJava5();
	
	//	URL url = ClassLoader.getSystemClassLoader().getResource("usecase/EmailSender.class"); //XXX
	//	File f = new File(url.getFile());
		File f = new File(OUTPUT_DIR + ctClass.getName().replace('.', '/') + ".class");
		cf.write(new DataOutputStream(new FileOutputStream(f)));
		
//		if (createJar) {
//			JSeussUtils.createJarArchive(containerClass.getName());
//			JSeussUtils.cleanupBytecode(containerClass.getName());
//		}
	}

	public static boolean factorizeLocalVariable(Class<?> containerClass,
			Class<?> localVariableClass, Class<?> newVariableClass,
			Class<?> factoryClass, String factoryMethod, boolean createJar)
			throws NotFoundException, ClassNotFoundException,
			CannotCompileException, IOException {
		boolean done = false;
		final CtClass ctContainer = classPool.getCtClass(containerClass
				.getName());
		CtClass ctCurrentType = classPool.getCtClass(localVariableClass
				.getName());
		CtClass ctNewType = classPool.getCtClass(newVariableClass.getName());
		CtClass ctFactory = classPool.getCtClass(factoryClass.getName());

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
			ctmethod.instrument(new NewExprEditor(ctCurrentType, ctNewType,
					ctFactory));
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
			ctMethods[i] = CtNewMethod.make(Modifier.PUBLIC | Modifier.STATIC,
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
						| Modifier.ABSTRACT);
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
