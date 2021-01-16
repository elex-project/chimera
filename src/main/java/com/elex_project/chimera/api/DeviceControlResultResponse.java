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
public final class DeviceControlResultResponse extends BaseResponse {
	public static final int STATUS_OK = 200;
	public static final int STATUS_UNREGISTERED_MgmtCmd = 404;

	private ExecInstance execInstance;

	DeviceControlResultResponse(@NotNull HttpResponse<String> response) {
		super(response);
		if (null != xml) {
			try {
				Element root = xml.getDocumentElement();
				execInstance = new ExecInstance(root);
			} catch (InvalidXMLFormatException e) {
				log.error("Invalid XML format.", e);
			}
		}
	}

	@JsonProperty("resId")
	public String getResId() {
		return execInstance.getResId();
	}

	@JsonProperty("createdTime")
	public LocalDateTime getCreatedTime() {
		return execInstance.getCreatedTime();
	}

	@JsonProperty("lastModifiedTime")
	public LocalDateTime getLastModifiedTime() {
		return execInstance.getLastModifiedTime();
	}

	@JsonProperty("expirationTime")
	public LocalDateTime getExpirationTime() {
		return execInstance.getExpirationTime();
	}

	@JsonProperty("execStatus")
	public int getExecStatus() {
		return execInstance.getExs();
	}

	@JsonProperty("execResult")
	public int getExecResult() {
		return execInstance.getExr();
	}

	@JsonProperty("ext")
	public String getExt() {
		return execInstance.getExt();
	}

	@JsonProperty("extra")
	public String getExtra() {
		return execInstance.getExtra();
	}

}
