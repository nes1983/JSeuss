package ch.unibnf.scg.jseuss.core.other.asm;

import java.io.File;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.ASMifierClassVisitor;

import ch.unibnf.scg.jseuss.utils.JSeussUtils;

public class JSeuss_ASM implements Opcodes {

	private static MethodNode createInterfaceMethodNode(Method amethod) {

		String[] exceptionNames = extractMethodExceptions(amethod
				.getExceptionTypes());

		MethodNode method = new MethodNode(ACC_PUBLIC | ACC_ABSTRACT,
				amethod.getName(), Type.getMethodDescriptor(amethod), null,
				exceptionNames);

		return method;
	}

	private static ClassNode createInterfaceNode(String interfaceFullName) {
		interfaceFullName = interfaceFullName != null ? interfaceFullName
				.replace('.', '/') : "";

		ClassNode cn = new ClassNode();
		cn.access = ACC_PUBLIC + ACC_INTERFACE;
		cn.name = interfaceFullName.trim();
		cn.superName = "java/lang/Object";
		return cn;
	}

	private static ClassNode createClassNode(String classFullName) {
		classFullName = classFullName != null ? classFullName.replace('.', '/')
				: "";

		ClassNode cn = new ClassNode();
		cn.access = ACC_PUBLIC;
		cn.name = classFullName.trim();
		cn.superName = "java/lang/Object";
		return cn;
	}

	private static String[] extractMethodExceptions(Class<?>[] exceptions) {
		String[] exceptionNames = null;
		if (exceptions != null && exceptions.length > 0) {
			exceptionNames = new String[exceptions.length];
			for (int i = 0; i < exceptions.length; i++) {
				exceptionNames[i] = exceptions[i].getName();
			}
		}
		return exceptionNames;
	}

	/**
	 * generates a java interface for a given class. The interface will only
	 * contain public methods specified by the parameter
	 * <code>specificMethods</code> which their name matches the givenClass
	 * method names. If <code>specificMethods</code> is null, the interface will
	 * contain all public methods exist in the given class.
	 * 
	 * @param givenClass
	 *            The class on which to extract public methods from.
	 * @param interfaceName
	 *            The name of the generated interface
	 * @param interfacePackage
	 *            The package of the generated interface
	 * @param specifcMethods
	 *            one or more method names
	 * @throws IOException
	 * @throws CannotCompileException
	 */
	public static boolean generateJavaInterface(Class<?> inputClass,
			String interfaceFullName, boolean implementInterface,
			boolean createJar) throws IOException {
		boolean generated = false;

		ClassNode newInterface = createInterfaceNode(interfaceFullName);

		Method[] inputMethods = inputClass.getDeclaredMethods();
		for (int i = 0; i < inputMethods.length; i++) {
			Method amethod = inputMethods[i];

			MethodNode newMethod = createInterfaceMethodNode(amethod);
			newInterface.methods.add(newMethod);
		}

		generated = writeClassFile(newInterface);

		if (implementInterface) {
			// generateClassImplements(inputClass, newInterface,
			// createJar);
		}

		return generated;
	}

	/**
	 * Generates a Java Factory Class for the given classes (which all must
	 * implement the same returned interface). <br>
	 * 
	 * The Java Factory will contain <code>public static</code> methods for the
	 * given <code>toFactoryClasses</code>. <br>
	 * 
	 * If <code>createJar</code> is true, then the Factory will be placed in a
	 * Jar Archive with the same name of the factory class, and the generated
	 * bytecode will be removed automatically.
	 * 
	 * @param toFactoryClasses
	 * @param returnInterfaceClass
	 * @param factoryFullName
	 * @return
	 */
	public static boolean generateJavaFactory(Class<?>[] toFactoryClasses,
			Class<?> returnInterfaceClass, String factoryFullName,
			boolean createJar) {
		boolean generated = false;
		try {
			ClassNode ctFactory = createClassNode(factoryFullName);

			MethodNode[] ctMethods = generateFactoryMethods(ctFactory,
					toFactoryClasses, returnInterfaceClass);
//			for (CtMethod ctMethod : ctMethods) {
//				ctFactory.addMethod(ctMethod);
//			}
//
//			ctFactory.writeFile(".");

			if (createJar) {
				JSeussUtils.createJarArchive(factoryFullName);
				JSeussUtils.cleanupBytecode(factoryFullName);
			}

			generated = true;
		} catch (Exception e) {
			e.printStackTrace();
			generated = false;
		}
		return generated;
	}

	private static ClassNode getClassNode(Class<?> aclass) throws IOException {
		ClassNode cn = new ClassNode();

		String name = aclass.getName().replace(".", "/");
		ClassReader cr = new ClassReader(name);
		cr.accept(cn, 0);

		return cn;
	}

	private static MethodNode[] generateFactoryMethods(ClassNode ctDeclaring,
			Class<?>[] toFactoryClasses, Class<?> returnInterface) throws IOException {
		MethodNode[] ctMethods = new MethodNode[toFactoryClasses.length];
		ClassNode ctReturn = getClassNode(returnInterface);

		for (int i = 0; i < toFactoryClasses.length; i++) {
			String fullClassName = toFactoryClasses[i].getName();
			String simpleClassName = toFactoryClasses[i].getSimpleName();
			String methodName = "create" + simpleClassName;
			MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC,
					methodName, Type.getDescriptor(returnInterface), null, null);
			InsnList instructions = new InsnList();
			TypeInsnNode newType = new TypeInsnNode(NEW, Type.getDescriptor(returnInterface));
			ctMethods[i] = methodNode;
		}

		return ctMethods;
	}

	public static void main(String[] args) {
		try {
			// generateJavaInterface(GermanSpellChecker.class,
			// "ch.generated.SpellCheckerInterface_ASM", true, true);
//			ASMifierClassVisitor
//					.main(new String[] { FrenchSpellCheckerFactory.class
//							.getName() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean writeClassFile(ClassNode cn) {
		boolean ok = false;
		ClassWriter cw = new ClassWriter(0);
		cn.accept(cw);
		byte[] byteCode = cw.toByteArray();

		try {
			String directories = "";
			if (cn.name.lastIndexOf("/") > -1) {
				// get packages
				directories = cn.name.substring(0, cn.name.lastIndexOf("/"));
			} else {
				// there is no packages, writing directly in current directory.
				directories = ".";
			}
			File dir = new File(directories);
			dir.mkdirs();
			System.out.println(dir.getAbsolutePath());
			System.out.println(dir.getCanonicalPath());
			System.out.println(dir.getPath());

			File binFile = new File(cn.name + ".class");
			FileOutputStream fw = new FileOutputStream(binFile);
			fw.write(byteCode);
			fw.flush();
			fw.close();
			ok = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		}

		return ok;
	}
}
