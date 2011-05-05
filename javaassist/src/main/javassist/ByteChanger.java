package javassist;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

public interface ByteChanger {

	void change(CodeIterator iterator, int currentPos, ConstPool constPool) throws BadBytecode;

}
