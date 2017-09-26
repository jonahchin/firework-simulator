package org.fireworksimulator.util;

public class URLHandler {
	
	public static String packagePath = "org/fireworksimulator/";
	public static String projectPath = "src/" + packagePath;
	
	public static String getResource(String filename) {
		return "res/" + filename;
	}
}
