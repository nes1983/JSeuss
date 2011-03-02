package ch.unibnf.scg.jseuss.core.javaassist.generic;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.NewExpr;

public class FieldAccessExprEditor extends ExprEditor {

	private CtClass ctInterfaceType;
	private CtClass ctCurrentType;

	public FieldAccessExprEditor(CtClass ctCurrent, CtClass ctNew) {
		ctInterfaceType = ctNew;
		ctCurrentType = ctCurrent;
	}

	public void edit(FieldAccess e) throws CannotCompileException {

		try {
			// only replace new expr for ctInterface type; not any "new expr"!
			if (e.getField().getType().equals(
					ctCurrentType)) {
				String replacement = "$type=" + ctInterfaceType;
				
				System.out.println(replacement);
				
				e.replace(replacement);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
