
public class URLHandler {
	
	private static String path = "/org/fireworksimulator/";
	
	public static String getResource(String filename) {
		return path + "res/" + filename;
	}
}
