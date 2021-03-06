package de.mide.kahoot.result2word.model;


/**
 * Concrete class for a true/false question, i.e. a question with a statement that is either
 * right or wrong.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class TrueFalseQuestion extends AbstractQuestion {

	/** {@code true} iff statement of this question is true. */
	protected boolean _statementIsTrue = false;


	/**
	 * Create new question of type true/false.
	 *
	 * @param statement  Question, which is for this question type a statement that
	 *                   is either true of false.
	 *
	 * @param isTrue  {@code true} iff the statement is true.
	 */
	public TrueFalseQuestion(String statement, boolean isTrue) {

		super(QuestionTypeEnum.TRUE_OR_FALSE, statement);

		_statementIsTrue = isTrue;
	}


	/**
	 * Getter for flag saying wether statement of this question is true or not.
	 *
	 * @return  {@code true} iff this question contains a true statement.
	 */
	public boolean isStatementTrue() {

		return _statementIsTrue;
	}


	/**
	 * Method to obtain string with short summary on question.
	 * <br><br>
	 *
	 * Example:
	 * <i>True/false question, statement "Beijing is the capital city of PR China.", isTrue=true. 100,0% of players gave the correct answer.</i>
	 *
	 * @return  Details of true/false question contained by this object, e.g. for writing to logger.
	 */
	@Override
	public String toString() {

		return String.format( "True/false question, statement \"%s\", isTrue=%b. %s.",
				              getQuestionText(),
				              _statementIsTrue,
				              getPercentageAnswersRightAsString()
				            );
	}

}
