package de.mide.kahoot.test;

import org.junit.Test;

import de.mide.kahoot.result2word.utils.KahootException;
import de.mide.kahoot.result2word.utils.StringUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Unit test methods for class {@code de.mide.kahoot.result2word.utils.StringUtils}.
 * <br><br>
 * 
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class StringUtilsTests {

	/** 
	 * Test for method {@code StringUtils::stringArray2String(String[])}.
	 * Convert array with three string elements in single string. 
	 */
	@Test
	public void stringArray2String_HappyPath() {
		
		String[] inputStringArray = { "abc", "def", "xyz" };
		
		
		// Call method under test
		String resultString = StringUtils.stringArray2String(inputStringArray);
		
		
		// Check that all three strings are contained in resultString
		assertTrue( resultString.indexOf("\"abc\"")  != -1); 
		assertTrue( resultString.indexOf("\"def\"")  != -1);
		assertTrue( resultString.indexOf("\"xyz\"")  != -1);						
	}
	
	
	/** 
	 * Test for method {@code StringUtils::stringArray2String(String[])}.
	 * Method to convert string array to single string should not crash for empty string as input.
	 */
	@Test
	public void stringArray2String_empty() {
	
		String[] inputStringArrayEmpty = new String[0];
		
		// call method under test
		String resultString = StringUtils.stringArray2String(inputStringArrayEmpty);
		
		assertTrue(resultString.length() == 0);				
	}
	
	
	/**
	 * Call method {@code StringUtils::isSymbolForCorrectAnwerOption(char)}
	 * with unexpected input, namely letter "a".
	 */
	@Test
	public void illegalSymbol() {
		
		try {
						
			StringUtils.isSymbolForCorrectAnwerOption('a');
			
			fail("No exception raised for unknown symbol.");
		}
		catch (KahootException ex) { /* expected exception */ }
	}


	/**
	 * Test for method {@code StringUtils::charToUnicode(char)}:
	 * Is right unicode for letter "a" returned?
	 */
	@Test
	public void charToUnicode() {
		
		String unicode = StringUtils.charToUnicode('a');
		
		assertEquals("\\u0061", unicode);
		// see also https://unicode.org/cldr/utility/character.jsp?a=61
	}
	
	
	/**
	 * Test for method {@code StringUtils::changeFilenameExtensionXlsx2Docx(String)}.
	 * 
	 * @throws KahootException  Test has failed
	 */
	@Test
	public void changeFilenameExtensionXlsx2Docx() throws KahootException {
		
		String resultString = "";
		
		resultString = StringUtils.changeFilenameExtensionXlsx2Docx("path/to/input.xlsx");
		assertEquals("path/to/input.docx", resultString);
		
		// same test with different upper/lower-case variants for ".xlsx"
		resultString = StringUtils.changeFilenameExtensionXlsx2Docx("path/to/input.XLSX");
		assertEquals("path/to/input.docx", resultString);		
		
		resultString = StringUtils.changeFilenameExtensionXlsx2Docx("path/to/input.Xlsx");
		assertEquals("path/to/input.docx", resultString);			
		
		resultString = StringUtils.changeFilenameExtensionXlsx2Docx("path/TO/input.XlSx");
		assertEquals("path/TO/input.docx", resultString);		
		
		// additional "." (dots) should not confuse the CUT
		resultString = StringUtils.changeFilenameExtensionXlsx2Docx("input.old.xlsx");
		assertEquals("input.old.docx", resultString);		
		
		
		try {
			StringUtils.changeFilenameExtensionXlsx2Docx("path/to/fileWithoutSuffix");
			
			fail("No exception raised for filename without any suffix.");
		}
		catch (KahootException ex) { /* Expected exception was caught. */ }
	}
	
}

