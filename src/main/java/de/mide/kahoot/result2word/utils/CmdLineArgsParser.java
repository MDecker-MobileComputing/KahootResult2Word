package de.mide.kahoot.result2word.utils;

import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * Parsing of command line arguments based on <a href="https://commons.apache.org/proper/commons-cli/" target="_blank">Apache Commons CLI</a>.
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
	
	/** Single letter "i" for command line argument to specify folder with xlsx files to be read, e.g. <code>-i path/to/input/folder/</code> . */
	public static final String CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER = "i";
	
	/** Single letter "o" for command line argument to specify folder into which docx file are to be written, e.g. <code>-o path/to/output/folder/</code> . */
	public static final String CMDLINE_OPTION_LETTER_O_FOR_OUTPUT_FOLDER = "o";
	
	/** Single letter "l" (small caps "L") for command line argument to specify the locale for the docx file to be written, e.g. <code>-l en</code>. */ 
	public static final String CMDLINE_OPTION_LETTER_L_FOR_LOCALE = "l";
	
	/** Single letter "h" for command line argument to call help on command line arguments with <code>-h</code>. */
	public static final String CMDLINE_OPTION_LETTER_H_FOR_HELP = "h";
	
	/** Single letter "t" for command line argument to specify a topline (header) in the generated word document. */
	public static final String CMDLINE_OPTION_LETTER_T_FOR_TOPLINE = "t";
	
	
	/** Options object (configuration for parser), will be filled in {@code static} block when class is loaded. */
	protected static Options sOptions = null; 
	
	/**
	 * Optionally supplied text for a topline (header) on each page of the produced docx file; will
	 * be set in method {@link #parseCommandLineArguments(String[])}. Cannot call it "header", because
	 * "-h" is already used for "help", see {@link #CMDLINE_OPTION_LETTER_H_FOR_HELP}.
	 */
	private static Optional<String> _toplineTextOptional = Optional.empty();
	
	
	static {
				
		sOptions = new Options();
		
		Option infileOption = Option.builder(CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE)
					                    .required(false)
					                    .longOpt("infile")
					                    .desc("Single Excel file to be processed, not compatible with -i")
					                    .hasArg(true)
					                    .argName("file")
					                    .build();
			
		Option infolderOption = Option.builder(CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER)
						                .required(false)
						                .longOpt("infolder")
						                .desc("Folder from which input files (xlsx) are to be read; not compatible with -f")
						                .hasArg(true)
						                .argName("folder")
						                .build();
		
		Option outfolderOption = Option.builder(CMDLINE_OPTION_LETTER_O_FOR_OUTPUT_FOLDER)
						                .required(false)
						                .longOpt("outfolder")
						                .desc("Folder into which output files (docx) are to be written")
						                .hasArg(true)
						                .argName("folder")
						                .build();				

		Option localeOption = Option.builder(CMDLINE_OPTION_LETTER_L_FOR_LOCALE)
						                .required(false)
						                .longOpt("locale")
						                .desc("Set language to be used for output files, e.g. \"en\" for English or \"de\" for German; default value is \"en\" for English")
						                .hasArg(true)
						                .argName("locale")
						                .build();				

		Option helpOption = Option.builder(CMDLINE_OPTION_LETTER_H_FOR_HELP)
						                .required(false)
						                .longOpt("help")
						                .desc("Show this help")
						                .hasArg(false)
						                .build();		
		
		Option toplineOption = Option.builder(CMDLINE_OPTION_LETTER_T_FOR_TOPLINE)
						                .required(false)
						                .longOpt("topline")
						                .desc("Set text for topline (header) on each page of the generated docx file.")
						                .hasArg(true)
                                        .argName("text")
						                .build();		
		

		sOptions.addOption( infileOption    );
		sOptions.addOption( infolderOption  );
		sOptions.addOption( outfolderOption );
		sOptions.addOption( localeOption    );
		sOptions.addOption( helpOption      );
		sOptions.addOption( toplineOption   );
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
		
		
		if (parseResult.hasOption(CMDLINE_OPTION_LETTER_T_FOR_TOPLINE)) {
		
			String toplineString = parseResult.getOptionValue(CMDLINE_OPTION_LETTER_T_FOR_TOPLINE);
		    _toplineTextOptional = Optional.of(toplineString);
		}		
				
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
	
	
	/**
	 * Getter for topline text (header) to be put onto each page of the docx file.  
	 * 
	 * @return  Optional object that might contain a topline set as command line argument.
	 */
	public static Optional<String> getToplineText() {
		
		return _toplineTextOptional;
	}
	
}
