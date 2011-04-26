package ch.unibnf.scg.jseuss.core.javaassist.generic;

import java.util.Hashtable;

import ch.unibnf.scg.jseuss.utils.JSeussConfig;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.ConstPool;
import javassist.bytecode.stackmap.TypeData.ClassName;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class NewGuiceExprEditor extends ExprEditor {

	@Deprecated
	private CtClass ctInterfaceType;
	private CtField ctGuiceInjector;
	
	private Hashtable<String, CtField> providerFields;

	public NewGuiceExprEditor() {
		this.providerFields = new Hashtable<String, CtField>();
	}
	
	@Deprecated
	public NewGuiceExprEditor(CtClass ctInterface, CtField guiceInjector) {
		ctInterfaceType = ctInterface;
		this.ctGuiceInjector = guiceInjector;
		this.providerFields = new Hashtable<String, CtField>();
	}

	public void edit(NewExpr e) throws CannotCompileException {
		String createdObjectClassName = e.getClassName();
		
		//exclude $... in classnames XXX
		if(createdObjectClassName.contains("$") || JSeussUtils.isInIgnoreList(createdObjectClassName))
			return;
		
		if((this.ctGuiceInjector = this.providerFields.get(createdObjectClassName)) == null) {
			try {
				System.out.println("--> " + JSeussUtils.getQualifiedInterfaceName(createdObjectClassName));
				this.providerFields.put(createdObjectClassName, JSeussJavaassist.addProviderField(e.getEnclosingClass(), ClassPool.getDefault().get(JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(createdObjectClassName))));
			} catch (NotFoundException e1) {
				System.out.println(e1.getMessage()); //XXX
			}
			this.ctGuiceInjector = this.providerFields.get(createdObjectClassName);
		}
		
		// only replace new expr for ctInterface type; not any "new expr"!
		System.out.println("exprClassName: " + createdObjectClassName);
		// check can be removed as soon as the deprecated constructor vanishes
		if(ctInterfaceType != null)
			System.out.println("ctInterfaceTypeName: " + ctInterfaceType.getName());
		
		//if (exprClassName.equals(ctInterfaceType.getName())) {
			//String replacement = "$_=" + "($r)" + ctGuiceInjector.getName()
			//		+ ".getInstance(" + ctInterfaceType.getName()
			//		+ ".class);";
		
		String replacement = "$_=" + "(" + JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(createdObjectClassName) + ")" + ctGuiceInjector.getName()+ ".get();";
		
		System.out.println(replacement);

		e.replace(replacement);
	}
}
