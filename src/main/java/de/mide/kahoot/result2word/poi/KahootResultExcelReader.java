package de.mide.kahoot.result2word.poi;

import de.mide.kahoot.result2word.model.QuestionList;
import de.mide.kahoot.result2word.utils.KahootException;
import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * This class contains the logic to load a XLSX file with Kahoot results and extract the
 * relevant information.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class KahootResultExcelReader {

	/** Object representing the whole XLSX file which consists of several sheets. */
	protected XSSFWorkbook _excelWorkbook = null;


	/**
	 * Load Excel file with Kahoot results.
	 *
	 * @param pathToExcelFile  Relative path to Excel file to be loaded.
	 *
	 * @throws KahootException  File was not found
	 */
	public KahootResultExcelReader(String pathToExcelFile) throws KahootException {

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
	 */
	public QuestionList extractQuestionList() {
		
		
		return null;
	}

}
