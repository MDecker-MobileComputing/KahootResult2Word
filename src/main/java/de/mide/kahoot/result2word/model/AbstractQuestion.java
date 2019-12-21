package de.mide.kahoot.result2word.model;


/**
 * Abstract superclass for classes representing a single question from the result file.
 * For different types of questions there are non-abstract subclasses of this class. 
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public abstract class AbstractQuestion {

	/** Question type, e.g {@link QuestionTypeEnum#SINGLE_CHOICE}. */
	protected QuestionTypeEnum _questionType;
	
	
	/** String with the question or statement (for true/false questions). */
	protected String _questionText = "";
	
	
	/**
	 * Construct new question object, type of question and question text need to be supplied.
	 * 
	 * @param questionType  Type of question
	 * 
	 * @param questionText  Text of question (which is a statement for true/false questions).
	 */
	public AbstractQuestion(QuestionTypeEnum questionType, String questionText) {
		
		_questionType = questionType;
		_questionText = questionText;
	}
	
	
	/**
	 * Setter for question text.
	 * 
	 * @param questionText  Text that was displayed to player, e.g. question or statement
	 *                      for true/false questions.
	 */
	public void setQuestionText(String questionText) {
		
		_questionText = questionText;
	}
	

	/**
	 * Getter for question text.
	 * 
	 * @return  Text that was displayed to player, e.g. question or statement for true/false questions.          
	 */
	public String getQuestionText() {
		
		return _questionText;
	}
	
	
	/**
	 * Getter for question type.
	 * 
	 * @return  Value from {@link QuestionTypeEnum}.
	 */
	public QuestionTypeEnum getQuestionType() {
		
		return _questionType;
	}
	
	
	/**
	 * Method to query if this question is of type {@link QuestionTypeEnum#SINGLE_CHOICE}.
	 * 
	 * @return {@code true} iff this question is of type {@link QuestionTypeEnum#SINGLE_CHOICE}
	 */
	public boolean getIsSingleChoiceQuestion() {
		
		return _questionType == QuestionTypeEnum.SINGLE_CHOICE;
	}
		
}
