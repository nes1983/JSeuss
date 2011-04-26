package ch.unibnf.scg.jseuss.core.javaassist.generic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javassist.ByteChanger;
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
        if (c == Opcode.INVOKEINTERFACE || c == Opcode.INVOKESPECIAL) { 
        	return;
        }
        
        if(c == Opcode.INVOKESTATIC) {
        	return;
        	//XXX 
        	//assert false;
        }

        int numberOfBytesForInvokeVirtualInstruction = 3;
        int numberOfBytesForInvokeInterfaceInstruction = 5;
        int codeLength = iterator.getCodeLength();
        System.out.println("codelength: " + codeLength + " | pos: " + pos);

        byte[] bytesAfterInvokeVirtual = new byte[codeLength - pos - numberOfBytesForInvokeVirtualInstruction];
        for(int i=0; i<bytesAfterInvokeVirtual.length; i++) {
        	bytesAfterInvokeVirtual[i] = (byte) iterator.byteAt(i+pos+numberOfBytesForInvokeVirtualInstruction);
        }
        
        // code before
        System.out.println("before changing: ");
        for(int i=pos; i<iterator.getCodeLength(); i++)
        	System.out.print(Integer.toHexString(iterator.byteAt(i)) + " [" + iterator.byteAt(i) + "] ");
        
        System.out.println();
        for(int i=0; i < bytesAfterInvokeVirtual.length; i++)
        	System.out.print(Integer.toHexString(bytesAfterInvokeVirtual[i] & 0xFF) + " [" + (bytesAfterInvokeVirtual[i] & 0xFF) + "] ");
        System.out.println();
        
        // write invokeinterface byte
        iterator.writeByte(Expr.INVOKEINTERFACE, pos);
        System.out.println("i: " + constPool.getMethodrefClass(iterator.byteAt(pos+2)));
        System.out.println("i: " + constPool.getMethodrefClassName(iterator.byteAt(pos+2)));
        System.out.println("i: " + constPool.getMethodrefName(iterator.byteAt(pos+2)));
        System.out.println("i: " + constPool.getMethodrefNameAndType(iterator.byteAt(pos+2)));
        System.out.println("i: " + constPool.getMethodrefType(iterator.byteAt(pos+2)));
        
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
        iterator.writeByte(argumentCount, pos+3); //XXX unsafe position!!!

        System.out.println(pos+4 + " :: " + codeLength + " byteAt: " + iterator.byteAt(codeLength-1));
        if(pos+4 >= codeLength) {
        	iterator.append(new byte[] {0});
        	iterator.append(bytesAfterInvokeVirtual);
        }
        else {
	        iterator.writeByte(0, pos+4);
	        int availableByteCount = codeLength-pos-5;
	        int overlappingByteCount = bytesAfterInvokeVirtual.length - availableByteCount;
	        System.out.println("available bytes: " + availableByteCount);
	        System.out.println("bytes to append: " + overlappingByteCount);
	        
	        for(int i=0; i<availableByteCount; i++)
	        	iterator.writeByte(bytesAfterInvokeVirtual[i], i+pos+5);
	        
	        for(int i=0; i<overlappingByteCount; i++)
	        	iterator.append(new byte[] {bytesAfterInvokeVirtual[i+availableByteCount]});
        }
        
        System.out.println("new codelength: " + iterator.getCodeLength());
        
        // code after
        System.out.println("after changing: ");
        for(int i=pos; i<iterator.getCodeLength(); i++)
        	System.out.print(Integer.toHexString(iterator.byteAt(i)) + " [" + iterator.byteAt(i) + "] ");
        System.out.println();
	}

}
