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
 * DevEui
 * @author Elex
 */
public final class DeviceEui {
	private final String deviceEui;

	/**
	 * @param devEui 16자리
	 * @throws InvalidValueException
	 */
	public DeviceEui(final @NotNull String devEui) throws InvalidValueException {
		if (devEui.length() == 16) {
			this.deviceEui = devEui.toLowerCase();
		} else {
			throw new InvalidValueException("Device EUI = " + devEui);
		}
	}

	public DeviceEui(final @NotNull byte[] devEui) throws InvalidValueException {
		this(Utils.byteArrayToHex("", devEui));
	}

	@NotNull
	@Contract("_ -> new")
	public static DeviceEui of(final String devEui) throws InvalidValueException {
		return new DeviceEui(devEui);
	}

	@NotNull
	@Contract("_ -> new")
	public static DeviceEui of(final byte[] devEui) throws InvalidValueException {
		return new DeviceEui(devEui);
	}

	@Override
	public String toString() {
		return deviceEui;
	}

	@Override
	public int hashCode() {
		return deviceEui.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof DeviceEui) {
			return deviceEui.equalsIgnoreCase(((DeviceEui) obj).deviceEui);
		} else {
			return super.equals(obj);
		}
	}
}
