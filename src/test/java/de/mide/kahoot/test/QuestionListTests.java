package de.mide.kahoot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.mide.kahoot.result2word.model.AbstractQuestion;
import de.mide.kahoot.result2word.model.MultipleOrSingleChoiceQuestion;
import de.mide.kahoot.result2word.model.QuestionList;
import de.mide.kahoot.result2word.model.QuestionTypeEnum;
import de.mide.kahoot.result2word.model.TrueFalseQuestion;
import de.mide.kahoot.result2word.utils.KahootException;


/** 
 * Unit tests for class {@code de.mide.kahoot.result2word.model.QuestionList}.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class QuestionListTests {
	
	/** Class/Code under test (CUT), is initialized in method {@link #prepare()}.*/ 
	private QuestionList _cut = null; 
	
	/** Multi-choice question, stored with index=0. */
	private AbstractQuestion _questionMC = null; 
	
	/** Single-choice question, stored with index=1. */
	private AbstractQuestion _questionSC = null;
	
	/** True/false question, stored with index=2. */
	private AbstractQuestion _questionTF = null;
	
	
	/**
	 * Method is executed before each unit test method.
	 * 
	 * @throws KahootException  Something went wrong
	 */
	@Before
	public void prepare() throws KahootException {
		
		_cut = new QuestionList();
		
		_questionMC = new MultipleOrSingleChoiceQuestion(QuestionTypeEnum.MULTIPLE_CHOICE, "MC-1");
		_questionSC = new MultipleOrSingleChoiceQuestion(QuestionTypeEnum.SINGLE_CHOICE  , "MC-2");
		_questionTF = new TrueFalseQuestion("test-3", true);
		
		_cut.addQuestion( _questionMC );
		_cut.addQuestion( _questionSC );
		_cut.addQuestion( _questionTF );		
	}

	
	@Test
	public void twoQuestions() throws KahootException {
				  												
		assertEquals(3, _cut.getNumberOfQuestions());
		
		assertEquals( QuestionTypeEnum.MULTIPLE_CHOICE, _cut.getTypeOfQuestion(0) );
		assertEquals( QuestionTypeEnum.SINGLE_CHOICE  , _cut.getTypeOfQuestion(1) );
		assertEquals( QuestionTypeEnum.TRUE_OR_FALSE  , _cut.getTypeOfQuestion(2) );								
	}
	
	
	/**
	 * Tests for raising of exception when too high index values to access a list element are used.
	 */
	@Test
	public void indexTooHigh() {
		
		final int indexTooHigh = 4;
		
		try {
			_cut.getTypeOfQuestion(indexTooHigh);
			
			fail("No exception raised for illegal index 4.");
		}
		catch (KahootException ex) { /* expected exception */ }
				
		
		try {
			_cut.getMultiSingleChoiceQuestion(indexTooHigh);
			
			fail("No exception raised for illegal index 4.");
		}
		catch (KahootException ex) { /* expected exception */ }
		
		
		try {
			_cut.getTrueOrFalseQuestion(indexTooHigh);
			
			fail("No exception raised for illegal index 4.");
		}
		catch (KahootException ex) { /* expected exception */ }					
	}
	

	/**
	 * Tests for raising of exception when negative index values to access a list element are used.
	 */
	@Test			
	public void indexNegative() {
			
		final int indexNegative = -1;
		
		try {
			_cut.getTypeOfQuestion(indexNegative);
			
			fail("No exception raised for illegal index 4-1.");
		}
		catch (KahootException ex) { /* expected exception */ }		
		
		
		try {
			_cut.getMultiSingleChoiceQuestion(indexNegative);
			
			fail("No exception raised for illegal index -1.");
		}
		catch (KahootException ex) { /* expected exception */ }
		
		
		try {
			_cut.getTrueOrFalseQuestion(indexNegative);
			
			fail("No exception raised for illegal index -1.");
		}
		catch (KahootException ex) { /* expected exception */ }					
	}
	
	
	/**
	 * Test for retrieval of question objects from list.
	 * 
	 * @throws KahootException  Something went wrong, test will fail
	 */
	@Test
	public void retrievalOfQuestions() throws KahootException {
		
		assertTrue( _cut.getMultiSingleChoiceQuestion(0) == _questionMC );		
		assertTrue( _cut.getMultiSingleChoiceQuestion(1) == _questionSC );		
		assertTrue( _cut.getTrueOrFalseQuestion(2)       == _questionTF );		
	}
	
	
	/**
	 * Create list with default capacity of 2, but add 3 questions.
	 */
	@Test
	public void increaseListElements() {
		
		QuestionList cut = new QuestionList(2);
		
		cut.addQuestion( _questionMC );
		cut.addQuestion( _questionSC );
		cut.addQuestion( _questionTF );
		
		assertEquals(3, cut.getNumberOfQuestions());		
	}
	
}
