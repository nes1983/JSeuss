package ch.unibnf.scg.jseuss.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTestPatternMatcher {
	public static final String EXAMPLE_TEST = "Lg/f/ELmail<Lg/f/ELPacman;>;";

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("L.[^(<|;)]+");
		Matcher matcher = pattern.matcher(EXAMPLE_TEST);
		// Check all occurrences
		while (matcher.find()) {
			String res = matcher.group();
			System.out.println(res.substring(1, res.length()-1));
		}
	}
}