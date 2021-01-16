/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import com.elex_project.chimera.pojo.Ltid;
import com.elex_project.chimera.pojo.Subscription;
import com.elex_project.chimera.pojo.UserKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;

import static com.elex_project.chimera.Utils.Random;

/**
 * @author Elex
 */
public final class RetrieveSubscriptionRequest
		extends BaseGetRequestBuilder<RetrieveSubscriptionResponse> {

	public RetrieveSubscriptionRequest(Subscription subscription, Ltid ltid, @NotNull UserKey userKey) {
		super();
		uri(path(ltid, subscription))
				.header("Accept", "application/xml")
				.header("X-M2M-RI", ltid.toString() + "_" + Random.nextInt(9999))
				.header("X-M2M-Origin", ltid.toString())
				.header("uKey", userKey.toString());
		//setUserAgent(USER_AGENT);
	}

	@NotNull
	private static String path(@NotNull Ltid ltid, @NotNull Subscription subscription) {
		return serverHost() + "/" + ltid.getAppEui() + "/v1_0/remoteCSE-"
				+ ltid.toString() + "/container-LoRa/subscription-" + subscription.getName();
	}

	@NotNull
	@Contract("_ -> new")
	@Override
	public RetrieveSubscriptionResponse conv(HttpResponse<String> response) {
		return new RetrieveSubscriptionResponse(response);
	}
}
