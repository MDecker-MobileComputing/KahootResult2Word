package de.mide.kahoot.test;
import java.io.File;

import org.junit.Test;

import de.mide.kahoot.result2word.utils.XlsxFileListFilter;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


/**
 * Unit test methods for test class {@code de.mide.kahoot.result2word.utils.XlsxFileListFilter}.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class XlsxFileListFilterTests {

	private File _directoryFile = new File("path/to/folder");
	
	/** Object of class/code under test (CUT). */
	private XlsxFileListFilter _xlsxFileListFilterCut = new XlsxFileListFilter();
		

	/** Checks some cases when file should be accepted. */
	@Test
	public void fileAccepted() {
	
		boolean isAccepted = false;
		
		isAccepted = _xlsxFileListFilterCut.accept(_directoryFile, "input.xlsx");
		assertTrue(isAccepted);
		
		isAccepted = _xlsxFileListFilterCut.accept(_directoryFile, "input.XLSX");
		assertTrue(isAccepted);		
		
		isAccepted = _xlsxFileListFilterCut.accept(_directoryFile, "input.Xlsx");
		assertTrue(isAccepted);
		
		isAccepted = _xlsxFileListFilterCut.accept(_directoryFile, "input.xlsX");
		assertTrue(isAccepted);		
	}
	
	
	/** Checks some cases when file should NOT be accepted. */
	@Test
	public void fileNotAccepted() {
		
		boolean isAccepted = false;
		
		isAccepted = _xlsxFileListFilterCut.accept(_directoryFile, "input.docx");
		assertFalse(isAccepted);
				
		isAccepted = _xlsxFileListFilterCut.accept(_directoryFile, "xlsx");
		assertFalse(isAccepted);		
	}
	
}
