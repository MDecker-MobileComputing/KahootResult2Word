package de.mide.kahoot.test;

import static org.junit.Assert.fail;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER;
import static de.mide.kahoot.result2word.utils.CmdLineArgsParser.CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import de.mide.kahoot.result2word.utils.CmdLineArgsParser;


/**
 * Unit tests for class {@code de.mide.kahoot.result2word.utils.CmdLineArgsParser}. 
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class CmdLineArgsParserTests {

	/**
	 * Call method {@code de.mide.kahoot.result2word.utils.CmdLineArgsParser.parseCommandLineArguments(String[])} 
	 * with illegal argument "-x".
	 */
	@Test
	public void exceptionOnIllegalComandLineArgument() {
		
		try {
			CmdLineArgsParser.parseCommandLineArguments(new String[]{"-x"});
			
			fail("No exception raised for illegal command line argument");
		}						
		catch (ParseException ex) { /* Expected exception was called. */ } 
	}
	

	/**
	 * Call method {@code de.mide.kahoot.result2word.utils.CmdLineArgsParser.parseCommandLineArguments(String[])}
	 * with argument "-i" but no value for input file specified. 
	 */
	@Test
	public void exceptionOnCommandLineArgumentWithMissingOption() {
		
		try {
			CmdLineArgsParser.parseCommandLineArguments(new String[]{"-i"});
			
			fail("No exception raised for incomplete command line argument");
		}						
		catch (ParseException ex) { /* Expected exception was called. */ }		
	}	

	
	/**
	 * Call method {@code de.mide.kahoot.result2word.utils.CmdLineArgsParser.parseCommandLineArguments(String[])}.
	 * 
	 *  @throws ParseException  Test failed
	 */
	@Test
	public void happyPath() throws ParseException {
		
		final String INPUT_FILE = "path/to/myInputFile.xlsx";
		
		CommandLine cmdLine = CmdLineArgsParser.parseCommandLineArguments(new String[]{"-i", INPUT_FILE});
		
		assertTrue(  cmdLine.hasOption(CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER) );
		assertFalse( cmdLine.hasOption(CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE) );

		assertEquals(INPUT_FILE, cmdLine.getOptionValue(CMDLINE_OPTION_LETTER_I_FOR_INPUT_FOLDER)); 		
	}
	
	
	/**
	 * Test for method {@code de.mide.kahoot.result2word.utils.CmdLineArgsParser.getToplineText()}.
	 * 
	 * @throws ParseException  Test failed
	 */
	@Test
	public void topline() throws ParseException {
		
		final String INPUT_FOLDER = "path/to/input/folder";		
		
		
		String[] args = new String[]{"-f", INPUT_FOLDER};
		CommandLine cmdLine = CmdLineArgsParser.parseCommandLineArguments(args);
		
		assertTrue(  cmdLine.hasOption(CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE) );
		
		Optional<String> optionalString = CmdLineArgsParser.getToplineText();
		
		assertFalse( optionalString.isPresent() );
		
		
		// now the same, but with topline text
		final String TOPLINE_TEXT = "Lorem Ipsum";
		
		args = new String[]{"-f", INPUT_FOLDER, "-t", TOPLINE_TEXT};
		cmdLine = CmdLineArgsParser.parseCommandLineArguments(args);
		
		assertTrue( cmdLine.hasOption(CMDLINE_OPTION_LETTER_F_FOR_INPUT_FILE) );
		
		optionalString = CmdLineArgsParser.getToplineText();
		assertTrue( optionalString.isPresent() );
		assertEquals( TOPLINE_TEXT, optionalString.get()); 
	}
	
}
