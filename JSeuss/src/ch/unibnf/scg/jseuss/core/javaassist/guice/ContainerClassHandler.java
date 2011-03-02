package ch.unibnf.scg.jseuss.core.javaassist.guice;

import java.io.IOException;

import javax.management.RuntimeErrorException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ch.unibnf.scg.jseuss.utils.JSeussUtils;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

public class ContainerClassHandler {

	private static ClassPool classPool = ClassPool.getDefault();

	public static ClassDetails makeGuiceCompliant(Class<?> containerClass,
			Class<?> currentClassType, Class<?> newInterfaceType,
			Class<?> guiceBinder, boolean createJar) {
		ClassDetails generated = null;
		try {
			CtClass ctContainer = classPool
					.getCtClass(containerClass.getName());
			CtClass ctCurrentType = classPool.getCtClass(currentClassType
					.getName());
			CtClass ctNewType = classPool
					.getCtClass(newInterfaceType.getName());
			CtClass ctGuiceBinder = classPool.getCtClass(guiceBinder.getName());

			CtField binderField = createGuiceBinder(ctContainer, ctGuiceBinder);
			CtField injectorField = createGuiceInjector(ctContainer,
					binderField);

			ctContainer.addField(binderField);
			ctContainer.addField(injectorField);

			createConstructor(ctContainer, ctGuiceBinder, binderField,
					injectorField);

			// look for any instance variables of type ctCurrentType
			// and change it to ctNewType
			CtField[] fields = ctContainer.getFields();
			boolean instanceVarFound = false;
			for (CtField ctField : fields) {
				if (ctField.getType().equals(ctCurrentType)) {
					ctField.setType(ctNewType);
					instanceVarFound = true;
					break;
				}
			}

			/* change the way of initializing instance variables */
			if (instanceVarFound) {
				// CodeConverter converter = new CodeConverter();
				// converter.replaceNew(ctCurrentType, ctNewType);
				// ctContainer.instrument(converter);

				ctContainer.instrument(new NewGuiceExprEditor(ctCurrentType,
						ctNewType, injectorField));
			}

			// if (instanceVarFound) {
			// for (CtMethod method : ctContainer.getDeclaredMethods()) {
			// // method.instrument(new NewGuiceExprEditor(ctNewType,
			// // injectorField));
			// method.addLocalVariable("injectorreplacement", ctNewType);
			// method.insertBefore("injectorreplacement = "
			// + injectorField.getName() + ".getInstance("
			// + ctNewType.getName() + ".class);");
			// final String theCurrentType = ctCurrentType.getName();
			// method.instrument(new ExprEditor() {
			// @Override
			// public void edit(NewExpr e)
			// throws CannotCompileException {
			// String exprClassName = e.getClassName();
			// if (exprClassName.equals(theCurrentType)) {
			// String replacement = "$_= injectorreplacement;";
			// e.replace(replacement);
			// }
			// }
			//
			// @Override
			// public void edit(FieldAccess f)
			// throws CannotCompileException {
			// System.out.println(f.getFieldName()
			// + "is being accessed");
			// super.edit(f);
			// }
			//
			// @Override
			// public void edit(MethodCall m)
			// throws CannotCompileException {
			// System.out.println("classname=" + m.getClassName());
			// System.out.println("filename=" + m.getFileName());
			// System.out.println("methodname="
			// + m.getMethodName());
			// super.edit(m);
			// }
			// });
			// }
			// }

			/* for all methods declared in ctContainer */
			// for (CtMethod method : ctContainer.getDeclaredMethods()) {
			// // method.instrument(new NewGuiceExprEditor(ctNewType,
			// // injectorField));
			// if (instanceVarFound) {
			// method.addLocalVariable("injectorreplacement", ctNewType);
			// method.insertBefore("injectorreplacement = "
			// + injectorField.getName() + ".getInstance("
			// + ctNewType.getName() + ".class);");
			// final String theNewType = ctCurrentType.getName();
			// method.instrument(new ExprEditor() {
			// @Override
			// public void edit(NewExpr e)
			// throws CannotCompileException {
			// String exprClassName = e.getClassName();
			// if (exprClassName.equals(theNewType)) {
			// String replacement = "$_= injectorreplacement;";
			// e.replace(replacement);
			// }
			// }
			// });
			// }
			// }

			// write the modified class
			ctContainer.rebuildClassFile();
			generated = JSeussUtils.writeCtClass(ctContainer);
			if (createJar) {
				JSeussUtils.createJarArchive(containerClass.getName());
				// JSeussUtils.cleanupBytecode(containerClass.getName());
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

	private static void createConstructor(CtClass ctContainer,
			CtClass ctGuiceBinder, CtField binderField, CtField injectorField)
			throws CannotCompileException, NotFoundException {
		String constructorSrc = "";

		String constructBinder = "dummyBinder" + " = new "
				+ ctGuiceBinder.getName() + "();";
		String constructInjector = "dummyInjector" + " = "
				+ Guice.class.getName()
				+ ".createInjector( new com.google.inject.Module[]{"
				+ "dummyBinder" + "});";

		String binderEqual = binderField.getName() + " = dummyBinder;";
		String injectorEqual = injectorField.getName() + " = dummyInjector;";

		CtConstructor[] inits = ctContainer.getConstructors();
		if (inits != null && inits.length > 0) {
			for (CtConstructor ctConstructor : inits) {
				ctConstructor.addLocalVariable("dummyBinder",
						binderField.getType());
				ctConstructor.addLocalVariable("dummyInjector",
						injectorField.getType());
				constructorSrc = "{" + constructBinder + constructInjector
						+ "}";
				ctConstructor.insertAfter(constructorSrc);

				constructorSrc = "{" + binderEqual + "\n" + injectorEqual
						+ "};";
				ctConstructor.insertAfter(constructorSrc);
			}
		} else {
			String constructorPrefix = "public " + ctContainer.getSimpleName()
					+ "(){";
			String constructorSuffix = "}";

			// create a new constructor
			constructorSrc = constructorPrefix + constructBinder
					+ constructInjector + constructorSuffix;
			CtConstructor constructor = CtNewConstructor.make(constructorSrc,
					ctContainer);
			ctContainer.addConstructor(constructor);
		}
	}

	private static CtField createGuiceInjector(CtClass ctContainer,
			CtField binderField) throws CannotCompileException,
			NotFoundException {

		// create necessary Giuce CtClasses
		CtClass ctGuiceInjector = classPool
				.getCtClass(Injector.class.getName());

		// create an injector instance variable
		String injectorVarName = "guiceInjector";
		CtField injectorField = null;

		try {
			injectorField = ctContainer.getField(injectorVarName);
		} catch (NotFoundException e) {
			injectorField = null;
		}

		// no instance variables exist with above name, good
		if (injectorField == null) {
			injectorField = new CtField(ctGuiceInjector, injectorVarName,
					ctContainer);
			injectorField.setModifiers(Modifier.PRIVATE);
		} else if (injectorField.getType().equals(ctGuiceInjector)) {
			throw new RuntimeException(
					"instance variable with same name and [Guice type] exists, name: "
							+ injectorVarName);
		} else {
			throw new RuntimeException(
					"instance variable with same name exists, name: "
							+ injectorVarName);
		}
		return injectorField;
	}

	private static CtField createGuiceBinder(CtClass ctContainer,
			CtClass ctGuiceBinder) throws CannotCompileException,
			NotFoundException {
		String binderVarName = "guiceBinder";
		CtField binderField = null;
		try {
			binderField = ctContainer.getField(binderVarName);
		} catch (NotFoundException e) {
			binderField = null;
		}

		// no instance variables exist with above name, good
		if (binderField == null) {
			binderField = new CtField(ctGuiceBinder, binderVarName, ctContainer);
			binderField.setModifiers(Modifier.PRIVATE);
		} else if (binderField.getType().equals(ctGuiceBinder)) {
			throw new RuntimeException(
					"instance variable with same name and [Guice type] exists, name: "
							+ binderVarName);
		} else {
			throw new RuntimeException(
					"instance variable with same name exists, name: "
							+ binderVarName);
		}
		return binderField;
	}
}
