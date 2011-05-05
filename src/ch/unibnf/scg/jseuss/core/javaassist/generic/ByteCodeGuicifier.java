package ch.unibnf.scg.jseuss.core.javaassist.generic;

import javassist.ByteChanger;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.expr.Cast;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import ch.unibnf.scg.jseuss.utils.JSeussConfig;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;

/**
 * checks that methodcalls classname is not in ignorelist and
 * lets jseussbytechanger start its work  
 */
public class ByteCodeGuicifier extends ExprEditor {
	
	public void edit(MethodCall methodCall)	throws CannotCompileException {
		if(JSeussUtils.isInIgnoreList(methodCall.getClassName())) 
			return;
		
		System.out.println("METHODCALL NAME:\t" + methodCall.getMethodName());
		System.out.println("METHODCALL CLASSNAME:\t" + methodCall.getClassName());
			
		methodCall.changeBytes(new JSeussByteChanger());
	}
	
	public void edit(Cast cast) {
		try {
			if(JSeussUtils.isInIgnoreList(cast.getType().getName()))
				return;
			
			cast.changeBytes(new JSeussCastChanger());
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
		
	}
	
}

class JSeussCastChanger implements ByteChanger {

	@Override
	public void change(CodeIterator iterator, int currentPos,
			ConstPool constPool) throws BadBytecode {
		int i1 = iterator.byteAt(currentPos+1);
		int i2 = iterator.byteAt(currentPos+2); 
		int classInfoRef = (i1 << 8) | i2;
		String oldClass = constPool.getClassInfo(classInfoRef);
		String newClass = JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(oldClass);
		int classRef = constPool.addClassInfo(newClass);
		iterator.write(JSeussUtils.toByteArray(classRef, 2), currentPos+1);
	}
	
}