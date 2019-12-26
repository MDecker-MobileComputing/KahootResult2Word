package de.mide.kahoot.result2word.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Class with helper method to find all xslx files in a particular folder.
 * <br><br>
 * 
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class DirectoryUtil {

	/**
	 * Method to find all xlsx files in a particular directory.
	 * 
	 * @param pathToInputDirectory  Directory in which all xlsx files are to be found.
	 * 
	 * @return  Array with file names (including path) of all xlsx files; might contain zero elements,
	 *          but won't be {@code null}.
	 *          
	 * @throws KahootException  {@code pathToInputDirectory} is not a directory or was not found.
	 */
	public static String[] findAllXlsxFilesInDirectory(String pathToInputDirectory) throws KahootException {
		
		if (checkIfDirectoryExists(pathToInputDirectory) == false) {
			
			throw new KahootException("Folder \"" + pathToInputDirectory + "\" not found or is not a folder.");
		}
		
		File directoryFile = new File( pathToInputDirectory );
				

		FilenameFilter xlsxFileFilter = new XlsxFileListFilter(); 
		
		// Method list(FileFilter) returns only file names without path
		String[] filesFoundStringArray = directoryFile.list( xlsxFileFilter );
		
		if (filesFoundStringArray == null) { return new String[]{}; }

		int numOfFilesFound = filesFoundStringArray.length;
		String[] filesFoundWithPathStringArray = new String[ numOfFilesFound ];
		
		String pathPrefix = pathToInputDirectory;
		if (pathToInputDirectory.endsWith("/") == false) { pathPrefix += "/"; }
		
		for (int i = 0; i < numOfFilesFound; i++) {
			
			filesFoundWithPathStringArray[i] = pathPrefix + filesFoundStringArray[i];
		}
		
		return filesFoundWithPathStringArray;
	}
	
	
	/** 
	 * Inner class defining custom filter class that accepts all files with suffix  
	 * {@code .xlsx} (case insensitive).
	 */
	protected static class XlsxFileListFilter implements FilenameFilter {
		
		/**
		 * Check if a file is a xlsx file or not.
		 * 
		 * @param directory  Is not considered.
		 * 
		 * @param fileName  Is checked for suffix {@code .xlsx} (Excel file).
		 * 
		 * @return  {@code true} iff {@code fileName} ends with {@code .xlsx} (case insensitive).
		 */
		@Override
		public boolean accept(File directory, String fileName) {
			
			return fileName.toLowerCase().endsWith(".xlsx");
		}
	}
	
	
	/**
	 * Check if {@code pathToDirectory} is a existing directory.
	 * 
	 * @param pathToDirectory  Path to directory to be checked.
	 * 
	 * @return  {@code true} iff {@code pathToDirectory} exists and is a directory.  
	 */
	public static boolean checkIfDirectoryExists(String pathToDirectory) {
		

		File directoryFile = new File( pathToDirectory );
		
		if (directoryFile.exists() == false) { return false; } 
								
		if (directoryFile.isDirectory() == false) { return false; }
		
		return true;
	}
	
}
