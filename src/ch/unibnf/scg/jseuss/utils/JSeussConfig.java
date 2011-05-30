package ch.unibnf.scg.jseuss.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class JSeussConfig {
	public static final String OUTPUT_DIR = "output/";
	public static final String GENERATED_PACKAGE_PREFIX = "generated.guice.";
	public static final String INTERFACE_PREFIX = "I";
	public static final Set<String> PACKAGE_IGNORE_LIST = new HashSet<String>(
		Arrays.asList("com.google.inject.", "org.aopalliance.", "java.", "org.junit.", "ch.unibnf.scg.jseuss.",
				"org.hamcrest.", "javassist.", "org.mockito.")
	);
}
