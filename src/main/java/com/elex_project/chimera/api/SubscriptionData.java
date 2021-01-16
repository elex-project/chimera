/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import com.elex_project.chimera.exception.InvalidValueException;
import com.elex_project.chimera.exception.InvalidXMLFormatException;
import com.elex_project.chimera.pojo.Ltid;
import com.elex_project.chimera.pojo.UplinkData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.elex_project.chimera.Utils.EMPTY_STRING;

/**
 * 구독 받는 데이터
 *
 * <pre><code>
 * POST /data HTTP/1.1
 * X-M2M-Origin: 00000004702c1ffffe1d79da
 * X-M2M-RI: 13868a1f-22fe-437d-ab52-781bdb234018
 * Accept: application/xml
 * Content-Type: application/vnd.onem2m-ntfy+xml;charset=UTF-8
 * User-Agent: Jakarta Commons-HttpClient/3.0.1
 * Host: xxx.xxx.xxx.xxx:5000
 * Content-Length: 601
 * </code></pre>
 * # Subscrption 이후 LoRa 디바이스가 Uplink를 하면 ThingPlug는 사용자의 App/Web 서버로 아래와 같은 포맷으로 데이터를 Push해 줍니다.
 *
 * <pre><code>
 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 * <m2m:cin
 * 	xmlns:m2m="http://www.onem2m.org/xml/protocols"
 * 	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
 * 	<ty>4</ty>
 * 	<ri>CI00000000000099328324</ri>
 * 	<rn>CI00000000000099328324</rn>
 * 	<pi>CT00000000000000006585</pi>
 * 	<ct>2017-12-19T10:53:59+09:00</ct>
 * 	<lt>2017-12-19T10:53:59+09:00</lt>
 * 	<sr>/0000000000000004/v1_0/remoteCSE-00000004702c1ffffe1d79da/container-LoRa/subscription-test_subscription</sr>
 * 	<et>2017-12-20T10:53:59+09:00</et>
 * 	<st>1836</st>
 * 	<cr>RC00000000000000383679</cr>
 * 	<cnf>LoRa/Sensor</cnf>
 * 	<cs>20</cs>
 * 	<con>31323334353637383930</con>
 * </m2m:cin>
 * </code></pre>
 * <p>
 * <p>
 * App/Web 서버가 수신하는 위 데이터 포맷을 보면 LoRa 디바이스가 전송한 Uplink 데이터가 <con> 필드에 포함된 것을 확인할 수 있습니다.
 * 위 예제에서는 “31323334353637383930” 이라는 데이터를 보낸 것을 확인할 수 있습니다. LoRa 디바이스가 데이터를 보낸 시간을 <ct> 필드로 확인할 수 있으며, <et> 필드의 시간이 되면 해당 데이터는 ThingPlug에서 삭제됩니다.
 *
 * @author Elex
 */
@Slf4j
@ToString
public final class SubscriptionData {

	private static final Pattern PATTERN = Pattern.compile("([0-9A-Fa-f]{24})");
	private static final Pattern PATTERN_SN = Pattern.compile("subscription-(.*)$");
	private final UplinkData data;
	private Ltid ltid;

	private String subscriptionName;

	public SubscriptionData(@NotNull Document xml) {
		data = new UplinkData(xml);

		try {
			Element root = xml.getDocumentElement();
			Element el = (Element) root.getElementsByTagName("sr").item(0);
			if (null == el) throw new InvalidXMLFormatException("No 'sr' tag.");
			this.ltid = new Ltid(parseLtid(el.getTextContent()));

			this.subscriptionName = parseSubsName(el.getTextContent());

		} catch (InvalidValueException | InvalidXMLFormatException e) {
			log.error("Invalid XML format.", e);
		}
	}

	private static String parseLtid(String sr) {
		Matcher m = PATTERN.matcher(sr);//this.
		if (m.find()) {
			return m.group(1);
		} else {
			return EMPTY_STRING;
		}
	}

	private static String parseSubsName(String sr) {
		Matcher m = PATTERN_SN.matcher(sr);//this.
		if (m.find()) {
			return m.group(1);
		} else {
			return EMPTY_STRING;
		}
	}

	@JsonProperty("ltid")
	public Ltid getLtid() {
		return ltid;
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

	@JsonProperty("subscriptionName")
	public String getSubscriptionName() {
		return subscriptionName;
	}

}
