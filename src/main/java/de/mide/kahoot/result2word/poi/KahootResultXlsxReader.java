package de.mide.kahoot.result2word.poi;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.mide.kahoot.result2word.model.AbstractQuestion;
import de.mide.kahoot.result2word.model.MultipleOrSingleChoiceQuestion;
import de.mide.kahoot.result2word.model.QuestionList;
import de.mide.kahoot.result2word.model.TrueFalseQuestion;
import de.mide.kahoot.result2word.utils.KahootException;
import de.mide.kahoot.result2word.utils.StringUtils;


/**
 * This class contains the logic to load a Excel file (Xslx) with Kahoot results and extract the
 * relevant information.
 * <br><br>
 *  
 * First three sheets are "Overview", "Final Scores" and "Kahoot! Summary"; after this the sheets
 * for the individual questions come. The final sheet is "RawReportData Data". So number of questions
 * in Xlsx file is number of sheets minus 4. 
 * <br><br> 
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class KahootResultXlsxReader {
	
	/** Question text is in cell B2 on the question sheets, so row has index 1 (because of 0-based index). */
	public static final int ROW_INDEX_QUESTION_TEXT = 1;
	
	/** Question text is in cell B2 on the question sheets, so column has index 1 (because of 0-based index). */
	public static final int COL_INDEX_QUESTION_TEXT = 1;
	
	/** Answer options are in row 8 of the sheet (cells D8, F8, H8, J8), so row has index 7 (because of 0-based index). */
	public static final int ROW_INDEX_ANSWER_OPTIONS = 7;

	/** First answer option is in cell D8, so column index is 3 (because of 0-based index). */
	public static final int COL_INDEX_ANSWER_OPTION_1 = 3;
	
	/** First answer option is in cell F8, so column index is 5 (because of 0-based index). */
	public static final int COL_INDEX_ANSWER_OPTION_2 = 5;	
	
	/** First answer option is in cell H8, so column index is 7 (because of 0-based index). */
	public static final int COL_INDEX_ANSWER_OPTION_3 = 7;
	
	/** First answer option is in cell J8, so column index is 9 (because of 0-based index). */
	public static final int COL_INDEX_ANSWER_OPTION_4 = 9;
	
	/** 
	 * In row 9 there are the checks or crosses which mark correct answer options (cells C9 and E9 for true/false questions,
	 * cells G9 and I9 for single-choice/multi-choice questions with four answer options. 
	 */
	public static final int ROW_INDEX_ANSWER_CORRECT = 8;
	
	/** In cell C9 there will be a check or cross saying wether first answer option is right or not. */
	public static final int COL_INDEX_ANSWER_CORRECT_1 = 2;
	
	/** In cell E9 there will be a check or cross saying wether second answer option is right or not. */
	public static final int COL_INDEX_ANSWER_CORRECT_2 = 4;	

	/** In cell G9 there will be a check or cross saying wether third answer option is right or not. */
	public static final int COL_INDEX_ANSWER_CORRECT_3 = 6;
	
	/** In cell I9 there will be a check or cross saying wether fourth answer option is right or not. */
	public static final int COL_INDEX_ANSWER_CORRECT_4 = 8;
	
	/** Object representing the whole XLSX file which consists of several sheets. */
	protected XSSFWorkbook _excelWorkbook = null;

	
	/**
	 * Load Xlsx file with Kahoot results.
	 *
	 * @param pathToExcelFile  Relative path to Excel file to be loaded.
	 *
	 * @throws KahootException  File was not found
	 */
	public KahootResultXlsxReader(String pathToExcelFile) throws KahootException {

		File file = new File(pathToExcelFile);

		if (!file.exists()) {

			throw new KahootException("Input file \"" + pathToExcelFile + "\" not found.");
		}

		try {
			FileInputStream fis = new FileInputStream(file);

			_excelWorkbook = new XSSFWorkbook(fis);
		}
		catch (Exception ex) {

			throw new KahootException("Error when trying to read input file \"" + pathToExcelFile + "\".", ex);
		}
	}
	
	
	/**
	 * Perform the actual extraction of the questions from the XLSX file.
	 * 
	 * @return  Object with list of all questions in XSLX file.
	 * 
	 * @throws KahootException  Xlsx file was not as expected. 
	 */
	public QuestionList extractQuestionList() throws KahootException {
		 
		QuestionList questionList = null;
		
		int numberOfSheets = _excelWorkbook.getNumberOfSheets();
		
		// "minus 4" because of the following sheets: "Overview", "Final Scores", "Kahoot! Summary", "RawReportData Data". 
		int numberOfQuestions = numberOfSheets - 4;  
		
		if (numberOfQuestions < 1) { throw new KahootException("Less than 1 sheet with questions."); }
																 
		System.out.println("Number of sheets with questions: " + numberOfQuestions);
		
		questionList = new QuestionList(numberOfQuestions);
		
		
		// Loop over all sheets with a question
		for (int questionNo = 1; questionNo <= numberOfQuestions; questionNo++) {
			
			int sheetIndex = 2 + questionNo;
			
			XSSFSheet sheet = _excelWorkbook.getSheetAt(sheetIndex);
			
			AbstractQuestion question = extractQuestionFromSheet(sheet); // might raise exception
		
			System.out.println("Found question on sheet with index " + sheetIndex + ": " + question);
			
			questionList.addQuestion(question);
		}
		
		return questionList;
	}
	
	
	/**
	 * Extract question from {@code sheet} passed as argument.
	 * 
	 * @param sheet  Must be an answer sheet!
	 * 
	 * @return  Object containing the question object, will be an instance of class {@link MultipleOrSingleChoiceQuestion}
	 *          or {@link TrueFalseQuestion}.
	 *          
	 * @throws KahootException  Error during attempt to extract question from sheet.
	 */
	public AbstractQuestion extractQuestionFromSheet(XSSFSheet sheet) throws KahootException {

		String questionText = getQuestionText(sheet);
		
		String[] answerOptions = getAnswerOptions(sheet);
		
		
		if ( checkIsTrueFalseQuestion(answerOptions) ) {
			
			boolean isStatementTrue = extractAnswerForTrueFalseQuestion(sheet); // might raise exception
		
			TrueFalseQuestion trueFalseQuestion = new TrueFalseQuestion(questionText, isStatementTrue);
		    return trueFalseQuestion;
		}				
		
		return null;
	}

	
	/**
	 * Extract correct answer from sheet with true/false question.
	 * The method checks both cells C9 and E9, but one of them would be sufficient to determine if the statement
	 * is wrong or right; however, checking the symbol in both cells allows to perform a simple consistency check. 
	 * 
	 * @param sheet  Sheet must contain a true/false question detected by method {@link #extractQuestionFromSheet(XSSFSheet)}.
	 * 
	 * @return {@code true} iff the statement of the true/false question is true.
	 * 
	 * @throws KahootException  Could not determine if statement is marked as true or false.
	 */
	public boolean extractAnswerForTrueFalseQuestion(XSSFSheet sheet) throws KahootException {
				
		XSSFRow row = sheet.getRow(ROW_INDEX_ANSWER_CORRECT);
		
		XSSFCell cell1 = row.getCell(COL_INDEX_ANSWER_CORRECT_1);
		XSSFCell cell2 = row.getCell(COL_INDEX_ANSWER_CORRECT_2);
		
		String str1 = cell1.getStringCellValue();
		String str2 = cell2.getStringCellValue();
		
		char ch1 = str1.charAt(0);
		char ch2 = str2.charAt(0);
		
		boolean statementIsFalse = StringUtils.isSymbolForCorrectAnwerOption(ch1); // might raise exception
		boolean statementIsRight = StringUtils.isSymbolForCorrectAnwerOption(ch2); // might raise exception
		
		if (statementIsFalse && statementIsRight) {
			
			throw new KahootException("Inconsistent result for true/false question: Both true and false seem to be marked as correct answer option."); 
		}
		
		if (!statementIsFalse && !statementIsRight) {
			
			throw new KahootException("Inconsistent result for true/false question: Both true and false seem to be marked as incorrect answer option."); 
		}		
		
		return statementIsRight;
	}
	
	
	/**
	 * Checks if answer options are those of a true/false question.
	 * 
	 * @param answerOptionsArray  Array obtained from method {@link #getAnswerOptions(XSSFSheet)}.
	 *  
	 * @return  {@code true} if argument {@code answerOptionsArray} is such of a true/false questions,
	 *          i.e. first element contains "false" and second element contains "true", and 
	 *          {@code answerOptionsArray} contains exactly two elements. 
	 */
	public boolean checkIsTrueFalseQuestion(String[] answerOptionsArray) {
		
		if (answerOptionsArray.length != 2) { return false; }
		
		return answerOptionsArray[0].equalsIgnoreCase("false") && answerOptionsArray[1].equalsIgnoreCase("true");
	}
	
	
	/**
	 * Extract question text from sheet.
	 * 
	 * @param sheet  Sheet with question (must be a question sheet!).
	 * 
	 * @return  String with question text in cell B2.
	 */
	public String getQuestionText(XSSFSheet sheet) {

		XSSFRow  row  = sheet.getRow(ROW_INDEX_QUESTION_TEXT);
		XSSFCell cell = row.getCell(COL_INDEX_QUESTION_TEXT);
		
		String   questionText = cell.getStringCellValue();
				
		return questionText;
	}
	
	
	/** 
	 * Get answer options from line 8; seeking in cells D8, F8, H8 and J8. 
	 * 
	 * @param sheet  Sheet with question (must be a question sheet!).
	 * 
	 * @return Array with answer options; will have at least two and at most four elements.
	 */
	public String[] getAnswerOptions(XSSFSheet sheet) {
		
		XSSFRow row = sheet.getRow(ROW_INDEX_ANSWER_OPTIONS);
		
		XSSFCell cell1 = row.getCell(COL_INDEX_ANSWER_OPTION_1);		
		XSSFCell cell2 = row.getCell(COL_INDEX_ANSWER_OPTION_2);
		XSSFCell cell3 = row.getCell(COL_INDEX_ANSWER_OPTION_3);
		XSSFCell cell4 = row.getCell(COL_INDEX_ANSWER_OPTION_4);
								
		int numOfAnswerOptions = 2;
		
		if (cell3 != null && cell3.getCellType() != CellType.BLANK) {
			
			numOfAnswerOptions = 3;
			
			if (cell4 != null && cell4.getCellType() != CellType.BLANK) {
				
				numOfAnswerOptions = 4;
			}
		}
		
		String[] resultArray = new String[numOfAnswerOptions];
		
		resultArray[0] = cell1.getStringCellValue();
		resultArray[1] = cell2.getStringCellValue();
		
		if (numOfAnswerOptions >= 3) {
			
			resultArray[2] = cell3.getStringCellValue();
		}		
		if (numOfAnswerOptions == 4) {
			
			resultArray[3] = cell4.getStringCellValue();
		}
		
		return resultArray;
	}

}
