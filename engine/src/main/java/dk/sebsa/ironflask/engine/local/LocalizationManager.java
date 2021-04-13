package dk.sebsa.ironflask.engine.local;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ironflask.engine.enums.Languages;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.LoggingUtil;

public class LocalizationManager {
	private static List<Language> languages = new ArrayList<>();
	private static Language currentLanguage;
	
	public static void load(String file) {
		System.out.println(file);
		Language lang = new Language(file);
		languages.add(lang);
	}
	
	public static void setLangauage(Languages code) {
		for(Language lang : languages) {
			if(lang.code.equals(code)) currentLanguage = lang;
		}
	}
	
	public static String getString(String key, String arg0, String arg1, String arg2) {
		return getString(key, arg0, arg1).replace("{2}", arg2);
	}
	
	public static String getString(String key, String arg0, String arg1) {
		return getString(key, arg0).replace("{1}", arg1);
	}
	
	public static String getString(String key, String arg0) {
		return getString(key).replace("{0}", arg0);
	}
	
	public static String getString(String key) {
		if(currentLanguage == null) {
			String error = "When getting string("+key+") no current language was set";
			
			LoggingUtil.coreLog(Severity.Error, error);
			throw new IllegalStateException(error);
		}
		return currentLanguage.get(key);
	}
}
