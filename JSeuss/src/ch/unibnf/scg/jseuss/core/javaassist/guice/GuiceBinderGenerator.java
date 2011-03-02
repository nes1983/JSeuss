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

import com.google.inject.AbstractModule;

public class GuiceBinderGenerator {

	private static ClassPool classPool = ClassPool.getDefault();

	private static CtClass createCtClass(String classFullName) {
		CtClass ctClass = classPool.makeClass(classFullName);
		ctClass.setModifiers(Modifier.PUBLIC);
		return ctClass;
	}

	/**
	 * Generates a Guice Binder Class for the given provider, interface classes. <br>
	 * 
	 * 
	 * If <code>createJar</code> is true, then the Binder will be placed in a
	 * Jar Archive with the same name of the binder class, and the generated
	 * bytecode will be removed automatically.
	 * 
	 * @param concreteClasses
	 * @param returnInterfaceClass
	 * @param binderFullName
	 * @return
	 */
	public static ClassDetails generate(Class<?> providerClass,
			Class<?> returnInterfaceClass, String binderFullName,
			boolean createJar) {
		ClassDetails generated = null;
		try {
			CtClass ctBinder = createCtClass(binderFullName);
			CtClass abstractModule = classPool.get(AbstractModule.class
					.getName());
			ctBinder.setSuperclass(abstractModule);

			CtConstructor[] constructors = ctBinder.getConstructors();
			if (constructors != null && constructors.length > 0) {
				for (CtConstructor ctConstructor : constructors) {
					ctConstructor.setBody("System.out.println();");
				}
			} else {
				String body = "public " + ctBinder.getSimpleName()
						+ "(){System.out.println();}";
				CtConstructor constructor = CtNewConstructor.make(body,
						ctBinder);
				ctBinder.addConstructor(constructor);
			}

			CtMethod ctMethod = generateBinderMethod(ctBinder, providerClass,
					returnInterfaceClass);
			ctBinder.addMethod(ctMethod);

			ctBinder.rebuildClassFile();
			generated = JSeussUtils.writeCtClass(ctBinder);

			if (createJar) {
				JSeussUtils.createJarArchive(binderFullName);
				// JSeussUtils.cleanupBytecode(binderFullName);
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

	private static CtMethod generateBinderMethod(CtClass ctDeclaring,
			Class<?> providerClass, Class<?> returnInterface)
			throws NotFoundException, CannotCompileException {

		CtMethod ctMethod = null;
		// CtClass ctReturn = classPool.getCtClass(Void.class.getName());
		// CtClass ctParam = classPool.getCtClass(Binder.class.getName());
		String methodSignature = "protected void";
		String methodBody = generateBinderMethodSource(providerClass,
				returnInterface);
		String methodName = "configure()";

		// ctMethod = CtNewMethod.make(Modifier.PROTECTED, ctReturn, methodName,
		// new CtClass[0], new CtClass[0], methodBody,
		// ctDeclaring);
		String methodSrc = methodSignature + " " + methodName + "{"
				+ methodBody + "}";
		ctMethod = CtNewMethod.make(methodSrc, ctDeclaring);

		return ctMethod;
	}

	private static String generateBinderMethodSource(Class<?> providerClass,
			Class<?> returnInterface) {
		// String methodBody = "= $1.bind(" + returnInterface.getName()
		// + ".class).toProvider(" + providerClass.getName()
		// + ".class); return $r";
		String methodBody = "com.google.inject.binder.AnnotatedBindingBuilder builder = bind("
				+ returnInterface.getName()
				+ ".class);"
				+ " builder.toProvider(" + providerClass.getName() + ".class);";
		return methodBody;
	}
}
