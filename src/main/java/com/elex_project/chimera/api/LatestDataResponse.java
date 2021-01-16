/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;


import com.elex_project.chimera.pojo.UplinkData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;

/**
 * @author Elex
 */
@ToString
public final class LatestDataResponse extends BaseResponse {
	public static final int STATUS_OK = 200;
	public static final int STATUS_UNREGISTERED_DEVICE = 404;

	private final UplinkData data;

	LatestDataResponse(HttpResponse<String> response) {
		super(response);
		data = new UplinkData(xml);
	}

	@JsonProperty("createdTime")
	public LocalDateTime getCreatedTime() {
		return data.getCreatedTime();
	}

	@JsonProperty("lastModifiedTime")
	public LocalDateTime getLastModifiedTime() {
		return data.getLastModifiedTime();
	}

	@JsonProperty("expirationTime")
	public LocalDateTime getExpirationTime() {
		return data.getExpirationTime();
	}

	@JsonProperty("creator")
	public String getCreator() {
		return data.getCreator();
	}

	@JsonProperty("content")
	public String getContent() {
		return data.getContent();
	}

	@JsonProperty("contentSize")
	public int getContentSize() {
		return data.getContentSize();
	}

}
