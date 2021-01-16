/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.pojo;

import com.elex_project.chimera.api.DeviceListResponse;
import com.elex_project.chimera.exception.InvalidValueException;
import com.elex_project.chimera.exception.InvalidXMLFormatException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static com.elex_project.chimera.Utils.*;

/**
 * @author Elex
 * @see DeviceListResponse
 */
@Slf4j
@Getter
public final class Device {
	@JsonProperty("ltid")
	private final Ltid deviceId; // device_Id
	@JsonProperty("status")
	private final String status; // status
	@JsonProperty("isFault")
	private final boolean isFault;
	@JsonProperty("isAlive")
	private final boolean isAlive; // fault_Yn, alive_Yn
	@JsonProperty("ownerId")
	private final String ownerId; // owner_Id
	@JsonProperty("registeredDate")
	private LocalDate registeredDate; // regi_Date

	public Device(final @NotNull Element deviceElement) throws InvalidXMLFormatException, InvalidValueException {
		Element el = (Element) deviceElement.getElementsByTagName("device_Id").item(0);
		if (null == el) throw new InvalidXMLFormatException("No 'device_Id' tag.");
		this.deviceId = new Ltid(el.getTextContent());

		el = (Element) deviceElement.getElementsByTagName("status").item(0);
		if (null == el) {
			this.status = EMPTY_STRING;
		} else {
			this.status = el.getTextContent();
		}

		el = (Element) deviceElement.getElementsByTagName("fault_Yn").item(0);
		if (null == el) {
			this.isFault = el.getTextContent().equalsIgnoreCase("Y");
		} else {
			this.isFault = false;
		}

		el = (Element) deviceElement.getElementsByTagName("alive_Yn").item(0);
		if (null == el) {
			this.isAlive = el.getTextContent().equalsIgnoreCase("Y");
		} else {
			this.isAlive = false;
		}

		el = (Element) deviceElement.getElementsByTagName("owner_Id").item(0);
		if (null == el) {
			this.ownerId = EMPTY_STRING;
		} else {
			this.ownerId = el.getTextContent();
		}

		el = (Element) deviceElement.getElementsByTagName("regi_Date").item(0);
		if (null == el) throw new InvalidXMLFormatException("No 'regi_Date' tag.");
		try {
			this.registeredDate = toLocalDate(el.getTextContent());
		} catch (DateTimeParseException e) {
			this.registeredDate = DEFAULT_DATETIME.toLocalDate();
			throw new InvalidValueException("Date? " + el.getTextContent());
		}
	}

	@Override
	public String toString() {
		return deviceId.getDeviceEui().toString();
	}
}
