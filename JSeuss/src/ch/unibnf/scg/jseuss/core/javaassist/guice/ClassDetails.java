package ch.unibnf.scg.jseuss.core.javaassist.guice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClassDetails {

	private Class<?> theClass;
	private File theClassFile;
	private byte[] theClassBytes;
	private boolean done;

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
	
	public ClassDetails() {
		
	}

	public ClassDetails(Class<?> aClass) {
		setTheClass(aClass);
		setTheClassBytes(getBytes(aClass));
	}

	public Class<?> getTheClass() {
		return theClass;
	}

	public void setTheClass(Class<?> theClass) {
		this.theClass = theClass;
	}

	public File getTheClassFile() {
		return theClassFile;
	}

	public void setTheClassFile(File classFile) {
		this.theClassFile = classFile;
	}

	public byte[] getTheClassBytes() {
		return theClassBytes;
	}

	public void setTheClassBytes(byte[] classBytes) {
		this.theClassBytes = classBytes;
	}

	public static byte[] getBytes(Object obj) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			bos.close();
			byte[] data = bos.toByteArray();
			return data;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
