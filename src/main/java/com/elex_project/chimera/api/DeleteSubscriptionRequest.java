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

import java.io.IOException;
import java.net.http.HttpResponse;

import static com.elex_project.chimera.Utils.Random;

/**
 * @author Elex
 */
@Slf4j
public final class DeleteSubscriptionRequest
		extends BaseDeleteRequestBuilder<DeleteSubscriptionResponse> {
	/**
	 * 구독 삭제
	 *
	 * @param subscription
	 * @param ltid
	 * @param userKey
	 * @throws IOException
	 */
	public DeleteSubscriptionRequest(@NotNull Subscription subscription, @NotNull Ltid ltid, @NotNull UserKey userKey) {
		super();

		uri(path(ltid, subscription))

				.header("Accept", "application/xml")
				.header("X-M2M-RI", ltid.toString() + "_" + Random.nextInt(9999))
				.header("X-M2M-Origin", ltid.toString())
				//header("Content-Type", "application/vnd.onem2m-res+xml;ty=23");
				.header("uKey", userKey.toString());
	}

	@NotNull
	private static String path(@NotNull Ltid ltid, @NotNull Subscription subscription) {
		return serverHost() + "/" + ltid.getAppEui() + "/v1_0/remoteCSE-" + ltid.toString() + "/container-LoRa/subscription-" + subscription.getName();
	}

	@NotNull
	@Contract(pure = true)
	@Override
	public DeleteSubscriptionResponse conv(HttpResponse<String> response) {
		return new DeleteSubscriptionResponse(response);
	}
}
