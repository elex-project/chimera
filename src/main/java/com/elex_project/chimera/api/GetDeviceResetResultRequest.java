/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import com.elex_project.chimera.pojo.Ltid;
import com.elex_project.chimera.pojo.UserKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;

import static com.elex_project.chimera.Utils.Random;

/**
 * @author Elex
 */
public final class GetDeviceResetResultRequest extends BaseGetRequestBuilder<DeviceResetResultResponse> {

	public GetDeviceResetResultRequest(Ltid ltid, String resId, @NotNull UserKey userKey) {
		super();
		uri(path(ltid, resId))
				//setRequestHeaderProperty("Accept", "application/xml");
				.header("X-M2M-RI", ltid.toString() + "_" + Random.nextInt(9999))
				.header("X-M2M-Origin", ltid.toString())
				//setRequestHeaderProperty("Content-Type", "application/vnd.onem2m-res+xml;ty=23");
				.header("uKey", userKey.toString());
		//setUserAgent(USER_AGENT);
	}

	@NotNull
	private static String path(@NotNull Ltid ltid, String resId) {
		return serverHost() + "/" + ltid.getAppEui() + "/v1_0/mgmtCmd-"
				+ ltid.toString() + "_DevReset/execInstance-" + resId;
	}

	@NotNull
	@Contract("_ -> new")
	@Override
	public DeviceResetResultResponse conv(HttpResponse<String> response) {
		return new DeviceResetResultResponse(response);
	}
}
