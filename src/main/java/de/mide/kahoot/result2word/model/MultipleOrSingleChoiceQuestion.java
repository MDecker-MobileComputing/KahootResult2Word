package de.mide.kahoot.result2word.model;

import de.mide.kahoot.result2word.utils.KahootException;

import static de.mide.kahoot.result2word.utils.StringUtils.stringArray2String;


/**
 * Class for single-choice or multiple choice questions; both type of questions can contain up to
 * four answer options, but for single-choice questions exactly one of these answers is true.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class MultipleOrSingleChoiceQuestion extends AbstractQuestion {

	/** Counter for number of answer options that were supplied; must not be greater than four. */
	protected int _numberOfAnswerOptions = 0;

	/**
	 * Counter for number of right answer options that were supplied; must not be greater than one
	 * for questions of type {@link QuestionTypeEnum#SINGLE_CHOICE}.
	 */
	protected int _numberOfRightAnswerOptions = 0;

	/** Counter for number of wrong answer options that were supplied. */
	protected int _numberOfWrongAnswerOptions = 0;

	/** Array for holding up to four answer option texts; is initialized with four empty strings. */
	protected String _answerOptionArray[] = new String[]{ "", "", "", ""};

	/**
	 * Array for holding flags saying whether an answer option was right (true) or wrong (false),
	 * must correspond to answer option text in {@link #_answerOptionArray}; default value for all four
	 * elements is {@link AnswerStatusEnum#UNKNOWN}.
	 */
	protected AnswerStatusEnum[] _answerOptionIsRightArray = { AnswerStatusEnum.UNKNOWN, AnswerStatusEnum.UNKNOWN, AnswerStatusEnum.UNKNOWN, AnswerStatusEnum.UNKNOWN };


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
	public MultipleOrSingleChoiceQuestion(QuestionTypeEnum questionType, String question) throws KahootException {

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

		if (isRight && isSingleChoiceQuestion() && _numberOfRightAnswerOptions > 0) {

			throw new KahootException("Added more than one correct answer option for single-choice question.");
		}


		_answerOptionArray[_numberOfAnswerOptions] = answerText;

		if (isRight) {

			_answerOptionIsRightArray[_numberOfAnswerOptions] = AnswerStatusEnum.RIGHT;

		} else {

			_answerOptionIsRightArray[_numberOfAnswerOptions] = AnswerStatusEnum.WRONG;
		}


		_numberOfAnswerOptions++;
		if (isRight) {

			_numberOfRightAnswerOptions++;

		} else {

			_numberOfWrongAnswerOptions++;
		}
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
	public int getNumberOfRightAnswerOptions() {

		return _numberOfRightAnswerOptions;
	}


	/**
	 * Getter for number of wrong answer options.
	 *
	 * @return  Number of answer options that are wrong.
	 */
	public int getNumberOfWrongAnswerOptions() {

		return _numberOfWrongAnswerOptions;
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

		String  answerOptionText    = _answerOptionArray       [indexOfAnswerOption];
		boolean answerOptionIsRight = _answerOptionIsRightArray[indexOfAnswerOption] == AnswerStatusEnum.RIGHT ? true : false;

		return new AnswerOption( answerOptionText, answerOptionIsRight);
	}



	/**
	 * Getter for array with texts of all correct answer option(s).
	 *
	 * @return  String array with all correct answer options (at least one element).
	 */
	public String[] getTrueAnswerOptions() {

		String[] resultString = new String[ getNumberOfRightAnswerOptions() ];

		int counter = 0;
		for (int i = 0; i < _answerOptionIsRightArray.length; i++ ) {

			if (_answerOptionIsRightArray[i] == AnswerStatusEnum.RIGHT) {

				resultString[counter] = _answerOptionArray[i];
			    counter++;
			}
		}

		return resultString;
	}


	/**
	 * Getter for array with texts of all incorrect answer option(s).
	 *
	 * @return  String array with all incorrect answer options (at least one element).
	 */
	public String[] getWrongAnswerOptions() {

		String[] resultString = new String[ getNumberOfWrongAnswerOptions() ];

		int counter = 0;
		for (int i = 0; i < _answerOptionIsRightArray.length; i++ ) {

			if (_answerOptionIsRightArray[i] == AnswerStatusEnum.WRONG) {

				resultString[counter] = _answerOptionArray[i];
			    counter++;
			}
		}

		return resultString;
	}


	/**
	 * Method to obtain string with short summary on question.
	 * <br><br>
	 *
	 * Example 1:
	 * <q>Single-choice question with question text "Which is NOT a country in Europe?"; Right answer: "Brasil"; Wrong answers: "Spain", "France", "Switzerland". 100,0% of players gave the correct answer.</q>
	 * <br><br>
	 *
	 * Example 2:
	 * <q>Multiple-choice question with question text "Which are major cities in the UK?"; Right answers: "Manchester", "Brighton"; Wrong answers: "Berlin", "Paris". 100,0% of players gave the correct answer.</q>
	 *
	 * @return  String with type of question and question text.
	 */
	@Override
	public String toString() {

		String questionType = super.toString();

		String[] trueAnswerOptions  = getTrueAnswerOptions();
		String[] falseAnswerOptions = getWrongAnswerOptions();

		String rightOptionsString = stringArray2String( trueAnswerOptions );
		String wrongOptionsString = stringArray2String( falseAnswerOptions );

		String answer1SingularOrPlural = trueAnswerOptions.length  == 1 ? "answer" : "answers";
		String answer2SingularOrPlural = falseAnswerOptions.length == 1 ? "answer" : "answers";

		return String.format( "%s with question text \"%s\"; Right %s: %s; Wrong %s: %s. %s.",
				              questionType, getQuestionText(),
				              answer1SingularOrPlural, rightOptionsString,
				              answer2SingularOrPlural, wrongOptionsString,
				              getPercentageAnswersRightAsString()
				            );
	}

}
