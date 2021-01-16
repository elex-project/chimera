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
public final class CreateSubscriptionRequest
		extends BasePostRequestBuilder<CreateSubscriptionResponse> {

	/**
	 * 구독 생성
	 * <p>
	 * LoRa 디바이스의 Uplink 데이터가 생성되면 사용자의 App/Web 서버로 Push 해주도록 설정합니다.
	 *
	 * <pre>
	 * ukey: 사용자 인증키
	 * X-M2M-NM: Subscription을 구분하기 위한 구분자, 예제에서는 임의의 값 “subscription_1” 사용
	 * <rss>: 1(데이터 생성시 Push 조건)
	 * <nu>: Uplink 데이터를 수신할 서버의 URL
	 * </pre>
	 * <p>
	 * # Request
	 * <pre><code>
	 * POST /0000000000000004/v1_0/remoteCSE-00000004702c1ffffe1d79da/container-LoRa HTTP/1.1
	 * Host: thingplugtest.sktiot.com:9443
	 * X-M2M-RI: 00000004702c1ffffe1d79da_00012
	 * X-M2M-Origin: 00000004702c1ffffe1d79da
	 * uKey: dG5janFndjNibzZ**************ERjd296V0poMFY0TTJ4UQ==
	 * X-M2M-NM: test_subscription
	 * Content-Type: application/vnd.onem2m-res+xml;ty=23
	 *
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <m2m:sub
	 *     xmlns:m2m="http://www.onem2m.org/xml/protocols"
	 *     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	 *     <enc>
	 *          <rss>1</rss>
	 *     </enc>
	 *     <nu>HTTP|http://234.1.2.3:5000/data</nu>
	 *     <nct>2</nct>
	 * </m2m:sub>
	 * </code></pre>
	 * <p>
	 * # Response
	 * <pre><code>
	 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	 * <m2m:sub xmlns:m2m="http://www.onem2m.org/xml/protocols" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	 *     <ty>23</ty>
	 *     <ri>SS00000000000000008085</ri>
	 *     <rn>test_subscription</rn>
	 *     <pi>CT00000000000000006585</pi>
	 *     <ct>2017-12-14T15:26:56+09:00</ct>
	 *     <lt>2017-12-14T15:26:56+09:00</lt>
	 *     <enc>
	 *         <rss>1</rss>
	 *     </enc>
	 *     <nu>HTTP|http://234.1.2.3:5000/data</nu>
	 *     <nct>2</nct>
	 * </m2m:sub>
	 * </code></pre>
	 * <p>
	 * # Subscrption 이후 LoRa 디바이스가 Uplink를 하면 ThingPlug는 사용자의 App/Web 서버로 아래와 같은 포맷으로 데이터를 Push해 줍니다.
	 *
	 * @param subscription
	 * @param ltid
	 * @param userKey
	 * @throws IOException
	 */
	public CreateSubscriptionRequest(@NotNull Subscription subscription, @NotNull Ltid ltid, @NotNull UserKey userKey) {
		super();
		uri(path(ltid))
				.header("Accept", "application/xml")
				.header("X-M2M-RI", ltid.toString() + "_" + Random.nextInt(9999))
				.header("X-M2M-Origin", ltid.toString())
				.header("X-M2M-NM", subscription.getName())
				.header("uKey", userKey.toString());
		body(subscription.getContentBody())
				.header("Content-Type", Subscription.CONTENT_TYPE);
	}

	@NotNull
	private static String path(@NotNull Ltid ltid) {
		return serverHost() + "/" + ltid.getAppEui() + "/v1_0/remoteCSE-" + ltid.toString() + "/container-LoRa";
	}

	@NotNull
	@Contract("_ -> new")
	@Override
	public CreateSubscriptionResponse conv(HttpResponse<String> response) {
		return new CreateSubscriptionResponse(response);
	}
}
