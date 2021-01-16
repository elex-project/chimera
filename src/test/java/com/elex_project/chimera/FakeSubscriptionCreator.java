/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera;

import com.elex_project.chimera.pojo.Ltid;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;

import static com.elex_project.chimera.Utils.OFFSET_DATETIME_FORMAT;

/**
 * 가짜 로라 구독 데이터 시뮬레이터
 */
public final class FakeSubscriptionCreator {
	private static final String USER_AGENT = "Fake Subscription";
	private static final String SUBS_NAME = "fake-subscription";
	protected final HttpRequest.Builder builder;
	private final HttpClient httpClient;

	public FakeSubscriptionCreator(URI uri) {

		this.httpClient = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(10)).build();

		this.builder = HttpRequest.newBuilder()
				.version(HttpClient.Version.HTTP_1_1)
				.timeout(Duration.ofSeconds(10))
				.uri(uri)
				.header("User-Agent", USER_AGENT)
				.header("X-M2M-RI", "13868a1f-22fe-437d-ab52-781bdb234018")
				.header("Accept", "application/xml")
				.header("Content-Type", "application/vnd.onem2m-ntfy+xml;charset=UTF-8");
	}

	private FakeSubscriptionCreator header(String key, String val) {
		builder.header(key, val);
		return this;
	}

	private FakeSubscriptionCreator body(String content) {
		builder.POST(HttpRequest.BodyPublishers
				.ofString(content, StandardCharsets.UTF_8));
		return this;
	}

	private String buildBody(Ltid ltid, String hexData) {
		OffsetDateTime now = OffsetDateTime.now();
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
				.append("<m2m:cin xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">")
				.append("<ty>").append(4).append("</ty>")
				.append("<ri>").append("CI00000000000099328324").append("</ri>")
				.append("<rn>").append("CI00000000000099328324").append("</rn>")
				.append("<pi>").append("CT00000000000000006585").append("</pi>")
				.append("<ct>").append(now.format(OFFSET_DATETIME_FORMAT)).append("</ct>")
				.append("<lt>").append(now.format(OFFSET_DATETIME_FORMAT)).append("</lt>")
				.append("<sr>").append("/").append(ltid.getAppEuiAsString()).append("/v1_0/remoteCSE-").append(ltid.toString()).append("/container-LoRa/subscription-").append(SUBS_NAME).append("</sr>")
				.append("<et>").append(now.plusDays(1).format(OFFSET_DATETIME_FORMAT)).append("</et>")
				.append("<st>").append(1836).append("</st>")
				.append("<cr>").append("RC00000000000000383679").append("</cr>")
				.append("<cnf>").append("LoRa/Sensor").append("</cnf>")
				.append("<cs>").append(hexData.length()).append("</cs>")
				.append("<con>").append(hexData).append("</con>")
				.append("</m2m:cin>");

		return sb.toString();
	}

	public HttpRequest build(final Ltid ltid, final String hexData) {
		header("X-M2M-Origin", ltid.toString());
		body(buildBody(ltid, hexData));
		return builder.build();
	}

	public HttpResponse<String> send(final Ltid ltid, final String hexData) throws IOException, InterruptedException {
		return httpClient.send(build(ltid, hexData),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
	}

}
