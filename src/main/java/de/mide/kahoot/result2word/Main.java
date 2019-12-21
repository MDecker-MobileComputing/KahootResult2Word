package de.mide.kahoot.result2word;


/**
 * Main class of the application, i.e. will be referenced by attribute {@code mainClass} in manifest file
 * of the jar containing this application.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class Main {
    
	/**
	 * Entry point of the program execution.
	 * 
	 * @param args  Command line argument with path to Excel file to be processed.
	 */
    public static void main(String[] args) {
    
        if (args.length != 1) {
        	
        	System.out.println("Program was started without valid command line arguments.");
        	System.out.println("Exactly one argument must be supplied, namely path to Excel file.");
        	System.exit(1);
        }
        
        String filenameInput = args[0];
        
        System.out.println("Should now load file " + filenameInput);
    }
    
}
