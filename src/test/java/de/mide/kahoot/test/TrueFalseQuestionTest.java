package de.mide.kahoot.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.mide.kahoot.result2word.model.AbstractQuestion;
import de.mide.kahoot.result2word.model.QuestionTypeEnum;
import de.mide.kahoot.result2word.model.TrueFalseQuestion;


/**
 * Tests for class {@code de.mide.kahoot.result2word.model.TrueFalseQuestion}.
 * <br><br>
 * 
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class TrueFalseQuestionTest {

	
	@Test
	public void correctQuestionType() {
		
		AbstractQuestion cut = new TrueFalseQuestion("test-1", true);
	
		assertEquals(QuestionTypeEnum.TRUE_OR_FALSE , cut.getQuestionType());
	}
}
