package ch.unibnf.scg.jseuss.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JSeussConfig {
	public static final String OUTPUT_DIR = "output/";
	public static final String GENERATED_PACKAGE_PREFIX = "generated.guice.";
	public static final String INTERFACE_PREFIX = "I";
	public static final List<String> PACKAGE_IGNORE_LIST = new ArrayList<String>(
		Arrays.asList("com.google.inject.", "org.aopalliance.", "java.")
	);
}
