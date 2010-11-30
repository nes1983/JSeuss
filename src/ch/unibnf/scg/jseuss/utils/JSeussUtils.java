package ch.unibnf.scg.jseuss.utils;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;

public class JSeussUtils {

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
						.println("jaring process didn't terminate gracefully");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// System.out.println(outputJar.getAbsolutePath() + " file created in: "
		// + outputJar.getAbsolutePath());
	}

}
