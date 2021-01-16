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
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;

import static com.elex_project.chimera.Utils.Random;

/**
 * enc, nct, nu를 변경할 경우에 사용.
 * 구독 이름을 변경하는 것은 안됨.
 *
 * @author Elex
 */
@Slf4j
public final class UpdateSubscriptionRequest extends BasePutRequestBuilder<UpdateSubscriptionResponse> {

	public UpdateSubscriptionRequest(Subscription oldSub, @NotNull Subscription newSub,
	                                 Ltid ltid, @NotNull UserKey userKey) {
		super();

		uri(path(ltid, oldSub))
				.header("Accept", "application/xml")
				.header("X-M2M-RI", ltid.toString() + "_" + Random.nextInt(9999))
				.header("X-M2M-Origin", ltid.toString())
				.header("X-M2M-NM", oldSub.getName())
				//setRequestHeaderProperty("Content-Type", "application/vnd.onem2m-res+xml;ty=23");
				.header("uKey", userKey.toString());
		//setUserAgent(USER_AGENT);

		body(newSub.getContentBody())
				.header("Content-Type", Subscription.CONTENT_TYPE);
	}

	@NotNull
	private static String path(@NotNull Ltid ltid, @NotNull Subscription oldSub) {
		return serverHost() + "/" + ltid.getAppEui() + "/v1_0/remoteCSE-" + ltid.toString()
				+ "/container-LoRa/subscription-" + oldSub.getName();
	}

	@NotNull
	@Contract("_ -> new")
	@Override
	public UpdateSubscriptionResponse conv(HttpResponse<String> response) {
		return new UpdateSubscriptionResponse(response);
	}
}
