package de.mide.kahoot.result2word.model;

/**
 * Enumeration type for status of an answer option: right, wrong or unknown.
 * Value {@link #UNKNOWN} is needed because an object of class 
 * {@link MultipleOrSingleChoiceQuestion} might contain less than four 
 * answer options.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public enum AnswerStatusEnum {

	/** Answer option is true. */
	RIGHT,
	
	/** Answer option is wrong. */
	WRONG,
	
	/** 
	 * Answer option is neutral because no answer option for this index, 
	 * e.g. if single/multiple-choice question has only two or three answer options. 
	 */ 
	UNKNOWN;	
}
