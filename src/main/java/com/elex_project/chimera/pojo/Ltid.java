/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2019. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.pojo;

import com.elex_project.chimera.exception.InvalidValueException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * AppEui와 DevEui의 조합
 *
 * @author Elex
 */
public final class Ltid {
	private final AppEui appEui;
	private final DeviceEui deviceEui;
	private final String ltid;

	/**
	 * AppEui와 DevEui를 조합해서 LTID를 생성한다.
	 *
	 * @param appEui
	 * @param deviceEui
	 */
	public Ltid(final @NotNull AppEui appEui, final @NotNull DeviceEui deviceEui) {
		this.appEui = appEui;
		this.deviceEui = deviceEui;
		this.ltid = appEui.toString().substring(8, 16) + deviceEui.toString();
	}

	/**
	 * LTID는 AppEui의 일부만 포함되어 있으므로, LTID로부터 App Eui를 복원할 수 없다.
	 * 웹에서 받은 데이터를 재활용해야하는 경우에만 제한적으로 사용할 것.
	 * 이 경우, AppEui는 null이다.
	 *
	 * @param ltid
	 * @throws InvalidValueException
	 */
	public Ltid(final @NotNull String ltid) throws InvalidValueException {
		this.appEui = null;
		this.deviceEui = new DeviceEui(ltid.substring(8));
		this.ltid = ltid;
	}

	@NotNull
	@Contract("_, _ -> new")
	public static Ltid of(final String appEui, final String devEui) throws InvalidValueException {
		return new Ltid(AppEui.of(appEui), DeviceEui.of(devEui));
	}

	@NotNull
	@Contract("_, _ -> new")
	public static Ltid of(final byte[] appEui, final byte[] devEui) throws InvalidValueException {
		return new Ltid(AppEui.of(appEui), DeviceEui.of(devEui));
	}

	//@JSONPropertyName("ltid")
	@Override
	public String toString() {
		return ltid;
	}

	/**
	 * 문자열 기반의 생성자를 사용하는 경우, null을 반환할 수 있다.
	 *
	 * @return
	 */
	@JsonIgnore
	@Nullable
	public AppEui getAppEui() {
		return appEui;
	}

	@JsonProperty("appEui")
	public String getAppEuiAsString() {
		return appEui.toString();
	}

	@JsonIgnore
	public DeviceEui getDeviceEui() {
		return deviceEui;
	}

	@JsonProperty("devEui")
	public String getDeviceEuiAsString() {
		return deviceEui.toString();
	}

	@Override
	public int hashCode() {
		return ltid.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Ltid) {
			return ltid.equalsIgnoreCase(((Ltid) obj).ltid);
		} else {
			return super.equals(obj);
		}
	}
}
