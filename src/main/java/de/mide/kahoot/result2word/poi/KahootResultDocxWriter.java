package de.mide.kahoot.result2word.poi;

import de.mide.kahoot.result2word.model.QuestionList;
import de.mide.kahoot.result2word.utils.KahootException;

/**
 * This class contains the logic to write a Docx file (word) with the results from a Kahoot game.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class KahootResultDocxWriter {
	
	/** Object with data extracted from Kahoot Excel file which is written into a Word file. */
	protected QuestionList _questionList = null; 
	
	
	/**
	 * Create new object to write data from {@code questionList} into Docx file with name {@code pathToWordResultFile}.
	 * 
	 * @param questionList  Object with data extracted from Kahoot Result file.
	 * 
	 * @param pathToWordResultFile  Path of Word file to be written as result, must end with suffix {@code .docx}.
	 * 
	 * @throws KahootException  {@code pathToWordResultFile} does not end with {@code .docx}.
	 */
	public KahootResultDocxWriter(QuestionList questionList, String pathToWordResultFile) throws KahootException {
		
		if (pathToWordResultFile.endsWith(".docx") == false) {
			
			throw new KahootException("Target file name \"" + pathToWordResultFile + "\" does not end with \".docx\".");
		}		
	}

	
	/**
	 * Actual creation of Word (docx) file.
	 * 
	 * @throws KahootException  Something went wrong
	 */
	public void writeResultFile() throws KahootException {
		
	}
	
}
