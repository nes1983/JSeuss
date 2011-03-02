package ch.unibnf.scg.jseuss.utils;

import java.io.File;
import java.io.IOException;

import ch.unibnf.scg.jseuss.core.javaassist.guice.ClassDetails;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class JSeussUtils {

	public static ClassDetails writeCtClass(CtClass ctClass) {
		ClassDetails details = new ClassDetails();
		try {
			String baseDir = "output/";
			String filepath = ctClass.getName().replace(".", "/");
			filepath = baseDir + filepath + ".class";

			// write the generated class on disk.
			try {
				ctClass.writeFile("./" + baseDir);
			} catch (Exception e1) {
				ctClass.defrost();
				String oldName = ctClass.getName();
				ctClass.setName("generated.modified." + ctClass.getSimpleName());
				ctClass.rebuildClassFile();
				ctClass.writeFile("./" + baseDir);
				System.out
						.println("Class: " + oldName + ", is renamed to: "
								+ ctClass.getName()
								+ " due to class reloading issues!");
			}

			File theFile = new File(filepath);

			Class<?> theClass = null;
			byte[] theBytes = null;
			try {
				theBytes = ctClass.toBytecode();
				theClass = ctClass.toClass();
			} catch (Exception e) {
				// e.printStackTrace();
			}

			details.setTheClass(theClass);
			details.setTheClassBytes(theBytes);
			details.setTheClassFile(theFile);

			details.setDone(true);

			System.out.println("generated: " + ctClass.getName());
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return details;
	}

	/**
	 * deletes the generated directories and class files for cleanup purposes as
	 * the .class files should be included in the jar file.
	 * 
	 * @param clazzFullName
	 */
	public static void cleanupBytecode(String clazzFullName) {

		File filez = new File(clazzFullName.replace('.', '/') + ".class");
		boolean isDeleted = filez.delete();
		if (isDeleted) {
			while ((filez = filez.getParentFile()) != null) {
				isDeleted = filez.delete();
				if (!isDeleted) {
					break;
				}
			}
		}
	}

	public static void createJarArchive(String jarname, String dirpath)
			throws IOException {

		jarname += jarname.endsWith(".jar") ? "" : ".jar";
		dirpath += dirpath.endsWith("/") ? "" : "/";
		File outputJar = new File(dirpath + jarname);
		String command = "jar cf " + outputJar.getAbsolutePath() + " "
				+ outputJar.getPath();
		Process process = Runtime.getRuntime().exec(command);

		try {
			int wait = process.waitFor();
			if (wait != 0 || process.exitValue() != 0) {
				System.out
						.println("jaring process didn't terminate gracefully");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * creates a Jar file for any CtClass entity, based on the full class and
	 * package path.<br>
	 * Jar file is generated in the same directory location.<br>
	 * Jar file name will be the same as givenClass or interface
	 * 
	 * @param ctclass
	 * @param clazzFullName
	 *            for example, com.unibnf.sample.MyClassOrInterface
	 * @throws IOException
	 * @throws CannotCompileException
	 */
	public static void createJarArchive(String clazzFullName)
			throws IOException {

		boolean isPackaged = false;
		String jarName = "";
		String jaringPath = "";
		if (clazzFullName.contains(".")) {
			// having a dot means we have package(s)
			isPackaged = true;
			jarName = clazzFullName
					.substring(clazzFullName.lastIndexOf('.') + 1) + ".jar";
			jaringPath = clazzFullName.substring(0,
					clazzFullName.lastIndexOf('.')).replace('.', '/');
		} else {
			isPackaged = false;
			jarName = clazzFullName + ".jar";
			jaringPath = clazzFullName;
		}
		// this path will contain which directories to generate the jar for.
		File jaringDir = new File(jaringPath);

		int packageIndex = 0;
		String outputJarPath = "";
		if (isPackaged) {
			packageIndex = jaringDir.getAbsolutePath().indexOf(
					jaringPath.substring(0, jaringPath.indexOf('/')));
			outputJarPath = jaringDir.getAbsolutePath().substring(0,
					packageIndex);
		} else {
			outputJarPath = jaringDir.getAbsolutePath();
			if (!outputJarPath.endsWith("/")) {
				outputJarPath += "/";
			}
		}
		// this is the full path where to generate the jar
		File outputJar = new File(outputJarPath + jarName);

		// this is the jar command to execute
		String command = "jar cf " + outputJar.getAbsolutePath() + " "
				+ jaringDir.getPath();

		// System.out
		// .println("Executing command: [" + "jar cvf "
		// + outputJar.getAbsolutePath() + " "
		// + jaringDir.getPath() + "]");

		Process process = Runtime.getRuntime().exec(command);

		try {
			int wait = process.waitFor();
			if (wait != 0 || process.exitValue() != 0) {
				System.out
						.println("jaring process didn't terminate gracefully, process.exitValue = "
								+ process.exitValue() + ", waitValue = " + wait);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// System.out.println(outputJar.getAbsolutePath() + " file created in: "
		// + outputJar.getAbsolutePath());
	}

}
