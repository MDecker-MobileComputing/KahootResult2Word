package de.mide.kahoot.result2word.utils;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * This class can provide translated texts from the properties files in folder {@code src/main/resources}.
 * These texts are to be used in the generated docx file. 
 */
public class TranslatedTextsProvider {

	/** Text returned as fallback when i18n text was not found. */
	protected static final String I18N_TEXT_NOT_FOUND = "<i18n text for key \"%s\" not found>";
	
	/** Instance of {@code ResourceBundle} contains i18n texts (key value pairs) for a particular language (locale). */
	protected static ResourceBundle sResourceBundle = null;
	
	
	/**
	 * Load resource bundle with texts for a particular language, has to be done only once.
	 * 
	 * @param locale  Locale for which the texts are to be loaded, e.g. {@link Locale#ENGLISH}
	 *                or {@link Locale#GERMAN}.
	 */
	public static void loadResourceBundle(Locale locale) {
		
		sResourceBundle = ResourceBundle.getBundle("i18n", locale);
	}
	
	
	/**
	 * Method to query translated text with {@code key}.
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
	
}
