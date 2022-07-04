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
import de.mide.kahoot.result2word.model.QuestionTypeEnum;
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
	protected static final int ROW_INDEX_QUESTION_TEXT = 1;

	/** Question text is in cell B2 on the question sheets, so column has index 1 (because of 0-based index). */
	protected static final int COL_INDEX_QUESTION_TEXT = 1;

	/** Answer options are in row 8 of the sheet (cells D8, F8, H8, J8), so row has index 7 (because of 0-based index). */
	protected static final int ROW_INDEX_ANSWER_OPTIONS = 7;

	/** First answer option is in cell D8, so column index is 3 (because of 0-based index). */
	protected static final int COL_INDEX_ANSWER_OPTION_1 = 3;

	/** First answer option is in cell F8, so column index is 5 (because of 0-based index). */
	protected static final int COL_INDEX_ANSWER_OPTION_2 = 5;

	/** First answer option is in cell H8, so column index is 7 (because of 0-based index). */
	protected static final int COL_INDEX_ANSWER_OPTION_3 = 7;

	/** First answer option is in cell J8, so column index is 9 (because of 0-based index). */
	protected static final int COL_INDEX_ANSWER_OPTION_4 = 9;

	/**
	 * In row 9 there are the checks or crosses which mark correct answer options (cells C9 and E9 for true/false questions,
	 * cells G9 and I9 for single-choice/multi-choice questions with four answer options.
	 */
	protected static final int ROW_INDEX_ANSWER_CORRECT = 8;

	/** In cell C9 there will be a check or cross saying wether first answer option is right or not. */
	protected static final int COL_INDEX_ANSWER_CORRECT_1 = 2;

	/** In cell E9 there will be a check or cross saying wether second answer option is right or not. */
	protected static final int COL_INDEX_ANSWER_CORRECT_2 = 4;

	/** In cell G9 there will be a check or cross saying wether third answer option is right or not. */
	protected static final int COL_INDEX_ANSWER_CORRECT_3 = 6;

	/** In cell I9 there will be a check or cross saying wether fourth answer option is right or not. */
	protected static final int COL_INDEX_ANSWER_CORRECT_4 = 8;

	/** In cell C4 the percentage of players who gave the correct answer for this question is stated. */
	protected static final int COL_INDEX_PLAYERS_CORRECT_PERCENT = 2;

	/** In cell C4 the percentage of players who gave the correct answer for this question is stated. */
	protected static final int ROW_INDEX_PLAYERS_CORRECT_PERCENT = 3;

	/** In cell A1 the title of the whole game can be found on each sheet but the last one. */
	protected static final int ROW_INDEX_TITLE = 0;

	/** In cell A1 the title of the whole game can be found on each sheet but the last one. */
	protected static final int COL_INDEX_TITLE = 0;


	/** Object representing the whole XLSX file which consists of several sheets. */
	protected XSSFWorkbook _excelWorkbook = null;


	/**
	 * Load Xlsx file with Kahoot results.
	 *
	 * @param pathToExcelFile  Relative path to Excel file with Kahoot results to be read.
	 *
	 * @throws KahootException  File was not found
	 */
	public KahootResultXlsxReader(String pathToExcelFile) throws KahootException {

		File file = new File(pathToExcelFile);

		if ( !file.exists() ) {

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

			System.out.println("Found question on sheet with index=" + sheetIndex + ": " + question);

			questionList.addQuestion(question);
		}


		String title = extractTitle( _excelWorkbook.getSheetAt(0) );
		questionList.setTitle(title);

		return questionList;
	}


	/**
	 * Extracts title of the Kahoot game which is be found in cell A1 on each sheet except the last one.
	 *
	 * @param sheet  Any sheet with a question.
	 *
	 * @return  Title of the game.
	 */
	protected String extractTitle(XSSFSheet sheet) {

		XSSFRow row = sheet.getRow(ROW_INDEX_TITLE);

		XSSFCell cell = row.getCell(COL_INDEX_TITLE);

		return cell.getStringCellValue().trim();
	}


	/**
	 * Extract question from {@code sheet} passed as argument; this includes determination of the type of question.
	 *
	 * @param sheet  Must be an answer sheet!
	 *
	 * @return  Object containing the question object, will be an instance of class {@link MultipleOrSingleChoiceQuestion}
	 *          or {@link TrueFalseQuestion}.
	 *
	 * @throws KahootException  Error during attempt to extract question from sheet.
	 */
	protected AbstractQuestion extractQuestionFromSheet(XSSFSheet sheet) throws KahootException {

		String   questionText  = getQuestionText(  sheet );
		String[] answerOptions = getAnswerOptions( sheet );

		float percentageCorrect = extractPercentageOfRightAnswers(sheet);


		if ( checkIsTrueFalseQuestion(answerOptions) ) {

			boolean isStatementTrue = extractAnswerForTrueFalseQuestion(sheet); // might raise exception

			TrueFalseQuestion trueFalseQuestion = new TrueFalseQuestion(questionText, isStatementTrue);

			trueFalseQuestion.setPercentageAnswersRight(percentageCorrect);

		    return trueFalseQuestion;
		}


		// If we come to this line, then the question is either a multi-choice or a single-choice question

		boolean[] correctAnswersArray = extractCorrectAnswerOptions(sheet); // might raise exception

		int numCorrectAnswerOptions = countNumberOfCorrectAnswerOptions(correctAnswersArray); // might raise exception

		QuestionTypeEnum questionType = numCorrectAnswerOptions == 1 ? QuestionTypeEnum.SINGLE_CHOICE : QuestionTypeEnum.MULTIPLE_CHOICE;

		MultipleOrSingleChoiceQuestion multiSingleChoiceQuestion = new MultipleOrSingleChoiceQuestion(questionType, questionText);

		multiSingleChoiceQuestion.setPercentageAnswersRight(percentageCorrect);

		for (int i = 0; i < answerOptions.length; i++) {

			multiSingleChoiceQuestion.addAnswerOption(answerOptions[i], correctAnswersArray[i]);
		}

		return multiSingleChoiceQuestion;
	}


	/**
	 * Extracts percentage value from cell C4, which says how many players gave the correct answer
	 * for this question.
	 *
	 * @param sheet  Must be sheet with question.
	 *
	 * @return  Percentage of answers for this answer which were correct.
	 *
	 * @throws KahootException  No percentage value found in cell C4.
	 */
	protected float extractPercentageOfRightAnswers(XSSFSheet sheet)  throws KahootException {

		XSSFRow row = sheet.getRow(ROW_INDEX_PLAYERS_CORRECT_PERCENT);

		XSSFCell cell = row.getCell(COL_INDEX_PLAYERS_CORRECT_PERCENT);

		// Percent value 100% is returned as 1.0, so times 100 to get 100%
		return (float) cell.getNumericCellValue() * 100f;
	}


	/**
	 * Extract which answer options are true and false from cells C9, E9, G9 or I9.
	 *
	 * @param sheet  Sheet with question of type single-choice, multi-choice or true/false.
	 *
	 * @return  Array of flags (2 till 4) saying which answer options are correct (true) or incorrect (false).
	 *
	 * @throws KahootException  Error during attempt to extract info from sheet.
	 */
	protected boolean[] extractCorrectAnswerOptions(XSSFSheet sheet) throws KahootException {

		boolean[] resultArray = null;

		XSSFRow row = sheet.getRow(ROW_INDEX_ANSWER_CORRECT);

		XSSFCell cell1 = row.getCell(COL_INDEX_ANSWER_CORRECT_1);
		XSSFCell cell2 = row.getCell(COL_INDEX_ANSWER_CORRECT_2);
		XSSFCell cell3 = row.getCell(COL_INDEX_ANSWER_CORRECT_3);
		XSSFCell cell4 = row.getCell(COL_INDEX_ANSWER_CORRECT_4);

		if (isCellEmpty(cell1, true)) { throw new KahootException("Cell with true/false for first  answer option is empty."); }
		if (isCellEmpty(cell2, true)) { throw new KahootException("Cell with true/false for second answer option is empty."); }

		int numberOfAnswerOptions = 2;

		if (isCellEmpty(cell3, true) == false) {

			numberOfAnswerOptions = 3;

			if (isCellEmpty(cell4, true) == false) {

				numberOfAnswerOptions = 4;
			}
		}

		resultArray = new boolean[numberOfAnswerOptions];

		resultArray[0] = StringUtils.isSymbolForCorrectAnwerOption( cell1.getStringCellValue().charAt(0) );
		resultArray[1] = StringUtils.isSymbolForCorrectAnwerOption( cell2.getStringCellValue().charAt(0) );

		if (numberOfAnswerOptions >= 3) {

			resultArray[2] = StringUtils.isSymbolForCorrectAnwerOption( cell3.getStringCellValue().charAt(0) );
		}

		if (numberOfAnswerOptions == 4) {

			resultArray[3] = StringUtils.isSymbolForCorrectAnwerOption( cell4.getStringCellValue().charAt(0) );
		}

		return resultArray;
	}


	/**
	 * Count number of correct answers.
	 * <br><br>
	 *
	 * Method is public to allow simple unit testing.
	 *
	 * @param isCorrectAnswerArray  Array of flags saying which answer options are correct; length must be at
	 *                              least 2 and at most 4.
	 *
	 * @return  Number of correct answers (i.e. {@code true} in argument {@code isCorrectAnswerArray}),
	 *          is at least 1 and at most 3.
	 *
	 * @throws KahootException  Inconsistent result detected, e.g. zero correct answers.
	 */
	public static int countNumberOfCorrectAnswerOptions(boolean[] isCorrectAnswerArray) throws KahootException {

		int counter = 0;
		for (boolean bool: isCorrectAnswerArray) {
			if (bool) { counter++; }
		}

		if (counter == 0) { throw new KahootException("No correct answer options found."); }

		if (counter > 3) { throw new KahootException("More than three correct answer options, namely " + counter + "."); }

		return counter;
	}


	/**
	 * Extract correct answer from sheet with true/false question.
	 * The method checks both cells C9 and E9, but one of them would be sufficient to determine if the statement
	 * is wrong or right; however, checking the symbol in both cells allows to perform a simple consistency check.
	 *
	 * @param sheet  Sheet must contain a true/false question detected by method {@link #extractQuestionFromSheet(XSSFSheet)}.
	 *
	 * @return  {@code true} iff the statement of the true/false question is true.
	 *
	 * @throws KahootException  Could not determine if statement is marked as true or false.
	 */
	protected boolean extractAnswerForTrueFalseQuestion(XSSFSheet sheet) throws KahootException {

		// First we find out if the correct answer is in the first or second answer option

		XSSFRow answerOptionRow = sheet.getRow(ROW_INDEX_ANSWER_CORRECT);

		XSSFCell answerOptionCell1 = answerOptionRow.getCell(COL_INDEX_ANSWER_CORRECT_1);
		XSSFCell answerOptionCell2 = answerOptionRow.getCell(COL_INDEX_ANSWER_CORRECT_2);

		String answerOptionStr1 = answerOptionCell1.getStringCellValue();
		String answerOptionStr2 = answerOptionCell2.getStringCellValue();

		char answerOptionChar1 = answerOptionStr1.charAt(0);
		char answerOptionChar2 = answerOptionStr2.charAt(0);


		boolean correctOptionIsFirstColumn = false;
		if ( StringUtils.isSymbolForCorrectAnwerOption(answerOptionChar1) == true  &&
			 StringUtils.isSymbolForCorrectAnwerOption(answerOptionChar2) == false
		   ) {

			correctOptionIsFirstColumn = true;

		} else
			if ( StringUtils.isSymbolForCorrectAnwerOption(answerOptionChar1) == false &&
				 StringUtils.isSymbolForCorrectAnwerOption(answerOptionChar2) == true
			   ) {

				correctOptionIsFirstColumn = false;

		} else {
			
			throw new KahootException("Could not determine for true/false question if correct option is first or second one.");
		}
			

		// Now we have to find out if the option which is marked as correct stands for "right" or "wrong"

		XSSFRow isAnswerCorrectRow = sheet.getRow(ROW_INDEX_ANSWER_OPTIONS);

		XSSFCell isCorrectCell1 = isAnswerCorrectRow.getCell(COL_INDEX_ANSWER_OPTION_1);
		XSSFCell isCorrectCell2 = isAnswerCorrectRow.getCell(COL_INDEX_ANSWER_OPTION_2);

		String str1 = isCorrectCell1.getStringCellValue();
		String str2 = isCorrectCell2.getStringCellValue();

		if ( str1.equalsIgnoreCase("true") && correctOptionIsFirstColumn == true ) {

			return true;
		}
		if ( str2.equalsIgnoreCase("true") && correctOptionIsFirstColumn == false ) {

			return true;
		}
		if ( str1.equalsIgnoreCase("false") && correctOptionIsFirstColumn == true ) {

			return false;
		}
		if ( str2.equalsIgnoreCase("false") && correctOptionIsFirstColumn == false ) {

			return false;
		}


		throw new KahootException("Could not determine if statement of true/false question is right or wrong.");
	}


	/**
	 * Checks if answer options are those of a true/false question.
	 * <br><br>
	 *
	 * Method is public to allow simple unit testing.
	 *
	 * @param answerOptionsArray  Array obtained from method {@link #getAnswerOptions(XSSFSheet)}.
	 *
	 * @return  {@code true} if argument {@code answerOptionsArray} indicates a true/false questions,
	 *          i.e. first element contains "false" and second element contains "true", and
	 *          {@code answerOptionsArray} contains exactly two elements.
	 */
	public static boolean checkIsTrueFalseQuestion(String[] answerOptionsArray) {

		if (answerOptionsArray.length != 2) { return false; }

		String wert1 = answerOptionsArray[0].toLowerCase();
		String wert2 = answerOptionsArray[1].toLowerCase();

		return ( wert1.equals("false") && wert2.equals("true") ) || ( wert1.equals("true") && wert2.equals("false") );
	}


	/**
	 * Extract question text from sheet.
	 *
	 * @param sheet  Sheet with question (must be a question sheet!).
	 *
	 * @return  String with question text in cell B2.
	 */
	protected String getQuestionText(XSSFSheet sheet) {

		XSSFRow  row  = sheet.getRow(ROW_INDEX_QUESTION_TEXT);
		XSSFCell cell = row.getCell(COL_INDEX_QUESTION_TEXT);

		String   questionText = cell.getStringCellValue();

		return questionText.trim();
	}


	/**
	 * Get answer options from line 8; seeking in cells D8, F8, H8 and J8.
	 *
	 * @param sheet  Sheet with question (must be a question sheet!).
	 *
	 * @return Array with answer options, e.g. <code>["France", "Switzerland", "Spain", "Austria"]</code>;
	 *         will have at least two and at most four elements.
	 */
	protected String[] getAnswerOptions(XSSFSheet sheet) {

		XSSFRow row = sheet.getRow(ROW_INDEX_ANSWER_OPTIONS);

		XSSFCell cell1 = row.getCell(COL_INDEX_ANSWER_OPTION_1);
		XSSFCell cell2 = row.getCell(COL_INDEX_ANSWER_OPTION_2);
		XSSFCell cell3 = row.getCell(COL_INDEX_ANSWER_OPTION_3);
		XSSFCell cell4 = row.getCell(COL_INDEX_ANSWER_OPTION_4);

		int numOfAnswerOptions = 2;

		if (isCellEmpty(cell3, true) == false) {

			numOfAnswerOptions = 3;

			if (isCellEmpty(cell4, true) == false) {

				numOfAnswerOptions = 4;
			}
		}


		String[] resultArray = new String[numOfAnswerOptions];

		resultArray[0] = cell1.getStringCellValue().trim();
		resultArray[1] = cell2.getStringCellValue().trim();

		if (numOfAnswerOptions >= 3) {

			resultArray[2] = cell3.getStringCellValue().trim();
		}
		if (numOfAnswerOptions == 4) {

			resultArray[3] = cell4.getStringCellValue().trim();
		}

		return resultArray;
	}


	/**
	 * Check if {@code cell} is empty.
	 *
	 * @param cell  Cell to be checked for being empty.
	 *
	 * @param isString  {@code true} if this cell should contain a String, {@code false} for other
	 *                  cell types like cells with percentage value. In case of a string cell an
	 *                  additional test if performed.
	 *
	 * @return  {@code true} iff this cell is empty.
	 */
	protected boolean isCellEmpty(XSSFCell cell, boolean isString) {

		if (cell == null) { return true; }

		if (cell.getCellType() == CellType.BLANK) { return true; }

		if (isString && cell.getStringCellValue().trim().length() == 0) { return true; }

		return false;
	}

}
