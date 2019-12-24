package de.mide.kahoot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.mide.kahoot.result2word.model.QuestionTypeEnum;
import de.mide.kahoot.result2word.model.TrueFalseQuestion;



/**
 * Tests for class {@code de.mide.kahoot.result2word.model.TrueFalseQuestion}.
 * <br><br>
 * 
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class TrueFalseQuestionTests {

	
	@Test
	public void correctQuestionType() {
		
		TrueFalseQuestion cut = new TrueFalseQuestion("test-1", true);
	
		assertEquals(QuestionTypeEnum.TRUE_OR_FALSE , cut.getQuestionType());
		
		assertFalse( cut.isMultipleChoiceQuestion() );
		assertFalse( cut.isSingleChoiceQuestion  () );
		assertTrue ( cut.isTrueOrFalseQuestion   () );
		
		assertTrue( cut.isStatementTrue() );
		
		assertEquals( "test-1", cut.getQuestionText() );
	}
	
}
