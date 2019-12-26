package de.mide.kahoot.result2word;

import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import de.mide.kahoot.result2word.model.QuestionList;
import de.mide.kahoot.result2word.poi.KahootResultDocxWriter;
import de.mide.kahoot.result2word.poi.KahootResultXlsxReader;
import de.mide.kahoot.result2word.utils.KahootException;
import de.mide.kahoot.result2word.utils.StringUtils;
import de.mide.kahoot.result2word.utils.TranslatedTextsProvider;

import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.parseCommandLineArguments;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.printHelpOnCmdLineArgs;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.CMDLINE_OPTION_LETTER_H_FOR_HELP;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.CMDLINE_OPTION_LETTER_L_FOR_LOCALE;;


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
	 * @param args  Command line argument with path to Excel file to be processed,
	 *              for example "ExampleFiles/input_result1.xlsx".
	 */
    public static void main(String[] args)  {
        
        CommandLine cmdLine = null;
        try {
        	
        	cmdLine = parseCommandLineArguments( args );        	        	
        }
        catch (ParseException ex) {
        	
        	printHelpOnCmdLineArgs();
        	System.exit(1);
        }
        
        
        if ( cmdLine.hasOption(CMDLINE_OPTION_LETTER_H_FOR_HELP) ) {
        	
        	printHelpOnCmdLineArgs();
        	return;
        }
        
        
        
        TranslatedTextsProvider.loadResourceBundle(Locale.ENGLISH);
        //TranslatedTextsProvider.loadResourceBundle(Locale.FRENCH);
        //TranslatedTextsProvider.loadResourceBundle(Locale.GERMAN);
                
        if (cmdLine.hasOption(CMDLINE_OPTION_LETTER_L_FOR_LOCALE)) {
        	
        	System.out.println("\nCommand Line Option -l for setting local not supported, ignoring it.");        	
        }
        
        if (!cmdLine.hasOption(CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER) && !cmdLine.hasOption(CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE)) {
        	
        	System.out.println("\nNeither Command Line Option -i nor -f was specified, aborting program.");
        	return;
        }
        
        if (cmdLine.hasOption(CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER)) {
        	
        	System.out.println("\nCommand Line option -i not yet supported, aborting program.");
        	return;
        }
        
        try {
        	
        	String inputFileName = "";
        	
            if (cmdLine.hasOption(CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE)) {
            	
            	inputFileName = cmdLine.getOptionValue(CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE);
            	
            	xlsx2docx( inputFileName );
            }
        	        	        	
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
