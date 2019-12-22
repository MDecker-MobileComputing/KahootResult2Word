package de.mide.kahoot.result2word.model;

import java.util.ArrayList;
import de.mide.kahoot.result2word.utils.KahootException;


/**
 * This class contains all questions extracted from an Kahoot Excel file in the order
 * of the sheets in the Excel file.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class QuestionList {

	/** List containing all the questions. */
	protected ArrayList<AbstractQuestion> _questionList = null;


	/**
	 * Creates a list object with a particular number of elements; if number of elements
	 * is exceeded during runtime, then list will be automatically enlarged, but this will
	 * cost a bit of runtime.
	 *
	 * @param numQuestions  Initial number of list entries.
	 */
	public QuestionList(int numQuestions) {

		_questionList = new ArrayList<AbstractQuestion>(numQuestions);
	}


	/**
	 * Default constructor, initial number of list entries will be 20.
	 */
	public QuestionList() {

		this(20);
	}


	/**
	 * Add new question to list. All types of questions are supported, because
	 * the type of argument {@code question} is the abstract superclass of all
	 * questions.
	 *
	 * @param question  Question to be added.
	 */
    public void addQuestion(AbstractQuestion question) {

    	_questionList.add(question);
    }


    /**
     * Getter for number of questions.
     *
     * @return  Number of questions currently stored in this object; might be zero.
     */
    public int getNumberOfQuestions() {

    	return _questionList.size();
    }


    /**
     * Getter for type of question at particular index position.
     *
     * @param index  0-based index of question int this list, must not be equals or greater
     *               than value returned by method {@link #getNumberOfQuestions()}.
     *
     * @return  Enum element describing type of question, e.g. {@link QuestionTypeEnum#SINGLE_CHOICE}
     *          or {@link QuestionTypeEnum#TRUE_OR_FALSE}.
     *
     * @throws KahootException Illegal value for argument {@code index}.
     */
    public QuestionTypeEnum getTypeOfQuestion(int index) throws KahootException {

    	if (index < 0 || index >= getNumberOfQuestions()) {

    		throw new KahootException("Attempt to receive type of question with illegal index " + index + ".");
    	}

    	AbstractQuestion question = _questionList.get(index);

    	return question.getQuestionType();
    }


    /**
     * Getter for question object of type {@link QuestionTypeEnum#TRUE_OR_FALSE} at position {@code index}
     * in this list.
     *
     * @param index  0-based index of question int this list, must not be equals or greater
     *               than value returned by method {@link #getNumberOfQuestions()}.
     *
     * @return  Question object with a true/false question.
     *
     * @throws KahootException  Illegal value for argument {@code index} or question at position
     *                          {@code index} is not a question of type  {@link QuestionTypeEnum#TRUE_OR_FALSE}.
     */
    public TrueFalseQuestion getTrueOrFalseQuestion(int index) throws KahootException {

    	if (index < 0 || index >= getNumberOfQuestions()) {

    		throw new KahootException("Attempt to receive true/false question at illegal index " + index + ".");
    	}

    	if ( getTypeOfQuestion(index) != QuestionTypeEnum.TRUE_OR_FALSE) {

    		throw new KahootException("Attempt to receive question at index " + index +
    				                  " as TrueFalseQuestion, but question has another type " + getTypeOfQuestion(index) + ".");
    	}

    	AbstractQuestion question = _questionList.get(index);

    	return (TrueFalseQuestion) question;
    }


    /**
     * Getter for question object of type {@link QuestionTypeEnum#SINGLE_CHOICE} or {@link QuestionTypeEnum#MULTIPLE_CHOICE}
     * at position {@code index} in this list.
     *
     * @param index  0-based index of question int this list, must not be equals or greater
     *               than value returned by method {@link #getNumberOfQuestions()}.
     *
     * @return  Question object with a single-choice or multiple-choice question.
     *
     * @throws KahootException  Illegal value for argument {@code index} or question at position
     *                          {@code index} is not a question of type  {@link QuestionTypeEnum#TRUE_OR_FALSE}.
     */
    public MultipleSingleChoiceQuestion getSingleChoiceQuestion(int index) throws KahootException {

    	if (index < 0 || index >= getNumberOfQuestions()) {

    		throw new KahootException("Attempt to receive single-choice question at illegal index " + index + ".");
    	}

    	QuestionTypeEnum questionType = getTypeOfQuestion(index);
    	if (questionType != QuestionTypeEnum.SINGLE_CHOICE && questionType != QuestionTypeEnum.MULTIPLE_CHOICE) {

    		throw new KahootException("Attempt to receive question at index " + index +
    				                  " as MultipleSingleChoiceQuestion, but question has another type " + questionType + ".");
    	}

    	AbstractQuestion question = _questionList.get(index);

    	return (MultipleSingleChoiceQuestion) question;
    }

}
