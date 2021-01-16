/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2019. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.pojo;

import com.elex_project.chimera.Utils;
import com.elex_project.chimera.exception.InvalidValueException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * App Eui
 * @author Elex
 */
public final class AppEui {

	private final String appEui;

	/**
	 * @param appEui 16자리
	 * @throws InvalidValueException
	 */
	public AppEui(final @NotNull String appEui) throws InvalidValueException {
		if (appEui.length() == 16) {
			this.appEui = appEui.toLowerCase();
		} else {
			throw new InvalidValueException("App EUI = " + appEui);
		}
	}

	public AppEui(final @NotNull byte[] appEui) throws InvalidValueException {
		this(Utils.byteArrayToHex("", appEui));
	}

	@NotNull
	@Contract("_ -> new")
	public static AppEui of(final String appEui) throws InvalidValueException {
		return new AppEui(appEui);
	}

	@NotNull
	@Contract("_ -> new")
	public static AppEui of(final byte[] appEui) throws InvalidValueException {
		return new AppEui(appEui);
	}

	@Override
	public String toString() {
		return appEui;
	}

	@Override
	public int hashCode() {
		return appEui.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof AppEui) {
			return appEui.equalsIgnoreCase(((AppEui) obj).appEui);
		} else {
			return super.equals(obj);
		}
	}
}
