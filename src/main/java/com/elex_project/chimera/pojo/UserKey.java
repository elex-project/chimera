/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2019. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.pojo;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

/**
 * Thingplug API Key
 * @author Elex
 */
@Slf4j
public final class UserKey {

	private final String uKey;

	/**
	 * @param uKey Base64 encoded user key
	 */
	public UserKey(final String uKey) {
		this.uKey = uKey;
	}

	/**
	 * @param uKey Base64 decoded user key.
	 */
	public UserKey(final byte[] uKey) {
		this.uKey = Base64.getEncoder().encodeToString(uKey);
		//this.uKey = ByteUtils.encodeToBase64(uKey);
	}

	@NotNull
	@Contract(value = "_ -> new", pure = true)
	public static UserKey of(final String uKey) {
		return new UserKey(uKey);
	}

	@NotNull
	@Contract("_ -> new")
	public static UserKey of(final byte[] uKey) {
		return new UserKey(uKey);
	}

	@Override
	public String toString() {
		return uKey;
	}

	@Override
	public int hashCode() {
		return uKey.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof UserKey) {
			return uKey.equals(((UserKey) obj).uKey);
		} else {
			return super.equals(obj);
		}
	}
}
