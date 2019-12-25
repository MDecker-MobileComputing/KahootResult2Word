package de.mide.kahoot.result2word.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.apache.poi.ooxml.POIXMLProperties.CoreProperties;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import de.mide.kahoot.result2word.model.AnswerOption;
import de.mide.kahoot.result2word.model.MultipleOrSingleChoiceQuestion;
import de.mide.kahoot.result2word.model.QuestionList;
import de.mide.kahoot.result2word.model.QuestionTypeEnum;
import de.mide.kahoot.result2word.model.TrueFalseQuestion;
import de.mide.kahoot.result2word.utils.KahootException;


/**
 * This class contains the logic to write a Docx file (word) with the results from a Kahoot game.
 * <br><br>
 * 
 * See also <a href="https://www.tutorialspoint.com/apache_poi_word/index.htm" target="_blank">this 3rd party tutorial</a>.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class KahootResultDocxWriter {
	
	/** Font size for text in normal paragraph. */
	protected static final int FONT_SIZE_NORMAL = 12;
	
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
				
		writeDocumentTitle(wordDocument);
		 
		loopOverAllQuestions(wordDocument);
		
		addFooter(wordDocument);		
		
		setMetadata(wordDocument);
		
		try {			
			writeFileToDisk(wordDocument);
		}
		catch (IOException ex) {
			
			throw new KahootException("I/O Error when writing docx file \"" + _pathForWordFile + "\".", ex);
		}		
	}
	
	
	/**
	 * Set some meta data values in the word document.
	 * 
	 * @param wordDocument  Document for which the meta data is to be set.
	 */
	protected void setMetadata(XWPFDocument wordDocument) {
		
		try {

			CoreProperties coreProperties = wordDocument.getProperties().getCoreProperties();
			
			coreProperties.setCreator("Kahoot Result to Word");
			
			Date dateNow = new Date();
			Optional<Date> dateOptional = Optional.of(dateNow);
			coreProperties.setCreated( dateOptional );
			
			wordDocument.getProperties().commit();
		} 
		catch (Exception ex) {
			
			System.out.println("Exception during writing meta data: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * Add footer on each page with "Page X of Y".
	 * 
	 * Based on <a href="https://stackoverflow.com/a/41391801/1364368" target="_blank">this answer</a>.
	 * 
	 * @param wordDocument  Word document to which the footer is to be added.
	 */
	protected void addFooter(XWPFDocument wordDocument) {
		
		XWPFHeaderFooterPolicy headerFooterPolicy = wordDocument.createHeaderFooterPolicy();
		
		XWPFFooter footer = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
		
		XWPFParagraph paragraph = footer.getParagraphArray(0);
		if (paragraph == null) { paragraph = footer.createParagraph(); }
		paragraph.setAlignment(ParagraphAlignment.CENTER);		
		
		XWPFRun run = paragraph.createRun();  
		run.setText("Page ");
		paragraph.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");
		run = paragraph.createRun();  
		run.setText(" of ");
		paragraph.getCTP().addNewFldSimple().setInstr("NUMPAGES \\* MERGEFORMAT");
	}
	
	
	/**
	 * Write all questions into the word file.
	 * 
	 * @param wordDocument  Document into which the questions are to be written.
	 * 
	 * @throws KahootException  Something went wrong.
	 */
	protected void loopOverAllQuestions(XWPFDocument wordDocument) throws KahootException {
		
		TrueFalseQuestion              trueFalseQuestion         = null;
		MultipleOrSingleChoiceQuestion multiSingleChoiceQuestion = null;
		
		
		int numberOfQuestions = _questionList.getNumberOfQuestions();
		
		for (int index = 0; index < numberOfQuestions; index++) {
						
			writeQuestionTitle(wordDocument, index + 1);
			
			QuestionTypeEnum questionType = _questionList.getTypeOfQuestion(index);
			
			switch(questionType) {
			
				case TRUE_OR_FALSE:
					trueFalseQuestion = _questionList.getTrueOrFalseQuestion(index);
					writeTrueFalseQuestion(wordDocument, trueFalseQuestion);
					break;
					
				case MULTIPLE_CHOICE:
				case SINGLE_CHOICE:
					multiSingleChoiceQuestion = _questionList.getMultiSingleChoiceQuestion(index);
					writeMultiSingleChoiceQuestion(wordDocument, multiSingleChoiceQuestion);
					break;
					
				default:
					throw new KahootException("Unexcepted type of question: " + questionType);				
			}
		}
	}
	
	
	/**
	 * Write a single True/False question into the Word document.
	 * 
	 * @param wordDocument  Document into which the {@code TrueFalseQuestion} is to be written.
	 * 
	 * @param trueFalseQuestion  True/False-Question to be written into {@code wordDocument}. 
	 */
	protected void writeTrueFalseQuestion(XWPFDocument wordDocument, TrueFalseQuestion trueFalseQuestion) {
				
		XWPFParagraph paragraph1 = wordDocument.createParagraph();		
		XWPFRun       run1       = paragraph1.createRun();
		
		run1.setText("Is the following statement true or false?");
		run1.setFontSize(FONT_SIZE_NORMAL);
		run1.addBreak();
		
		XWPFParagraph paragraph2 = wordDocument.createParagraph();		
		XWPFRun       run2       = paragraph2.createRun();
		
		run2.setText("   \"" + trueFalseQuestion.getQuestionText() + "\"");
		run2.setItalic(true);
		run2.setFontSize(FONT_SIZE_NORMAL);
		run2.addBreak();
		
		XWPFParagraph paragraph3 = wordDocument.createParagraph();		
		XWPFRun       run3a      = paragraph3.createRun();
		XWPFRun       run3b      = paragraph3.createRun();
		XWPFRun       run3c      = paragraph3.createRun();
		
		run3a.setText("The statement is ");
		run3a.setFontSize(FONT_SIZE_NORMAL);
		
		if ( trueFalseQuestion.isStatementTrue() ) {
			
			run3b.setText("TRUE");			
			
		} else {
			
			run3b.setText("FALSE");
		}
		run3b.setBold(true);
		run3b.setItalic(true);
		run3b.setFontSize(FONT_SIZE_NORMAL);
			
		run3c.setText(".");
		run3c.addBreak();
	}
	
	
	/**
	 * Write {@code multiSingleChoiceQuestion} into {@code wordDocument}.<br><br>
	 * 
	 * On how to create a table see 
	 * <a href="https://www.tutorialspoint.com/apache_poi_word/apache_poi_word_tables.htm" target="_blank">this 3rd-party tutorial</a>.
	 * 
	 * @param wordDocument  Document into which the {@code multiSingleChoiceQuestion} is to be written.
	 * 
	 * @param multiSingleChoiceQuestion  Question which is to be written into {@code wordDocument}. 
	 * 
	 * @throws KahootException  Internal error has occured
	 */
	protected void writeMultiSingleChoiceQuestion(XWPFDocument wordDocument, 
			                                      MultipleOrSingleChoiceQuestion multiSingleChoiceQuestion) throws KahootException {
		
		XWPFParagraph paragraph = wordDocument.createParagraph();
		
		XWPFRun run = paragraph.createRun();
		
		run.setText( multiSingleChoiceQuestion.getQuestionText() );
		run.setFontSize(FONT_SIZE_NORMAL);
		run.addBreak();
		
		
		XWPFTable table = wordDocument.createTable();
		
		XWPFTableCell cell = null;
		
		int numberOfAnswerOptions = multiSingleChoiceQuestion.getNumberOfAnswerQuestions(); 
				
		AnswerOption answerOption = multiSingleChoiceQuestion.getAnswerOption(1);
		
		// Create 1st table row (first row needs to be created differently)
		XWPFTableRow tableRow = table.getRow(0);
		
		cell = tableRow.getCell(0);
		setTextInCell(cell, answerOption.getAnswerOptionText());
		
		cell = tableRow.addNewTableCell();
		setTextInCell(cell, answerOption.getAnswerOptionIsRightAsString());
		 
		
		for (int i = 2; i <= numberOfAnswerOptions; i++) {
		
			answerOption = multiSingleChoiceQuestion.getAnswerOption( i );
						
			tableRow = table.createRow();
			
			cell = tableRow.getCell(0);
		    setTextInCell(cell, answerOption.getAnswerOptionText());
		    
		    cell = tableRow.getCell(1);
		    setTextInCell(cell, answerOption.getAnswerOptionIsRightAsString());
		}		
		
		
		XWPFRun runAfterTable = wordDocument.createParagraph().createRun();
		runAfterTable.setText("");
		runAfterTable.addBreak();		
	}
	
	
	/**
	 * Set {@code text} as paragraph in {@code cell} according to 
	 * <a href="https://stackoverflow.com/a/29258785/1364368" target="_blank">this answer on stackoverflow.com</a><br><br>
	 * 
	 * Text can also be set using method {@code XWPFTableCell::setText(String text)}, but then no font size can be set.
	 *  
	 * @param cell  Cell into which the {@code text} is to be set as paragraph.
	 * 
	 * @param text Text to be set in a paragraph in {@code cell}.  
	 */
	protected void setTextInCell(XWPFTableCell cell, String text) {
		
		XWPFParagraph paragraph = cell.addParagraph();
		XWPFRun       run       = paragraph.createRun();
		
		run.setFontSize(FONT_SIZE_NORMAL);
		run.setText(text);
		
		run.addBreak();
	}
	
	
	/**
	 * Write title of {@code question} into {@code wordDocument}.
	 * 
	 * @param wordDocument  Document into which the title of {@code question} is to be written.
	 * 
	 * @param questionNumber  Number (1-based) of question to be written into {@code wordDocument}.
	 */
	protected void writeQuestionTitle(XWPFDocument wordDocument, int questionNumber) {
		
		XWPFParagraph paragraph = wordDocument.createParagraph();
		
		XWPFRun run = paragraph.createRun();
		
		run.setText("Question No " + questionNumber);
		run.setFontSize(14);
		run.setBold(true);
		
		run.addBreak();
	}
	
	
	/**
	 * Add title to word document which contains title of the Kahoot game.
	 * 
	 * @param wordDocument  Word document to which title paragraph is to be added.
	 */
	protected void writeDocumentTitle(XWPFDocument wordDocument) {
		
		XWPFParagraph paragraph = wordDocument.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun run = paragraph.createRun();
		
		run.setText("Kahoot Game \"" + _questionList.getTitle() + "\"");
		run.setFontSize(18);
		run.setBold(true);
		
		run.addBreak();
		run.addBreak();
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
		
		fos.close();
	}
	
}
