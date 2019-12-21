package de.mide.kahoot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.mide.kahoot.result2word.model.AnswerOption;
import de.mide.kahoot.result2word.model.MultipleSingleChoiceQuestion;
import de.mide.kahoot.result2word.model.QuestionTypeEnum;
import de.mide.kahoot.result2word.utils.KahootException;



/**
 * Tests for class {@code de.mide.kahoot.result2word.model.MultipleSingleChoiceQuestion}.
 * <br><br>
 * 
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class MultipleSingleChoiceQuestionTests 
{
    @Test
    public void rejectIllegalQuestionType() {
    
    	try {
    		new MultipleSingleChoiceQuestion(QuestionTypeEnum.TRUE_OR_FALSE, "test");
    		
    		fail("No exception raised when trying to create MultipleSingleChoiceQuestion with illegal question type.");
    	}
    	catch (KahootException ex) { /* expected exception */ }
    }
    
    
    /**
     * No exception should be raised when legal question type is supplied.
     * 
     * @throws KahootException  When exception is not raised then test is green, otherwise it is red. 
     */
    @Test
    public void acceptLegalQuestionType() throws KahootException {
    	
    	new MultipleSingleChoiceQuestion(QuestionTypeEnum.SINGLE_CHOICE, "test-1");
    	
    	new MultipleSingleChoiceQuestion(QuestionTypeEnum.MULTIPLE_CHOICE, "test-2");
    }
    
    
    @Test
    public void rejectMoreThanFourAnswers() throws KahootException {
    
    	MultipleSingleChoiceQuestion cut = new MultipleSingleChoiceQuestion(QuestionTypeEnum.MULTIPLE_CHOICE, "test");
    	
    	assertEquals(0, cut.getNumberOfAnswerQuestions());
    	
    	cut.addAnswerOption("answer-1", true );    	
    	assertEquals(1, cut.getNumberOfAnswerQuestions()); 
    	assertEquals(1, cut.getNumberOfRightAnswerOtpions());
    	
    	cut.addAnswerOption("answer-2", false);
    	assertEquals(2, cut.getNumberOfAnswerQuestions());
    	assertEquals(1, cut.getNumberOfRightAnswerOtpions());
    	
    	cut.addAnswerOption("answer-3", true );
    	assertEquals(3, cut.getNumberOfAnswerQuestions());
    	assertEquals(2, cut.getNumberOfRightAnswerOtpions());
    	
    	cut.addAnswerOption("answer-4", false);
    	assertEquals(4, cut.getNumberOfAnswerQuestions());
    	assertEquals(2, cut.getNumberOfRightAnswerOtpions());
    	
    	try {
    		cut.addAnswerOption("answer-5", false);

    		fail("No exception was raised upon attempt to add more than four answer options.");
    	}
    	catch (KahootException ex) { /* expected exception */ } 
 
    	assertEquals(4, cut.getNumberOfAnswerQuestions());
    	assertEquals(2, cut.getNumberOfRightAnswerOtpions());
    }

    
    @Test
    public void rejectMoreThanOneCorrectAnswerForSingleChoice() throws KahootException {
    	
    	MultipleSingleChoiceQuestion cut = new MultipleSingleChoiceQuestion(QuestionTypeEnum.SINGLE_CHOICE, "test");
    	
    	cut.addAnswerOption("answer-1", true );    	
    	assertEquals(1, cut.getNumberOfAnswerQuestions());
    	
    	cut.addAnswerOption("answer-2", false);
    	assertEquals(2, cut.getNumberOfAnswerQuestions());
 
    	
    	try {
    		cut.addAnswerOption("answer-3", true);
    		
    		fail("No exception was raised for 2nd true answer option of single-choice question.");
    	}
    	catch (KahootException ex) {
    		// expected exception
    	}     	
    	
    	assertEquals(2, cut.getNumberOfAnswerQuestions());
    }
    
    
    @Test
    public void getAnswerOptionText() throws KahootException {
    	
    	MultipleSingleChoiceQuestion cut = new MultipleSingleChoiceQuestion(QuestionTypeEnum.SINGLE_CHOICE, "test");
    	
    	try {
    		cut.getAnswerOptionText(0);
    		
    		fail("No exception for attempt to get answer option text with number 0.");
    	}    		
    	catch (KahootException ex) { /* expected exception */ }
    	
    	cut.addAnswerOption("answer-1", true );
    	
    	AnswerOption answer = cut.getAnswerOptionText(1);
    	
    	assertEquals("answer-1", answer.getAnswerOptionText());
    	assertTrue(answer.getAnswerOptionIsRight());
    	
    	
    	try {
    		cut.getAnswerOptionText(2);
    		
    		fail("No exception for attempt to get answer option text with number 2 (which was not set).");
    	}    		
    	catch (KahootException ex) { /* expected exception */ }
    	
    	
    	cut.addAnswerOption("answer-2", false );
    	answer = cut.getAnswerOptionText(2);
    	assertEquals("answer-2", answer.getAnswerOptionText());
    	assertFalse(answer.getAnswerOptionIsRight());
    	
    	
    	// Check that first answer option was not changed by adding second answer option    	
    	answer = cut.getAnswerOptionText(1);
    	
    	assertEquals("answer-1", answer.getAnswerOptionText());
    	assertTrue(answer.getAnswerOptionIsRight());    	
    }
    
}
