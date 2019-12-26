package de.mide.kahoot.result2word;

import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.CMDLINE_OPTION_LETTER_H_FOR_HELP;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.CMDLINE_OPTION_LETTER_L_FOR_LOCALE;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.parseCommandLineArguments;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.printHelpOnCmdLineArgs;
import static de.mide.kahoot.result2word.utils.DirectoryUtil.findAllXlsxFilesInDirectory;
import static de.mide.kahoot.result2word.utils.TranslatedTextsProvider.writeWarningWhenLocaleIsNotSupported;

import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

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
	
	/** Result code (RC) for aborting program with {@code System::exit(int)} when inconsistent command line arguments are specified. */ 
	protected static final int RESULT_CODE_ON_INVALID_ARGS = 1;
	
	/** Result code (RC) for aborting program with {@code System::exit(int)} when exception during reading of input file or writing of docx files has occured. */ 
	protected static final int RESULT_CODE_ON_EXCEPTION_DURING_PROCESSING = 2;

    
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
            System.exit( RESULT_CODE_ON_INVALID_ARGS );
        }
        
        
        abortBasedOnCommandLineArgsIfNeeded( cmdLine ); 
        loadLanguage( cmdLine );

        
        // When we come to this line, then programm was started either with cmdline option -i <inputFolder> or -f <inputFile>
        
        try {
                        
            if (cmdLine.hasOption(CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE)) {
                                                                
            	proccessSingleExcelFile( cmdLine );
            	
            } else {
            	
            	processAllExcelFilesInDirectory( cmdLine );
            }
        } 
        catch (KahootException ex) {
            
            System.err.println( "Error: " + ex.getMessage() );
            ex.printStackTrace();
            
            System.exit( RESULT_CODE_ON_EXCEPTION_DURING_PROCESSING );
        }
    }
    
    
    /**
     * Method for processing when program was called to process single xlsx file 
     * (which will be read from {@code CmdLineArgsParser}).
     * 
     * @param cmdLine  Object with result of parsing command line arguments.
     * 
     * @throws KahootException  Something went wrong during performing "xlsx2docx".
     */
    protected static void proccessSingleExcelFile(CommandLine cmdLine) throws KahootException {
    	
    	String inputFileName = cmdLine.getOptionValue(CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE);
    	
    	xlsx2docx( inputFileName );
    }
    

    /**
     * Method for processing when program was called to process all xlsx files form a particular folder 
     * (which will be read from {@code CmdLineArgsParser}).
     * 
     * @param cmdLine  Object with result of parsing command line arguments.
     * 
     * @throws KahootException  Something went wrong during performing "xlsx2docx" or when no xlsx files are found
     *                          in the specified folder.
     */
    protected static void processAllExcelFilesInDirectory(CommandLine cmdLine) throws KahootException {
    	
    	String inputFolder= cmdLine.getOptionValue(CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER);
    	
    	String[] xlsxFilesInFolderStringArray = findAllXlsxFilesInDirectory( inputFolder );
    	
    	int numOfXlsxFiles = xlsxFilesInFolderStringArray.length;
    	if (numOfXlsxFiles == 0) {
    		
    		throw new KahootException("No xlsx files found in folder \"" + inputFolder + "\".");
    	}
    	
    	System.out.println("\nNumber of xlsx files found in input folder: " + numOfXlsxFiles + "\n");
    	
    	for (String fileName: xlsxFilesInFolderStringArray) {
    		
    		xlsx2docx( fileName );
    		
    		System.out.println("  file  \"" + fileName + "\" was processed.");    		    		
    	}
    	
    	System.out.println();
    }
    
    
    /**
     * Programmatic consistency checks of command line arguments:
     * When neither option {@code -i} nor {@code -f} is specified, then program executed is aborted with result 
     * code {@link #RESULT_CODE_ON_INVALID_ARGS}; the program is also aborted when both options are specified
     * at the same time (they are not compatible().
     * 
     * @param cmdLine  Object with result of parsing command line arguments.
     */    
    protected static void abortBasedOnCommandLineArgsIfNeeded(CommandLine cmdLine) {
    	
        if (!cmdLine.hasOption(CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER) && !cmdLine.hasOption(CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE)) {
            
            System.out.println("\nNeither Command Line Option -i nor -f was specified, aborting program.");
            System.exit(1);
        }
        
        
        if (cmdLine.hasOption(CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER) && cmdLine.hasOption(CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE)) {

            System.out.println("\nBoth command line options -i and -f were specified, aborting program.");
            System.exit(1);        	
        }
        
        
        if ( cmdLine.hasOption(CMDLINE_OPTION_LETTER_H_FOR_HELP) ) {
            
            printHelpOnCmdLineArgs();
            System.exit(0);
        }        
    }
    
    
    /**
     * Evaluate command line argument to set locale.
     * 
     * @param cmdLine  Object with result of parsing command line arguments.
     */
    protected static void loadLanguage(CommandLine cmdLine) {
    	
        if (cmdLine.hasOption(CMDLINE_OPTION_LETTER_L_FOR_LOCALE)) {
            
            String localeCode = cmdLine.getOptionValue(CMDLINE_OPTION_LETTER_L_FOR_LOCALE);
            Locale locale     = new Locale(localeCode);
            TranslatedTextsProvider.loadResourceBundle(locale);
            
            writeWarningWhenLocaleIsNotSupported(locale);
            
        } else {
        
            TranslatedTextsProvider.loadResourceBundle(Locale.ENGLISH);
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
