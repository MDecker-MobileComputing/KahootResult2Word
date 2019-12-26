package de.mide.kahoot.result2word.utils;

import java.io.File;
import java.io.FilenameFilter;


/** 
 * Custom filter class that accepts all files with suffix {@code .xlsx} (case insensitive).  
 * This filter is needed by method {@link DirectoryUtil#findAllXlsxFilesInDirectory(String)}.
 */
public class XlsxFileListFilter implements FilenameFilter {
	
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