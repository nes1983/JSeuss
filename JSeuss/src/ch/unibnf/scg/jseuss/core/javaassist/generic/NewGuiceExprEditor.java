package ch.unibnf.scg.jseuss.core.javaassist.generic;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class NewGuiceExprEditor extends ExprEditor {

	public static final String INTERFACE_PREFIX = "I";
	private CtClass ctInterfaceType;
	private CtField ctGuiceInjector;

	public NewGuiceExprEditor(CtClass ctInterface, CtField guiceInjector) {
		ctInterfaceType = ctInterface;
		ctGuiceInjector = guiceInjector;
	}

	public void edit(NewExpr e) throws CannotCompileException {

		// only replace new expr for ctInterface type; not any "new expr"!
		String exprClassName = e.getClassName();
		System.out.println("exprClassName: " + exprClassName);
		System.out.println("ctInterfaceTypeName: " + ctInterfaceType.getName());
		//if (exprClassName.equals(ctInterfaceType.getName())) {
			//String replacement = "$_=" + "($r)" + ctGuiceInjector.getName()
			//		+ ".getInstance(" + ctInterfaceType.getName()
			//		+ ".class);";
		
		int lastIndexOfDot = exprClassName.lastIndexOf(".");
		String exprClassNamePrefix = "generated.guice." + exprClassName.substring(0, lastIndexOfDot);
		String exprClassNamePostfix = "";
		if(lastIndexOfDot != exprClassName.length()) {
			exprClassNamePostfix = "." + INTERFACE_PREFIX + exprClassName.substring(lastIndexOfDot+1, exprClassName.length());
		} else {
			exprClassNamePrefix = INTERFACE_PREFIX + exprClassNamePrefix;
		}
		
		String replacement = "$_=" + "(" + exprClassNamePrefix + exprClassNamePostfix + ")" + ctGuiceInjector.getName()+ ".get();";
		
		System.out.println(replacement);

		e.replace(replacement);
	}
}
