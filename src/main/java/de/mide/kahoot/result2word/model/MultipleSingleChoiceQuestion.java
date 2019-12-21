package de.mide.kahoot.result2word.model;

import de.mide.kahoot.result2word.utils.KahootException;

/**
 * Class for single-choice or multiple choice questions; both type of questions can contain up to
 * four answer options, but for single-choice questions exactly one of these answers is true.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class MultipleSingleChoiceQuestion extends AbstractQuestion {

	/** Counter for number of answer options that were supplied; must not be greater than four. */
	protected int _numberOfAnswerOptions = 0;
	
	/** 
	 * Counter for number of right answer options that were supplied; must not be greater than one
	 * for questions of type {@link QuestionTypeEnum#SINGLE_CHOICE}.
	 */
	protected int _numberOfRightAnswerOptions = 0;
	
	/** Array for holding up to four answer option texts; is initialized with four empty strings. */
	protected String _answerOptionArray[] = new String[]{ "", "", "", ""};
	
	/** 
	 * Array for holding flags saying whether an answer option was right (true) or wrong (false), 
	 * must correspond to answer option text in {@link #_answerOptionArray}; default value is
	 * wrong (false).
	 */
	protected boolean _answerOptionIsRightArray[] = new boolean[]{ false, false, false, false};
	
	
	/**
	 * Construct a new question; it must be already known if exactly one answer is true.
	 * 
	 * @param questionType  Must be either {@link QuestionTypeEnum#MULTIPLE_CHOICE} or {@link QuestionTypeEnum#SINGLE_CHOICE};
	 *                      will raise an exception for every other question type.
	 * 
	 * @param question  Question text
	 * 
	 * @throws KahootException  Illegal type of question specified.
	 */
	public MultipleSingleChoiceQuestion(QuestionTypeEnum questionType, String question) throws KahootException {
		
		super(questionType, question);
		
		if (questionType != QuestionTypeEnum.MULTIPLE_CHOICE && questionType != QuestionTypeEnum.SINGLE_CHOICE) {
			
			throw new KahootException("Illegal question type " + questionType);
		}
	}
	
	
	/**
	 * Add answer option and specify if this answer option is true.
	 * 
	 * @param answerText  Text of answer option that was displayed to the user.
	 * 
	 * @param isRight  {@code true} iff this answer option is a correct one.
	 * 
	 * @throws KahootException  Will be raised if more than four answer texts are added, or 
	 *                          if for a single-choice question more than one answer option
	 *                          is true.
	 */
	public void addAnswerOption(String answerText, boolean isRight) throws KahootException {
		
		if (_numberOfAnswerOptions >= 4) {
			
			throw new KahootException("Attempt to add more than four answer options to question.");
		}

		if (isRight && getIsSingleChoiceQuestion() && _numberOfRightAnswerOptions > 0) {
			
			throw new KahootException("Added more than one correct answer option for single-choice question.");
		}
		
		
		_answerOptionArray       [_numberOfAnswerOptions] = answerText;
		_answerOptionIsRightArray[_numberOfAnswerOptions] = isRight;
		
		
		_numberOfAnswerOptions++;		
		if (isRight) { _numberOfRightAnswerOptions++; }		 				
	}
	
	
	/**
	 * Getter for number of answer options which are contained by the this object.
	 * 
	 * @return  Number of answer options (0-4) that was already added to this object.
	 */
	public int getNumberOfAnswerQuestions() {
		
		return _numberOfAnswerOptions;
	}
	
	
	/**
	 * Getter for number of right answer options.
	 * 
	 * @return  Number of answer options that are right; value will not be greater than 1
	 *          for single choice questions.
	 */
	public int getNumberOfRightAnswerOtpions() {
		
		return _numberOfRightAnswerOptions;
	}
	
	
	/**
	 * Getter for a single answer option text.
	 * 
	 * @param numberOfAnswerOption  1-4, must not exceed value returned by {@link #getNumberOfAnswerQuestions()};
	 *                              will raise exception for wrong number!
	 * 
	 * @return  Object containing the answer option text and if it was a right (true) or wrong (false) answer option.
	 * 
	 * @throws KahootException  Attempt to obtain answer option with illegal number.
	 */
	public AnswerOption getAnswerOptionText(int numberOfAnswerOption) throws KahootException {
		
		if (numberOfAnswerOption < 1) {
			
			throw new KahootException("Attempt to obtain answer option with too low number " + numberOfAnswerOption + ".");
		}
		
		
		if (numberOfAnswerOption > getNumberOfAnswerQuestions()) {
			
			throw new KahootException("Attempt to obtain answer option with too high number " + numberOfAnswerOption + ".");
		}
		
		int indexOfAnswerOption = numberOfAnswerOption - 1;						
		
		String answerOptionText     = _answerOptionArray    [indexOfAnswerOption];
		boolean answerOptionIsRight = _answerOptionIsRightArray[indexOfAnswerOption];
		
		return new AnswerOption( answerOptionText, answerOptionIsRight);		
	}
	
}
