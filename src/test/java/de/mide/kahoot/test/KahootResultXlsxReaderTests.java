package de.mide.kahoot.test;

import org.junit.Test;

import de.mide.kahoot.result2word.model.MultipleOrSingleChoiceQuestion;
import de.mide.kahoot.result2word.model.QuestionList;
import de.mide.kahoot.result2word.model.QuestionTypeEnum;
import de.mide.kahoot.result2word.model.TrueFalseQuestion;
import de.mide.kahoot.result2word.poi.KahootResultXlsxReader;
import de.mide.kahoot.result2word.utils.KahootException;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Unit tests for class {@code de.mide.kahoot.result2word.poi.KahootResultXlsxReader}.
 * <br><br>
 * 
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class KahootResultXlsxReaderTests {

	/**
	 * Process example file {@code ExampleFiles/input_result_1.xlsx}.
	 * 
	 * @throws KahootException  Test failed
	 */
	@Test
	public void inputFile1() throws KahootException {
		
		KahootResultXlsxReader cut = new KahootResultXlsxReader("ExampleFiles/input_result_1.xlsx");

		// Call method under test
		QuestionList resultList = cut.extractQuestionList();
		
		assertEquals( 3, resultList.getNumberOfQuestions());
		
		
		// Assert first question 
		assertEquals(QuestionTypeEnum.SINGLE_CHOICE, resultList.getTypeOfQuestion(0));
		
		MultipleOrSingleChoiceQuestion question1 = resultList.getMultiSingleChoiceQuestion(0);
		
		assertEquals( 4, question1.getNumberOfAnswerQuestions   () );
		assertEquals( 1, question1.getNumberOfRightAnswerOptions() );
		assertEquals( 3, question1.getNumberOfWrongAnswerOptions() );

		
		// Assert second question 
		assertEquals(QuestionTypeEnum.MULTIPLE_CHOICE, resultList.getTypeOfQuestion(1));
		
		MultipleOrSingleChoiceQuestion question2 = resultList.getMultiSingleChoiceQuestion(1);
		
		assertEquals( 4, question2.getNumberOfAnswerQuestions   () );
		assertEquals( 2, question2.getNumberOfRightAnswerOptions() );
		assertEquals( 2, question2.getNumberOfWrongAnswerOptions() );
		
		
		// Assert third question
		assertEquals(QuestionTypeEnum.TRUE_OR_FALSE, resultList.getTypeOfQuestion(2));		
				
		TrueFalseQuestion question3 = resultList.getTrueOrFalseQuestion(2);
		
		assertTrue( question3.isStatementTrue() );
		
		assertTrue( question3.getQuestionText().indexOf("Beijing") != -1);
		
		
		assertEquals("Test Questions for XLSX2Word (1)", resultList.getTitle());
	}
	
	
	/**
	 * Process example file {@code ExampleFiles/input_result_2.xlsx}. This result file contains
	 * three multiple/single-choice questions (but no true/false question), and each of them
	 * has less than four answer options.
	 * 
	 * @throws KahootException  Test failed
	 */
	@Test
	public void inputFile2() throws KahootException {
		
		KahootResultXlsxReader cut = new KahootResultXlsxReader("ExampleFiles/input_result_2.xlsx");
	
		// Call method under test
		QuestionList resultList = cut.extractQuestionList();
		
		assertEquals( 3, resultList.getNumberOfQuestions());
		
		
		// Assert first question 
		assertEquals(QuestionTypeEnum.SINGLE_CHOICE, resultList.getTypeOfQuestion(0));
		
		MultipleOrSingleChoiceQuestion question1 = resultList.getMultiSingleChoiceQuestion(0);
		
		assertEquals( 2, question1.getNumberOfAnswerQuestions   () );
		assertEquals( 1, question1.getNumberOfRightAnswerOptions() );
		assertEquals( 1, question1.getNumberOfWrongAnswerOptions() );		
		
		
		// Assert second question 
		assertEquals(QuestionTypeEnum.MULTIPLE_CHOICE, resultList.getTypeOfQuestion(1));
		
		MultipleOrSingleChoiceQuestion question2 = resultList.getMultiSingleChoiceQuestion(1);
		
		assertEquals( 3, question2.getNumberOfAnswerQuestions   () );
		assertEquals( 2, question2.getNumberOfRightAnswerOptions() );
		assertEquals( 1, question2.getNumberOfWrongAnswerOptions() );			

		
		// Assert third question 
		assertEquals(QuestionTypeEnum.SINGLE_CHOICE, resultList.getTypeOfQuestion(2));
		
		MultipleOrSingleChoiceQuestion question3 = resultList.getMultiSingleChoiceQuestion(2);
		
		assertEquals( 3, question3.getNumberOfAnswerQuestions   () );
		assertEquals( 1, question3.getNumberOfRightAnswerOptions() );
		assertEquals( 2, question3.getNumberOfWrongAnswerOptions() );					
	}
	
	
	/**
	 * Test behaviour when Xlsx file to be loaded does not exist. 
	 */
	@Test
	public void fileNotFound() {
		
		try {
			new KahootResultXlsxReader("does_not_exist.xlsx");
			
			fail("No exception raised upon attempt to load non-existing file.");
		}
		catch (KahootException ex) { /* expected exception */ }
	}	
	
	
	/**
	 * Test for method {@code de.mide.kahoot.result2word.poi.KahootResultXlsxReader.countNumberOfCorrectAnswerOptions(boolean[])}.
	 * 
	 * @throws KahootException  Test failed
	 */
	@Test
	public void countCorrectAnswersHappyPath() throws KahootException {
		
		boolean[] inputArray1 = { false, true, false, false };
		
		// Call method under test
		int result1 = KahootResultXlsxReader.countNumberOfCorrectAnswerOptions( inputArray1 );
		
		assertEquals(1, result1);
		
		
		boolean[] inputArray2 = { false, true, true, false };
		
		// Call method under test
		int result2 = KahootResultXlsxReader.countNumberOfCorrectAnswerOptions( inputArray2 );
		
		assertEquals(2, result2);						
	}
	
	
	/**
	 * Test for method {@code de.mide.kahoot.result2word.poi.KahootResultXlsxReader.countNumberOfCorrectAnswerOptions(boolean[])}.
	 */
	@Test
	public void countCorrectAnswersErrorCases() {
		
		boolean[] allAnswerOptionsWrong = { false, false, false, false };
		
		try {
			
			// Call method under test
			KahootResultXlsxReader.countNumberOfCorrectAnswerOptions( allAnswerOptionsWrong );
			
			fail("No exception raised for four incorrect answer options.");
		}
		catch (KahootException ex) { /* expected exception */ }
		
		
		boolean[] allAnswerOptionsTrue = { true, true, true, true };
		
		try {
			
			// Call method under test
			KahootResultXlsxReader.countNumberOfCorrectAnswerOptions( allAnswerOptionsTrue );
			
			fail("No exception raised for four correct answer options.");
		}
		catch (KahootException ex) { /* expected exception */ }		
	}
	
	
	/**
	 * Test for method {@code de.mide.kahoot.result2word.poi.KahootResultXlsxReader.checkIsTrueFalseQuestion(String[])}. 
	 */
	@Test	
	public void checkIsTrueFalseQuestion() {
		
		String[] answerOptionsArray1 = { "false", "true" };
		
		// call method under test
		boolean isTrueFalseQuestion1 = KahootResultXlsxReader.checkIsTrueFalseQuestion(answerOptionsArray1);
		
		assertTrue(isTrueFalseQuestion1);
		
		
		String[] answerOptionsArray2 = { "true", "false" };
		
		// call method under test
		boolean isTrueFalseQuestion2 = KahootResultXlsxReader.checkIsTrueFalseQuestion(answerOptionsArray2);
		
		assertTrue(isTrueFalseQuestion2);		
		

		String[] answerOptionsArray3 = { "Beijing", "Paris", "London", "Rome" };
		
		// call method under test
		boolean isTrueFalseQuestion3 = KahootResultXlsxReader.checkIsTrueFalseQuestion(answerOptionsArray3);
		
		assertFalse(isTrueFalseQuestion3);
		
		
		String[] answerOptionsArray4 = { "false", "true", "foobar" };
		
		// call method under test
		boolean isTrueFalseQuestion4 = KahootResultXlsxReader.checkIsTrueFalseQuestion(answerOptionsArray4);
		
		assertFalse(isTrueFalseQuestion4);				
	}
	
}
