package ch.unibnf.scg.jseuss.core.javaassist;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class NewGuiceExprEditor extends ExprEditor {

	private CtClass ctInterfaceType;
	private CtField ctGuiceProvider;

	public NewGuiceExprEditor(CtClass ctInterface, CtField guiceProvider) {
		ctInterfaceType = ctInterface;
		ctGuiceProvider = guiceProvider;
	}

	public void edit(NewExpr e) throws CannotCompileException {

		try {
			// only replace new expr for ctInterface type; not any "new expr"!
			if (e.getClassName().equals(
					ctInterfaceType.getName())) {
				String replacement = "$_=" + "($r)" + ctGuiceProvider.getName() + ".get();";
				
				System.out.println(replacement);
				
				e.replace(replacement);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
