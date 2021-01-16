/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2019. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * 구독 신청 / 변경을 위한 데이터
 *
 * @author Elex
 */
public final class Subscription {
	public static final String CONTENT_TYPE = "application/vnd.onem2m-res+xml;ty=23";

	private final String name;
	private final String url;
	private EventNotificationCriteria enc;
	private NotificationContentType nct;

	/**
	 * static메서드를 사용할 것.
	 *
	 * @param name
	 * @param url
	 */
	private Subscription(final String name, final String url) {
		this.name = name;
		this.url = url;
		this.enc = EventNotificationCriteria.ChildCreated;
		this.nct = NotificationContentType.WholeResource;
	}

	/**
	 * @param name 구독명
	 * @param url  구독받을 주소
	 * @return
	 */
	@NotNull
	@Contract(pure = true)
	public static Subscription newHttpSubscription(final String name, final String url) {
		return new Subscription(name, "HTTP|" + url);
	}

	@NotNull
	@Contract(pure = true)
	public static Subscription newMqttSubscription(final String name, final String clientId) {
		return new Subscription(name, "MQTT|" + clientId);
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	@JsonProperty("enc")
	public EventNotificationCriteria getEnc() {
		return enc;
	}

	public void setEnc(EventNotificationCriteria enc) {
		this.enc = enc;
	}

	@JsonProperty("nct")
	public NotificationContentType getNct() {
		return nct;
	}

	public void setNct(NotificationContentType nct) {
		this.nct = nct;
	}

	@NotNull
	public String getContentBody() {
		//String s = """<?xml version="1.0"?>""";
		return new StringBuilder()
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<m2m:sub xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">")
				.append(this.enc.toString())
				.append("<nu>").append(this.getUrl()).append("</nu>")
				.append(this.nct.toString())
				.append("</m2m:sub>")
				.toString();
	}

	/**
	 * 통지가 실행(Trigger)가 되는 조건(Condition)
	 */
	public enum EventNotificationCriteria {
		ChildCreated(1),
		ChildDeleted(2),
		Updated(3),
		Deleted(4),
		ChildUpdated(5); // 제어결과 업데이트

		private final int rss;

		EventNotificationCriteria(int val) {
			rss = val;
		}


		@NotNull
		@Contract(pure = true)
		@Override
		public String toString() {
			return "<enc><rss>" + this.rss + "</rss></enc>";
		}
	}

	/**
	 * 통지 메시지 타입을 설정
	 */
	public enum NotificationContentType {
		ModifiedAttributeOnly(1),
		WholeResource(2);

		private final int val;

		NotificationContentType(int val) {
			this.val = val;
		}

		@NotNull
		@Contract(pure = true)
		@Override
		public String toString() {
			return "<nct>" + this.val + "</nct>";
		}
	}
}
