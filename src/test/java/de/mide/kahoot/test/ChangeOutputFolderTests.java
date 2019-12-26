package de.mide.kahoot.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static de.mide.kahoot.result2word.utils.DirectoryUtil.changeOutputFolder;


/**
 * Unit test methods for method {@code changeOutputFolder(String,String)} in 
 * class {@code de.mide.kahoot.result2word.utils.DirectoryUtil}.  
 */
public class ChangeOutputFolderTests {

	/**
	 * Test for the case that file name passed as first argument to method under test
	 * does NOT contain path information.
	 */
	@Test
	public void filenameWithoutPath() {
		
		String resultName;
		
		resultName = changeOutputFolder("result.docx", "/path/to/results/");		
		assertEquals(resultName, "/path/to/results/result.docx");
		
		resultName = changeOutputFolder("result.docx", "/path/to/results"); // target path has no "/" at the end!
		assertEquals(resultName, "/path/to/results/result.docx");		
	}
	

	/**
	 * Test for the case that file name passed as first argument to method under test
	 * does contain path information.
	 */	
	@Test
	public void filenameWithPath() {
		
		String resultName;
		
		resultName = changeOutputFolder("/old/path/result.docx", "/path/to/results/");
		assertEquals(resultName, "/path/to/results/result.docx");
		
		resultName = changeOutputFolder("/old/path/result.docx", "/path/to/results"); // target path has no "/" at the end!
		assertEquals(resultName, "/path/to/results/result.docx");		
	}
	
}
