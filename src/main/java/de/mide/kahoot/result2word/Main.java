package de.mide.kahoot.result2word;

import java.util.Locale;

import de.mide.kahoot.result2word.model.QuestionList;
import de.mide.kahoot.result2word.poi.KahootResultDocxWriter;
import de.mide.kahoot.result2word.poi.KahootResultXlsxReader;
import de.mide.kahoot.result2word.utils.KahootException;
import de.mide.kahoot.result2word.utils.StringUtils;
import de.mide.kahoot.result2word.utils.TranslatedTextsProvider;


/**
 * Main class of the application, i.e. will be referenced by attribute {@code mainClass} in manifest file
 * of the jar containing this application.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class Main {

	/**
	 * Entry point of the program execution.<br><br>
	 *
	 * TODO Use "Apache Common cli ( https://commons.apache.org/proper/commons-cli/ ) to parse Command Line Arguments;
	 *      Maven coordinates: https://mvnrepository.com/artifact/commons-cli/commons-cli;
	 *      Example: https://stackoverflow.com/a/7341992
	 *      Ideas for further command line arguments: -folder path/to/folder, -gui, -help, -output
	 *
	 * @param args  Command line argument with path to Excel file to be processed,
	 *              for example "ExampleFiles/input_result1.xlsx".
	 */
    public static void main(String[] args)  {

        if (args.length != 1) {

        	System.out.println("\nProgram was started without valid command line arguments."        );
        	System.out.println("Exactly one argument must be supplied, namely path to Excel file.\n");
        	System.exit(1);
        }

        String filenameInput = args[0];
        
        TranslatedTextsProvider.loadResourceBundle(Locale.ENGLISH);
        //TranslatedTextsProvider.loadResourceBundle(Locale.FRENCH);
        //TranslatedTextsProvider.loadResourceBundle(Locale.GERMAN);
                
        try {
        	
        	xlsx2docx( filenameInput );
        } 
        catch (KahootException ex) {
        	
        	System.err.println("Error: " + ex.getMessage());
        	ex.printStackTrace();
        }
    }
    
    
    /**
     * Perform the actual work: read one input file {@code pathToInputExcel} and write one word file. 
     * 
     * @param pathToInputExcel  Relative path to Excel file with Kahoot results to be read; suffix {@code .xlsx}
     *                          is replaced with {@code .docx} to obtain name of target file.
     * 
     * @throws KahootException  Something went wrong
     */
    protected static void xlsx2docx(String pathToInputExcel) throws KahootException {
    
    	QuestionList questionList = null;
    	
        KahootResultXlsxReader xlsxReader = new KahootResultXlsxReader(pathToInputExcel);
        
        // read input file (Excel file with results downloaded from Kahoot)
        questionList = xlsxReader.extractQuestionList();

        System.out.println( "\n" + questionList.toString() + "\n");
        
        String pathToOutputWord = StringUtils.changeFilenameExtensionXlsx2Docx(pathToInputExcel);
        
        KahootResultDocxWriter docxWriter = new KahootResultDocxWriter(questionList, pathToOutputWord);
        
        docxWriter.writeResultFile();
        
        System.out.println("Target file written: " + pathToOutputWord);
    }

}
