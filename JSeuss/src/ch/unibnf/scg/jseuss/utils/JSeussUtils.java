package ch.unibnf.scg.jseuss.utils;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.CtClass;

public class JSeussUtils {
	
	public static final String INTERFACE_PREFIX = "I";

	public static Object writeCtClass(CtClass ctClass) {
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

			System.out.println("wrote output file: " + ctClass.getName());
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
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
			throw new RuntimeException(e);
		}

		// System.out.println(outputJar.getAbsolutePath() + " file created in: "
		// + outputJar.getAbsolutePath());
	}
	
	/**
	 * example: ch.unibe.jseuss.Test turns to ch.unibe.jseuss.ITest
	 * @param qualifiedClassName
	 * @return the qualified interface name
	 */
	public static String getQualifiedInterfaceName(String qualifiedClassName) {
		int lastIndexOfDot = qualifiedClassName.lastIndexOf(".");
		String exprClassNamePrefix = qualifiedClassName;
		if(lastIndexOfDot != -1)
			exprClassNamePrefix = exprClassNamePrefix.substring(0, lastIndexOfDot);
		String exprClassNamePostfix = "";
		if(lastIndexOfDot != qualifiedClassName.length()) {
			exprClassNamePostfix = "." + INTERFACE_PREFIX + qualifiedClassName.substring(lastIndexOfDot+1, qualifiedClassName.length());
		} else {
			exprClassNamePrefix = INTERFACE_PREFIX + exprClassNamePrefix;
		}
		
		String result = exprClassNamePrefix + exprClassNamePostfix;
		// XXX ?? Fix due to Javassist naming the main loading class with a dollar sign and a one behind "$1"
		// result = result.replace("$1", "");
		
		return result;
	}

	/**
	 * checks whether the className is in the PACKAGE_IGNORE_LIST
	 * @param className the fully qualified class name
	 * @return
	 */
	public static boolean isInIgnoreList(String className) {
		for(String toIgnore : JSeussConfig.PACKAGE_IGNORE_LIST)
			if(className.startsWith(toIgnore))
				return true;
		return false;
	}

	public static String firstLetterToLowerCase(String value) {
		String result = value;
		if(value.length() > 1)
			result = value.substring(0, 1).toLowerCase() + value.substring(1, value.length());
		else
			result = value.toLowerCase();
		return result;
	}

}
