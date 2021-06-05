package dk.sebsa.ender.game.util;

public class StringUtil {
	public static int charToInt(char c, String b) {
		return b.indexOf(c);
	}
	
	public static char intToChar(int i, String b) {
		return b.charAt(i);
	}
	
	public static int stringToInt(String s, int min, int max, String b) {
		int i = 0;
		for(char c : s.toCharArray()) {
			i += charToInt(c, b);
		}
		
		while(i > max) {
			i -= max - min;
		}
		
		return i;
	}
}
