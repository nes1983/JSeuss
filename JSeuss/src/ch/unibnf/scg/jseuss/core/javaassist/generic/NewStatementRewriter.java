package ch.unibnf.scg.jseuss.core.javaassist.generic;

import java.util.Hashtable;

import ch.unibnf.scg.jseuss.utils.JSeussConfig;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

/**
 * checks that the created object is not in ignorelist and then replaces new expressions by a provider.get() method
 */
public class NewStatementRewriter extends ExprEditor {

	private CtField ctGuiceInjector;
	
	private Hashtable<String, CtField> providerFields;

	public NewStatementRewriter() {
		this.providerFields = new Hashtable<String, CtField>();
	}

	public void edit(NewExpr e) throws CannotCompileException {
		String createdObjectClassName = e.getClassName();
		
		if(JSeussUtils.isInIgnoreList(createdObjectClassName))
			return;
		
		if((this.ctGuiceInjector = this.providerFields.get(createdObjectClassName)) == null) {
			try {
				this.providerFields.put(createdObjectClassName, JSeussJavaassist.addProviderField(e.getEnclosingClass(), ClassPool.getDefault().get(JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(createdObjectClassName))));
			} catch (NotFoundException nfe) {
				throw new RuntimeException(nfe);
			}
			this.ctGuiceInjector = this.providerFields.get(createdObjectClassName);
		}
		
		String replacement = "$_=" + "(" + JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(createdObjectClassName) + ")" + ctGuiceInjector.getName()+ ".get();";
		
		System.out.println("REPLACEMENT:\t" + replacement);

		e.replace(replacement);
	}
	
	@Deprecated
	public NewStatementRewriter(CtClass ctInterface, CtField guiceInjector) {
		this.ctGuiceInjector = guiceInjector;
		this.providerFields = new Hashtable<String, CtField>();
	}
}
