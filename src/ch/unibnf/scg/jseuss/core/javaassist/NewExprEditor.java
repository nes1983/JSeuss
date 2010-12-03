package ch.unibnf.scg.jseuss.core.javaassist;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class NewExprEditor extends ExprEditor {

	private CtClass ctCurrentType;
	private CtClass ctNewType;
	private CtClass ctFactory;

	public NewExprEditor(CtClass ctCurrent, CtClass ctNew, CtClass ctFact) {
		ctCurrentType = ctCurrent;
		ctNewType = ctNew;
		ctFactory = ctFact;
	}

	public void edit(NewExpr e) throws CannotCompileException {
		// try {
		// String replacement = " (" + e.getClassName() + ") " +
		// e.getClassName() + "Factory" + ".get()"; // XXX helper method
		// factor
		// out
		String replacement = "$_=" + ctFactory.getName() + ".createInstance();";

		System.out.println(replacement);

		// CtField annotatedField = new CtField(e.getEnclosingClass()
		// .getClassPool().get("com.google.inject.Provider"),
		// e.getClassName() + "Factory", e.getEnclosingClass());

		// ConstPool constPool = ctContainer.getClassFile().getConstPool();
		// ByteArrayOutputStream output = new ByteArrayOutputStream();
		// AnnotationsWriter writer = new AnnotationsWriter(output,
		// constPool);
		// writer.numAnnotations(1);
		// writer.annotation("Inject", 0);
		// writer.memberValuePair("name");
		// writer.constValueIndex("chiba");
		// writer.close();
		// byte[] attribute_info = output.toByteArray();
		// AnnotationsAttribute anno
		// = new AnnotationsAttribute(constPool,
		// AnnotationsAttribute.visibleTag,
		// attribute_info);

		// annotatedField.setAttribute("Inject", attribute_info);
		// clazz.defrost();
		// e.getEnclosingClass().addField(annotatedField);
		e.replace(replacement);

		// clazz.writeFile(".");

		// } catch (NotFoundException nfe) {
		// throw new RuntimeException(nfe);
		// }
		// catch(IOException ioe) {
		// throw new RuntimeException(ioe);
		// }
	}
}
