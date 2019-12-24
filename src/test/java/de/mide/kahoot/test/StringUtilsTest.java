package de.mide.kahoot.test;

import org.junit.Test;

import de.mide.kahoot.result2word.utils.StringUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Unit test methods for class {@code de.mide.kahoot.result2word.utils.StringUtils}.
 * <br><br>
 * 
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class StringUtilsTest {

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
	
	
	@Test
	public void stringArray2String_empty() {
	
		String[] inputStringArrayEmpty = new String[0];
		
		// call method under test
		String resultString = StringUtils.stringArray2String(inputStringArrayEmpty);
		
		assertTrue(resultString.length() == 0);				
	}
	
	
	@Test
	public void charToUnicode() {
		
		String unicode = StringUtils.charToUnicode('a');
		
		assertEquals("\\u0061", unicode);
		// see also https://unicode.org/cldr/utility/character.jsp?a=61
	}
	
}
