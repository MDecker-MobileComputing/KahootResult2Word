package de.mide.kahoot.result2word.utils;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * This class can provide translated texts from the properties files in folder {@code src/main/resources}.
 * These texts are to be used in the generated docx file. 
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class TranslatedTextsProvider {

	/** Text returned as fallback when i18n text was not found. */
	protected static final String I18N_TEXT_NOT_FOUND = "<i18n text for key \"%s\" not found>";
	
	/** 
	 * Base name for language bundle (without suffix) "i18n". Code for language and suffix {@code .properties}
	 * will be appended to get full file name, e.g. {@code i18n_en.properties} or {@code i18n_de.properties}.
	 */    
	protected static final String BASE_NAME_LANGUAGE_BUNDLE = "i18n";
	
	/** Instance of {@code ResourceBundle} contains i18n texts (key value pairs) for a particular language (locale). */
	protected static ResourceBundle sResourceBundle = null;
	
	
	/**
	 * Load resource bundle with texts for a particular language, has to be done only once.
	 * 
	 * @param locale  Locale for which the texts are to be loaded, e.g. {@link Locale#ENGLISH}
	 *                or {@link Locale#GERMAN}.
	 */
	public static void loadResourceBundle(Locale locale) {
		
		sResourceBundle = ResourceBundle.getBundle(BASE_NAME_LANGUAGE_BUNDLE, locale);
	}
	
	
	/**
	 * Method to query translated text with {@code key}.<br><br>
	 * 
	 * You might want to use a static import for this method:<br>
	 * <code>import static de.mide.kahoot.result2word.utils.TranslatedTextsProvider.getTextByKey;</code>
	 * 
	 * 
	 * @param key  Key of i18n file as used in properties file before the "=".
	 *  
	 * @return  Translated text or fallback text {@link #I18N_TEXT_NOT_FOUND}. 
	 *          Text might contain placeholders {1}, {2} ... that have to be replaced before using the text.          
	 */
	public static String getTextByKey(String key) {
		 		
		try {
			
			String result = sResourceBundle.getString(key);
			
			if (result == null || result.trim().length() == 0) {
				
				return String.format(I18N_TEXT_NOT_FOUND, key);
				
			} else {
				
				return result;
			}
		}
		catch (Exception ex) {
			
			System.err.println("Exception during attempt to get i18n text with key=\"" + key + "\", will return fallback text: " + ex.getMessage() + "\n");
			
			return String.format(I18N_TEXT_NOT_FOUND, key);
		}		
	}
	
	
	/**
	 * Writes warning to STDOUT when {@code locale} is not supported by this program, i.e. not specified
	 * text bundle for this language was added to folder {@code src/main/resources/}.
	 * <br><br>
	 * 
	 * Checking for existing of resource file according to 
	 * <a href="https://stackoverflow.com/a/9380730" target="_blank">this solution on stackoverflow.com</a>.
	 * 
	 * @param locale  Locale object to be checked.
	 */
	public static void writeWarningWhenLocaleIsNotSupported(Locale locale) {
		
		String fileName = BASE_NAME_LANGUAGE_BUNDLE + "_" + locale.getLanguage() + ".properties"; // e.g. "i18n_en.properties". 
				
		URL url = TranslatedTextsProvider.class.getResource( "/" + fileName );
				
		if (url == null) {
		
			System.out.println("WARNING: No language bundle \"" + fileName + "\", will use fallback.");
		}					
	}
	
}
