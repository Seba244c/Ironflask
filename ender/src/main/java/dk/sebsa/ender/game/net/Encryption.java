package dk.sebsa.ender.game.net;

import dk.sebsa.ender.game.util.StringUtil;
import dk.sebsa.ironflask.engine.math.Math;

public class Encryption {
	public static final String ALPHABET_STRING = "abcWdeABCDEFGHfghijklmnQRSopqr2345stuvwxyzIJKLMNOPTUVZYX016789@!#%$&:*,-_ ";
	public static final String EALPHABET_STRING = "qqqq<0M!1)2(YHX/3O&4%I5=6S#7CGP8.9R,E+_-ZAL^T~£@>|";
	
	private static String encryptionKey;
	private static int ceaserOffset;
	private static int seededMax;
	private static int seedInt;
	
	public static void setEncryptionKey(String key) {
		encryptionKey = key;
		ceaserOffset = StringUtil.stringToInt(encryptionKey, 4, 45, EALPHABET_STRING);
		seededMax = StringUtil.stringToInt(encryptionKey, 4, 12, EALPHABET_STRING);
		seedInt = StringUtil.stringToInt(encryptionKey, 1, 3, EALPHABET_STRING);
	}
	
	private static String encryption(String input, int offset, String baseString, int wrapMaxDistortion) {
		String result = "";
		
		for(char c : input.toCharArray()) {
			int lengthDistortion = result.length();
			lengthDistortion = Math.wrap(lengthDistortion, 1, wrapMaxDistortion);
			int pos = baseString.indexOf(c);
			
			int poswithkey = pos+offset+1+lengthDistortion;
			if(poswithkey > baseString.length()-1) {
				poswithkey = poswithkey - baseString.length();
			}
			int newpos = Math.mod(poswithkey, baseString.length())-1;
			result+=baseString.charAt(newpos);
		}
		
		return result;
	}
	
	private static String decryption(String input, int offset, String baseString, int wrapMaxDistortion) {
		String result = "";
		
		for(char c : input.toCharArray()) {
			int lengthDistortion = result.length();
			lengthDistortion = Math.wrap(lengthDistortion, 1, wrapMaxDistortion);
			
			int pos = baseString.indexOf(c);
			int newpos = Math.mod((pos-offset-lengthDistortion), baseString.length());
			
			result+=baseString.charAt(newpos);
		}
		
		return result;
	}
	
	public static String encrypt(String input) {
		int wrapMaxDistosian = Math.getRandomNumberInRange(2, seededMax)+2;
		String out = encryption(input, ceaserOffset, ALPHABET_STRING, wrapMaxDistosian);
		out += StringUtil.intToChar(wrapMaxDistosian*seedInt, EALPHABET_STRING);
		
		return out;
	}
	
	public static String decrypt(String input) {
		int wrapMaxDistosian = StringUtil.charToInt(input.charAt(input.length() - 1), EALPHABET_STRING)/seedInt;
		
		input = input.substring(0, input.length() - 1);
		String out = decryption(input, ceaserOffset, ALPHABET_STRING, wrapMaxDistosian);
		
		return out;
	}
	
	public static void test() {
		int i = 0;
		String[] arr = new String[] {"#_WFaN#AI4-DIG", "@OS~M%_R2TXM^=0Y2/I", "IMOM_420.69", "08072006", "CGYHX<OM!"};
		for(String kkk : arr) {
			i++;
			String inpot = "I really love cookies!";
			setEncryptionKey(kkk);
			
			// Test 1
			String encrypted = encrypt(inpot);
			String decrypted = decrypt(encrypted);
			
			if(inpot.equals(decrypted)) System.out.println("Test " + String.valueOf(i) + ": \t✅");
			else System.out.println("Test " + String.valueOf(i) + ": \t❌");
			
			// Test 2
			i++;
			setEncryptionKey("#_WEEEEEEEEE");
			decrypted = decrypt(encrypted);
			
			if (inpot != decrypted) System.out.println("Test " + String.valueOf(i) + ": \t✅");
			else System.out.println("Test " + String.valueOf(i) + ": \t❌");
		}
	}
}
