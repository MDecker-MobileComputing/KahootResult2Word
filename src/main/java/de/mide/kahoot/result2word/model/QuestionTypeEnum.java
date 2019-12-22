package de.mide.kahoot.result2word.model;


/**
 * Enumeration type for supported question types.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public enum QuestionTypeEnum {

	/** Quiz question with up to four answer options, with exactly one correct answer. */
	SINGLE_CHOICE,

	/** Quiz question with up to four answer options, with two or three correction answers. */
	MULTIPLE_CHOICE,

	/** Question which contains a statement which is either right (true) or wrong (false). */
	TRUE_OR_FALSE;

}
