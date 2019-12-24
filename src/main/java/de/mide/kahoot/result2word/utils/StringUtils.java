package de.mide.kahoot.result2word.utils;

/**
 * Some helper methods to work with strings.
 * <br><br>
 * 
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class StringUtils {
	
	/** 
	 * Unicode string for cross symbol ("Heavy Check Mark") used by Kahoot to mark wrong answer option,
	 * see also <a href="https://unicode.org/cldr/utility/character.jsp?a=2714">this page on unicode.org</a>.
	 */	
	public static final String UNICODE_FOR_RIGHT_ANSWER_OPTION = "\\u2714";	
	
	
	/** 
	 * Unicode string for cross symbol ("Heavy Ballot X") used by Kahoot to mark wrong answer option,
	 * see also <a href="https://unicode.org/cldr/utility/character.jsp?a=2718">this page on unicode.org</a>.
	 */
	public static final String UNICODE_FOR_WRONG_ANSWER_OPTION = "\\u2718"; 
		
	
	/**
	 * "Merge" elements of string array into single string. 
	 * <br>
	 * Example:
	 * <ul>
	 *   <li>Input  (string array):  <code>[ "ab", "cd"]</code></li>
	 *   <li>Output (single string): <code>"\"ab\", \"cd\""</code></li>
	 * </ul>
	 * 
	 * @param stringArray  Array whose string elements are to be put into a single string.
	 * 
	 * @return  Single string containing all elements from argument {@code stringArray}.
	 */
	public static String stringArray2String(String[] stringArray) {
		
		StringBuffer sb = new StringBuffer();
		
		int maxIndexValue = stringArray.length - 1;
		for (int i = 0; i <= maxIndexValue; i++) {
	
			sb.append("\"").append(stringArray[i]).append("\"");
			
			if (i != maxIndexValue) {
				sb.append(", ");
			}
		}
		
		return sb.toString();
	}

	
	/**
	 * Get unicode sequence for char, according to 
	 * <a href="https://stackoverflow.com/a/18098216" target="_blank">this answer on stackoverflow.com</a>. 
	 *   
	 * @param ch  Single character for which unicode sequence is to be returned.
	 * 
	 * @return  Unicode sequence  
	 */
	public static String charToUnicode(char ch) {
		
		return String.format("\\u%04x", (int) ch);
	}
	
	
	/**
	 * Check if {@code ch} is the symbol for a correct or an incorrect answer option.
	 * 
	 * @param ch  Char to be recognized
	 * 
	 * @return {@code true}  when {@code ch} was the symbol for a    correct answer ("Heavy Ballot X"),
	 *         {@code false} when {@code ch} was the symbol for an incorrect answer ("Heavy Check Mark").
	 * 
	 * @throws KahootException  {@code ch} was neither recognized as "Heavy Ballot X" nor as "Heavy Check Mark".
	 */
	public static boolean isSymbolForCorrectAnwerOption(char ch) throws KahootException {
		
		String charAsUnicode = charToUnicode(ch);
		
		if ( charAsUnicode.equalsIgnoreCase(UNICODE_FOR_RIGHT_ANSWER_OPTION) ) { return true; }
		
		if ( charAsUnicode.equalsIgnoreCase(UNICODE_FOR_WRONG_ANSWER_OPTION) ) { return false; }		
		
		throw new KahootException("Could not recognize symbol with unicode \"" + charAsUnicode + "\".");
	}
	
	
	/**
	 * Replace file suffix {@code .xlsx} (Excel) with {@code .docx} (Word), needed to construct name 
	 * of target file based on source file.
	 * 
	 * @param xlsxFilename  Filename (maybe with path) with suffix {@code .xlsx} (detection of suffix is not case-sensitive).
	 * 
	 * @return  Filename with suffix {@code .docx}
	 * 
	 * @throws KahootException  {@code xlsxFilename} did not contain suffix {@code .xlsx}.
	 */
	public static String changeFilenameExtensionXlsx2Docx(String xlsxFilename) throws KahootException {
		
		String xlsxFilenameNormalized = xlsxFilename.toLowerCase(); 
		
		if ( xlsxFilenameNormalized.endsWith(".xlsx") == false ) {
			
			throw new KahootException("Filename \"" + xlsxFilename + "\" did not have suffix \"xlsx\".");
		}
		
		int indexOfLastPoint = xlsxFilenameNormalized.lastIndexOf('.');
		
		String filenameWithoutSuffix = xlsxFilename.substring(0, indexOfLastPoint);
				
		return filenameWithoutSuffix + ".docx";
	}
	
}
