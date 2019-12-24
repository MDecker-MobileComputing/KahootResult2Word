package de.mide.kahoot.result2word;

import de.mide.kahoot.result2word.model.QuestionList;
import de.mide.kahoot.result2word.poi.KahootResultXlsxReader;
import de.mide.kahoot.result2word.utils.KahootException;


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
	 *      Ideas for further command line arguments: -folder path/to/folder, -gui, -help
	 *
	 * @param args  Command line argument with path to Excel file to be processed,
	 *              for example "ExampleFiles/input_result1.xlsx".
	 *
	 * @throws KahootException  Something went wrong
	 */
    public static void main(String[] args) throws KahootException {

        if (args.length != 1) {

        	System.out.println("\nProgram was started without valid command line arguments.");
        	System.out.println("Exactly one argument must be supplied, namely path to Excel file.\n");
        	System.exit(1);
        }

        String filenameInput = args[0];


        KahootResultXlsxReader xlsxReader   = new KahootResultXlsxReader(filenameInput);
        QuestionList           questionList = xlsxReader.extractQuestionList();

        System.out.println( "\n" + questionList.toString() );
    }

}
