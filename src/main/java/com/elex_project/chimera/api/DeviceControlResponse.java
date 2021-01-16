/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import com.elex_project.chimera.exception.InvalidXMLFormatException;
import com.elex_project.chimera.pojo.ExecInstance;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;

/**
 * @author Elex
 */
@Slf4j
@ToString
public final class DeviceControlResponse extends BaseResponse {
	public static final int STATUS_OK = 200;
	public static final int STATUS_UNREGISTERED_MgmtCmd = 404;

	private ExecInstance execInstance;

	DeviceControlResponse(@NotNull HttpResponse<String> response) {
		super(response);

		if (null != xml) {
			try {
				Element el = (Element) xml.getDocumentElement()
						.getElementsByTagName("m2m:exin").item(0);
				if (null == el) throw new InvalidXMLFormatException("No 'm2m:exin' tag.");
				execInstance = new ExecInstance(el);
			} catch (InvalidXMLFormatException e) {
				log.error("Invalid XML format.", e);
			}
		}
	}

	@JsonIgnore
	public ExecInstance getExecInstance() {
		return execInstance;
	}

	@JsonProperty("resId")
	public String getResId() {
		return execInstance.getResId();
	}

	@JsonProperty("createdTime")
	public LocalDateTime getCreatedTime() {
		return execInstance.getCreatedTime();
	}

	@JsonProperty("extra")
	public String getExtra() {
		return execInstance.getExtra();
	}

	@JsonProperty("execStatus")
	public int getExecStatus() {
		return execInstance.getExs();
	}

}
