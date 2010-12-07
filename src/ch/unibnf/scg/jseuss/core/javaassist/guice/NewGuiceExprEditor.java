package ch.unibnf.scg.jseuss.core.javaassist.guice;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class NewGuiceExprEditor extends ExprEditor {

	private CtClass ctInterfaceType;
	private CtField ctGuiceInjector;

	public NewGuiceExprEditor(CtClass ctInterface, CtField guiceInjector) {
		ctInterfaceType = ctInterface;
		ctGuiceInjector = guiceInjector;
	}

	public void edit(NewExpr e) throws CannotCompileException {

		try {
			// only replace new expr for ctInterface type; not any "new expr"!
			String exprClassName = e.getClassName();
			if (exprClassName.equals(ctInterfaceType.getName())) {
				String replacement = "$_=" + "($r) " + ctGuiceInjector.getName()
						+ ".getInstance(" + ctInterfaceType.getName()
						+ ".class);";
				e.replace(replacement);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
