package ch.unibnf.scg.jseuss.utils;


public class JSeussUtils {
	
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
			exprClassNamePostfix = "." + JSeussConfig.INTERFACE_PREFIX + qualifiedClassName.substring(lastIndexOfDot+1, qualifiedClassName.length());
		} else {
			exprClassNamePrefix = JSeussConfig.INTERFACE_PREFIX + exprClassNamePrefix;
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
	
	/**
	 * add a package to the ignorelist (ex: "com.google.inject.")
	 * @param packagePrefix
	 */
	public static void addToIgnoreList(String packagePrefix) {
		JSeussConfig.PACKAGE_IGNORE_LIST.add(packagePrefix);
	}

	/**
	 * converts the first character of a string to lower case
	 * @param value
	 * @return
	 */
	public static String firstLetterToLowerCase(String value) {
		assert(value.length() > 0);
		value = Character.toLowerCase(value.charAt(0)) + (value.length() > 1 ? value.substring(1) : "");
		return value;
	}
	
	/**
	 * converts a value to a byte array (ex: toByteArray(20, 2) --> [0x00, 0x14])
	 * @param value
	 * @param nbOfBytes
	 * @return
	 */
	public static byte[] toByteArray(int value, int nbOfBytes) {
		byte[] result = new byte[nbOfBytes];
		for(int i=0; i<nbOfBytes; i++) {
			result[i] = (byte) (value >>> (8*(nbOfBytes-i-1)));
		}
		return result;
	}
}
