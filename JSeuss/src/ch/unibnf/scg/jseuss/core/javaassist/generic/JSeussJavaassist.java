package ch.unibnf.scg.jseuss.core.javaassist.generic;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.annotation.Annotation;
import ch.unibnf.scg.jseuss.utils.JSeussConfig;
import ch.unibnf.scg.jseuss.utils.JSeussUtils;

import com.google.inject.Provider;

public class JSeussJavaassist {

	private static ClassPool classPool = ClassPool.getDefault();
	
	/**
	 * Guicifies a ctClass, that means it instruments method calls in declared methods and constructors.
	 * Plus it changes "new" calls to providercalls.
	 * @param target
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 */
	public static void guicify(CtClass target) throws NotFoundException, CannotCompileException {
		
		// for all methods declared in ctContainer
		for (CtMethod method : target.getDeclaredMethods()) {
			System.out.println("METHOD:\t" + method.getName());
			System.out.println("M.SIGN:\t" + method.getSignature());
			
			method.instrument(new ByteCodeGuicifier());
		}
		
		// for all constructors declared in ctContainer
		for(CtConstructor constructor : target.getConstructors()) {
			System.out.println("CONSTRUCTOR:\t" + constructor.getName());
			
			constructor.instrument(new ByteCodeGuicifier());
		}
		
		// replace the new expressions with Guice Provider get()
		target.instrument(new NewStatementRewriter());
	}
	
	/**
	 * 
	 * @param target
	 * @param interfaceClass
	 * @return the generated provider field (already attached to the target class!)
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	public static CtField addProviderField(CtClass target, CtClass interfaceClass) throws CannotCompileException, NotFoundException {
		CtClass providerClass = classPool.getCtClass(Provider.class.getName());
		String instanceVarName = JSeussUtils.firstLetterToLowerCase(interfaceClass.getSimpleName()) + "Provider";
		CtField providerField;
		
		try {
			providerField = target.getField(instanceVarName);
		} catch (NotFoundException e) {
			providerField = null;
		}
		
		if(providerField != null){
			if (providerField.getType().equals(providerClass)) {
				throw new RuntimeException(
						"instance variable with same name and [Guice type] exists, name: "
								+ instanceVarName);
			} else {
				throw new RuntimeException( //XXX Eigene Exception
						"instance variable with same name exists, name: "
								+ instanceVarName);
			}
		}
		
		// no instance variables exist with above name, good
		
		providerField = new CtField(providerClass, instanceVarName, target);
		// XXX add static modifier for static methods?? --> does this hurt?? YES!!
		providerField.setModifiers(Modifier.PROTECTED);
		
		ConstPool constPool = target.getClassFile().getConstPool();
		AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		Annotation a = new Annotation("com.google.inject.Inject", constPool);
		attr.setAnnotation(a);
		providerField.getFieldInfo().addAttribute(attr);
		//XXX Second L might have to be an [ in case of providers that provide arrays.
		SignatureAttribute sa = new SignatureAttribute(constPool, "Lcom/google/inject/Provider<L" + interfaceClass.getName().replace('.', '/') +";>;");
		providerField.getFieldInfo().addAttribute(sa);
		
		target.addField(providerField);
		return providerField;
	}
	
	/**
	 * write a ctClass to disk
	 * @param ctClass
	 * @throws IOException
	 * @throws CannotCompileException
	 */
	public static void writeCtClass(CtClass ctClass) throws IOException, CannotCompileException {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(ctClass.toBytecode()));
		ClassFile cf = new ClassFile(dis);
		cf.setVersionToJava5();
	
		File f = new File(JSeussConfig.OUTPUT_DIR + ctClass.getName().replace('.', '/') + ".class");
		cf.write(new DataOutputStream(new FileOutputStream(f)));
		
		System.out.println("WROTE FILE TO DISK:\t" + cf.getName());
	}
	
}
