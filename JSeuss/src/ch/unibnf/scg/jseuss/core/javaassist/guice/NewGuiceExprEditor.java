package ch.unibnf.scg.jseuss.core.javaassist.guice;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class NewGuiceExprEditor extends ExprEditor {

	private CtClass ctInterfaceType;
	private CtClass ctCurrentType;
	private CtField injectorField;

	public NewGuiceExprEditor(CtClass ctCurrent, CtClass ctInterface,
			CtField guiceInjector) {
		ctInterfaceType = ctInterface;
		injectorField = guiceInjector;
		ctCurrentType = ctCurrent;
	}

	public void edit(NewExpr e) throws CannotCompileException {

		try {
			String theCurrentType = ctCurrentType.getName();
			String exprClassName = e.getClassName();
			if (exprClassName.equals(theCurrentType)) {
				// only replace new expr for ctInterface type; not any
				// "new expr"!
				String replacement = "$_= (" + (ctInterfaceType.getName()) + ")" + injectorField.getName()
						+ ".getInstance(" + ctInterfaceType.getName()
						+ ".class);";

				e.replace(replacement);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
