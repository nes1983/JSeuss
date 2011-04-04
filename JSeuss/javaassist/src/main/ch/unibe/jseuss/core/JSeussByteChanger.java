package ch.unibe.jseuss.core;

import javassist.CannotCompileException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.expr.Expr;
import javassist.bytecode.Opcode;

/**
 * transforms the methodcall to jseuss compatible code
 * invokeinterface and call to the interfacetype
 * @throws CannotCompileException 
 * @throws BadBytecode 
 */
public class JSeussByteChanger implements ByteChanger {
	@Override
	public void change(CodeIterator iterator, int currentPos, ConstPool constPool)
			throws BadBytecode {
		int pos = currentPos;
		int c = iterator.byteAt(pos);
        if (c == Opcode.INVOKEINTERFACE) { 
        	return;
        }
        
        if(c == Opcode.INVOKESTATIC) {
        	//XXX 
        	assert false;
        }
 
        
        // write invokeinterface byte
        iterator.writeByte(Expr.INVOKEINTERFACE, pos);
        System.out.println(constPool.getMethodrefClass(iterator.byteAt(pos+2)));
        System.out.println(constPool.getMethodrefClassName(iterator.byteAt(pos+2)));
        System.out.println(constPool.getMethodrefName(iterator.byteAt(pos+2)));
        System.out.println(constPool.getMethodrefNameAndType(iterator.byteAt(pos+2)));
        System.out.println(constPool.getMethodrefType(iterator.byteAt(pos+2)));
        
        String arguments = constPool.getMethodrefType(iterator.byteAt(pos+2));
        int argumentCount = 1;
        for(int i=0; i<arguments.length(); i++)
        	if(arguments.charAt(i) == ';')
        		argumentCount++;
        
        String methodClass = constPool.getMethodrefClassName(iterator.byteAt(pos+2));
        String name = constPool.getMethodrefName(iterator.byteAt(pos+2));
        String type = constPool.getMethodrefType(iterator.byteAt(pos+2));
        
        int lastIndexOfDot = methodClass.lastIndexOf(".");
		String exprClassNamePrefix = "generated.guice." + methodClass.substring(0, lastIndexOfDot);
		String exprClassNamePostfix = "";
		if(lastIndexOfDot != methodClass.length()) {
			exprClassNamePostfix = "." + "I" + methodClass.substring(lastIndexOfDot+1, methodClass.length());
		} else {
			exprClassNamePrefix = "generated.guice." + exprClassNamePrefix;
		}
		
        String interfaceClass = exprClassNamePrefix + exprClassNamePostfix;
        int classinfo = constPool.addClassInfo(interfaceClass);
        int interfaceMethodrefInfo = constPool.addInterfaceMethodrefInfo(classinfo, name, type);
        iterator.writeByte(interfaceMethodrefInfo, pos+2);
        iterator.writeByte(argumentCount, pos+3);
        //iterator.writeByte(0, pos+4);
        //int nati = constPool.addNameAndTypeInfo("fsc", interfaceClass);
        //constPool.addFieldrefInfo(classinfo, nati);
        //iterator.insert(pos, new byte[] { (byte) Expr.GETFIELD, 0, (byte) nati});
        //iterator.append(new byte[] {0, Expr.POP, (byte) Expr.RETURN});
        iterator.append(new byte[] {0});
        
        
        for(int i=pos; i<iterator.getCodeLength(); i++)
        	System.out.print(Integer.toHexString(iterator.byteAt(i)) + " ");
	}

}
