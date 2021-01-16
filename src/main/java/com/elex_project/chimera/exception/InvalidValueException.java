/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.exception;

public class InvalidValueException extends Exception {
	public InvalidValueException() {
		super("Invalid Value");
	}

	public InvalidValueException(String desc) {
		super("Invalid Value: " + desc);
	}
}
