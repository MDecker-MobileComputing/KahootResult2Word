package de.mide.kahoot.test;

import org.junit.Test;

import static org.junit.Assert.fail;

import de.mide.kahoot.result2word.model.QuestionList;
import de.mide.kahoot.result2word.poi.KahootResultDocxWriter;
import de.mide.kahoot.result2word.utils.KahootException;



/**
 * Unit test methods for class {@code de.mide.kahoot.result2word.poi.KahootResultDocxWriter}.
 */
public class KahootResultDocxWriterTests {

	@Test
	public void exceptionOnIllegalFilename() {
		
		QuestionList questionList = new QuestionList();
		
		try {
			new KahootResultDocxWriter(questionList, "resultfile_without_suffix");
			
			fail("No exception raised for target file name without suffix \".docx\".");
		}
		catch (KahootException ex) { /* Expected exception */ }		
	}
	
}
