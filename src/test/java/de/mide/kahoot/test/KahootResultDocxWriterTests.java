package de.mide.kahoot.test;

import org.junit.Test;
import org.apache.commons.cli.CommandLine;

import static org.junit.Assert.fail;

import de.mide.kahoot.result2word.model.QuestionList;
import de.mide.kahoot.result2word.poi.KahootResultDocxWriter;
import de.mide.kahoot.result2word.utils.KahootException;



/**
 * Unit test methods for class {@code de.mide.kahoot.result2word.poi.KahootResultDocxWriter}.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class KahootResultDocxWriterTests {

	@Test
	public void exceptionOnIllegalFilename() {

		CommandLine cmdLine = null;

		QuestionList questionList = new QuestionList();

		try {
			new KahootResultDocxWriter(questionList, "resultfile_without_suffix", cmdLine);

			fail("No exception raised for target file name without suffix \".docx\".");
		}
		catch (KahootException ex) { /* Expected exception */ }
	}

}
