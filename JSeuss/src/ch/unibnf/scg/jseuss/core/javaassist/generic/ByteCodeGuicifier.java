package ch.unibnf.scg.jseuss.core.javaassist.generic;

import ch.unibnf.scg.jseuss.utils.JSeussUtils;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * checks that methodcalls classname is not in ignorelist and
 * lets jseussbytechanger start its work  
 */
public class ByteCodeGuicifier extends ExprEditor {
	
	public void edit(MethodCall methodCall)	throws CannotCompileException {
		if(!JSeussUtils.isInIgnoreList(methodCall.getClassName())) {
			System.out.println("METHODCALL NAME:\t" + methodCall.getMethodName());
			System.out.println("METHODCALL CLASSNAME:\t" + methodCall.getClassName());
			
			methodCall.changeBytes(new JSeussByteChanger());
		}
	}
	
}
