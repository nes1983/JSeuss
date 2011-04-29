package ch.unibnf.scg.jseuss.core.javaassist.generic;

import ch.unibnf.scg.jseuss.utils.JSeussConfig;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;
import javassist.ByteChanger;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Opcode;
import javassist.expr.Expr;

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
        int codeLength = iterator.getCodeLength();
        System.out.println("BC codelength:\t" + codeLength + " | pos: " + pos);

        byte[] bytesAfterInvokeVirtual = new byte[codeLength - pos - numberOfBytesForInvokeVirtualInstruction];
        for(int i=0; i<bytesAfterInvokeVirtual.length; i++) {
        	bytesAfterInvokeVirtual[i] = (byte) iterator.byteAt(i+pos+numberOfBytesForInvokeVirtualInstruction);
        }
        
        // ******* DEBUG INFO
        // code before
        System.out.println("BC before changing:\t");
        for(int i=pos; i<iterator.getCodeLength(); i++)
        	System.out.print(Integer.toHexString(iterator.byteAt(i)) + " [" + iterator.byteAt(i) + "] ");
        
        System.out.println();
        for(int i=0; i < bytesAfterInvokeVirtual.length; i++)
        	System.out.print(Integer.toHexString(bytesAfterInvokeVirtual[i] & 0xFF) + " [" + (bytesAfterInvokeVirtual[i] & 0xFF) + "] ");
        System.out.println();
        // *******
        
        // write invokeinterface byte
        iterator.writeByte(Expr.INVOKEINTERFACE, pos);
        
        int methodrefIndex = iterator.byteAt(pos+1) + iterator.byteAt(pos+2);
        System.out.println("BC i:\t" + constPool.getMethodrefClass(methodrefIndex));
        System.out.println("BC i:\t" + constPool.getMethodrefClassName(methodrefIndex));
        System.out.println("BC i:\t" + constPool.getMethodrefName(methodrefIndex));
        System.out.println("BC i:\t" + constPool.getMethodrefNameAndType(methodrefIndex));
        System.out.println("BC i:\t" + constPool.getMethodrefType(methodrefIndex));
        
        String arguments = constPool.getMethodrefType(methodrefIndex);
        int argumentCount = 1;
        for(int i=0; i<arguments.length(); i++)
        	if(arguments.charAt(i) == ';')
        		argumentCount++;
        
        String methodClass = constPool.getMethodrefClassName(methodrefIndex);
        String name = constPool.getMethodrefName(methodrefIndex);
        String type = constPool.getMethodrefType(methodrefIndex);
        
        String interfaceClass = JSeussConfig.GENERATED_PACKAGE_PREFIX + JSeussUtils.getQualifiedInterfaceName(methodClass);
        int classinfo = constPool.addClassInfo(interfaceClass);
        int interfaceMethodrefInfo = constPool.addInterfaceMethodrefInfo(classinfo, name, type); //XXX could be a value greater than 256 (1byte)!!
        byte[] interfaceMethodrefInfoBytes = JSeussUtils.toByteArray(interfaceMethodrefInfo, 2);
        iterator.write(interfaceMethodrefInfoBytes, pos+1);
        
        // pos+3 = ARGUMENT COUNT
        if(pos+3 >= codeLength)
        	iterator.append(new byte[] {(byte)argumentCount});
        else
        	iterator.writeByte(argumentCount, pos+3);

        if(pos+4 >= codeLength) {
        	iterator.append(new byte[] {0});
        	iterator.append(bytesAfterInvokeVirtual);
        }
        else {
	        iterator.writeByte(0, pos+4);
	        int availableByteCount = codeLength-pos-5;
	        int overlappingByteCount = bytesAfterInvokeVirtual.length - availableByteCount;
	        System.out.println("BC available bytes:\t" + availableByteCount);
	        System.out.println("BC bytes to append:\t" + overlappingByteCount);
	        
	        for(int i=0; i<availableByteCount; i++)
	        	iterator.writeByte(bytesAfterInvokeVirtual[i], i+pos+5);
	        
	        for(int i=0; i<overlappingByteCount; i++)
	        	iterator.append(new byte[] {bytesAfterInvokeVirtual[i+availableByteCount]});
        }
        
        System.out.println("BC new codelength:\t" + iterator.getCodeLength());
        
        // code after
        System.out.println("BC after changing:\t");
        for(int i=pos; i<iterator.getCodeLength(); i++)
        	System.out.print(Integer.toHexString(iterator.byteAt(i)) + " [" + iterator.byteAt(i) + "] ");
        System.out.println();
	}

}
