package de.mide.kahoot.result2word.utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * Parsing of command line arguments based on <a href="https://commons.apache.org/proper/commons-cli/" target="_blank">commons cli</a>.
 * <br><br>
 * 
 * See also <a href="http://blog.wenzlaff.de/?p=12952" target="_blank">this 3rd-party tutorial</a>.
 * <br><br>
 * 
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class CmdLineArgsParser {

	/** Single letter "f" for command line argument to specify input file, e.g. <code>-f path/to/result.xlsx</code> . */
	public static final String CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE = "f";
	
	/** Single letter "i" for command line argument to specify folder with xlsx files to be read, e.g. <code>-i path/to/input/folder</code> . */
	public static final String CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER = "i";
	
	/** Single letter "o" for command line argument to specify folder into which docx file are to be written, e.g. <code>-o path/to/output/folder</code> . */
	public static final String CMDLINE_OPTION_LETTER_O_FOR_OUTPUT_FOLDER = "o";
	
	/** Single letter "l" (small caps "L") for command line argument to specify the locale for the docx file to be written, e.g. <code>-l en</code>. */ 
	public static final String CMDLINE_OPTION_LETTER_L_FOR_LOCALE = "l";
	
	/** Single letter "h" for command line argument to call help on command line arguments with <code>-h</code>. */
	public static final String CMDLINE_OPTION_LETTER_H_FOR_HELP = "h";
	
	
	/** Options object (configuration for parser), will be filled in {@code static} block when class is loaded. */
	protected static Options sOptions = null; 
	
	
	static {
		
		// TODO Refactor using "Option Builder" for better readability, see https://commons.apache.org/proper/commons-cli/usage.html
		
		sOptions = new Options();
		 
		sOptions.addOption( CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE, "infile", true,  // true: has argument
				            "Single Excel file to be processed, not compatible with -i." ); 
		
		sOptions.addOption( CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER, "infolder" , true, 
				            "Folder from which input files (xlsx) are to be read; not compatible with -f." );
		
		sOptions.addOption( CMDLINE_OPTION_LETTER_O_FOR_OUTPUT_FOLDER, "outfolder", true, 
				            "Folder into which output files (docx) are to be written." );
		
		sOptions.addOption( CMDLINE_OPTION_LETTER_L_FOR_LOCALE, "locale", true, 
				            "Set locale to be used for output files, e.g. \"en\" for English or \"de\" for German." );
		
		sOptions.addOption( CMDLINE_OPTION_LETTER_H_FOR_HELP, "help", false, 
				            "Show help on command line arguments." );
	}
	
	
	/**
	 * Parse command line arguments handed over to {@code main} method. 
	 * 
	 * @param argsToBeParsed  Array of command line arguments receved in {@code main} method.
	 * 
	 * @return  Object to query which switches have been set and/or which value for a command 
	 *          argument has been set.
	 *          
	 * @throws  ParseException  Illegal arguments
	 */
	public static CommandLine parseCommandLineArguments(String[] argsToBeParsed) throws ParseException {
		
		CommandLineParser parser = new DefaultParser();
		
		CommandLine parseResult = parser.parse(sOptions, argsToBeParsed); 
				
		return parseResult;
	}
	
	
	/**
	 * Write help on command line arguments to STDOUT. 
	 */
	public static void printHelpOnCmdLineArgs() {
		
		System.out.println("\nProgram was called with illegal command line arguments.\n");
	
		HelpFormatter helpFormatter = new HelpFormatter();
		
		helpFormatter.printHelp("de.mide.kahoot.result2word.Main", sOptions);
	}
	
}
