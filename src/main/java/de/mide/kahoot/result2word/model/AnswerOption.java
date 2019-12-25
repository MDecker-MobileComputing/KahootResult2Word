package de.mide.kahoot.result2word.model;


/**
 * One object of this class contains a single answer option for a question of type {@code QuestionTypeEnum#SINGLE_CHOICE}
 * or {@code QuestionTypeEnum#MULTIPLE_CHOICE}. The object contains the text of the answer option and also a flag
 * saying whether the answer option was right or false. 
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class AnswerOption {
	
	/** Text of the answer option that was displayed to the players. */
	protected String _answerOptionText = "";
	
	/** Flag saying whether this answer option is right (true) or wrong (false). */
	protected boolean _isRightOption = false;

	
	/**
	 * Create an answer option, supply text of answer option and whether it is a right or wrong option.
	 * 
	 * @param answerOptionText  Text that was displayed to player.
	 * 
	 * @param isRight  {@code true} iff this is a right answer option. 
	 */
	public AnswerOption(String answerOptionText, boolean isRight) {
		
		_answerOptionText = answerOptionText;
		_isRightOption    = isRight;
	}

	
	/**
	 * Getter for text of the answer option.
	 * 
	 * @return Text of the answer option, was displayed to player.
	 */
	public String getAnswerOptionText() {
		
		return _answerOptionText;
	}
	
	
	/**
	 * Getter for flag saying whether the answer option of this object is right or false.
	 * 
	 * @return {@code true} iff answer option is right.
	 */	
	public boolean getAnswerOptionIsRight() {
		
		return _isRightOption;
	}
	
	
	/**
	 * Getter for text saying whether the answer option of this object is right or false.
	 *  
	 * @return  Text to be displayed in table inj word document saying if 
	 *          answer option is right or wrong.
	 */
	public String getAnswerOptionIsRightAsString() {
		
		if ( getAnswerOptionIsRight() ) {
			
			return "Right";
			
		} else {
			
			return "Wrong";
		}
	}
}
