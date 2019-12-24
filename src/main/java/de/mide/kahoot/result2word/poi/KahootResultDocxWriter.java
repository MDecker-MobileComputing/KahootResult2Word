package de.mide.kahoot.result2word.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

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
	
	/** Path where target file (docx) is to be written, must end with suffix {@code .docx}. */
	protected String _pathForWordFile = "";
	
	
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
		
		_questionList    = questionList;
		_pathForWordFile = pathToWordResultFile;
	}

	
	/**
	 * Actual creation of Word (docx) file.
	 * 
	 * @throws KahootException  Something went wrong, e.g. IO-Exception.
	 */
	public void writeResultFile() throws KahootException {
		
		XWPFDocument wordDocument = new XWPFDocument();
				
		writeTitleParagraph(wordDocument);
		 
		
		
		try {			
			writeFileToDisk(wordDocument);
		}
		catch (IOException ex) {
			
			throw new KahootException("I/O Error when writing docx file \"" + _pathForWordFile + "\".", ex);
		}		
	}
	
	
	/**
	 * Add title to word document which contains title of the Kahoot game.
	 * 
	 * @param wordDocument  Word document to which title paragraph is to be added.
	 */
	protected void writeTitleParagraph(XWPFDocument wordDocument) {
		
		XWPFParagraph titleParagraph = wordDocument.createParagraph();
		
		XWPFRun titleRun = titleParagraph.createRun();
		
		titleRun.setText("Kahoot Game \"" + _questionList.getTitle() + "\"" );
		titleRun.setFontSize(18);
	}
	
	
	/**
	 * Write target file (Word, docx) to disk.
	 * 
	 * @param wordDocument  Word document to be written.
	 * 
	 * @throws IOException  Error during writing of Word document.
	 */
	protected void writeFileToDisk(XWPFDocument wordDocument) throws IOException {

		File             targetFile = new File(_pathForWordFile);
		FileOutputStream fos        = new FileOutputStream(targetFile);
		
		wordDocument.write(fos);
	}
	
}
