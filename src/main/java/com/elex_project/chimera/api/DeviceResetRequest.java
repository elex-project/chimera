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
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;

import static com.elex_project.chimera.Utils.Random;

/**
 * LoRa 디바이스를 리셋하기 위한 명령입니다. 이 명령은 오직 디바이스 리셋을 위해 사용하기 때문에 별도의 payload를 함께 보낼 수 없습니다.
 * <p>
 * ukey: 사용자 인증키
 * <exe>: true로 설정
 * <p>
 * # Request
 * <pre><code>
 * PUT /0000000000000004/v1_0/mgmtCmd-00000004702c1ffffe1d79da_DevReset HTTP/1.1
 * Host: thingplugtest.sktiot.com:9443
 * Accept: application/xml
 * X-M2M-RI: 00000004702c1ffffe1d79da_0012
 * X-M2M-Origin: 00000004702c1ffffe1d79da
 * ukey: dG5janFndjNibzZ**************ERjd296V0poMFY0TTJ4UQ==
 * Content-Type: application/xml
 *
 * <?xml version="1.0" encoding="UTF-8"?>
 * <m2m:mgc
 *     xmlns:m2m="http://www.onem2m.org/xml/protocols"
 *     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
 *     <exe>true</exe>
 * </m2m:mgc>
 * </code></pre>
 * <p>
 * # Response
 * <pre><code>
 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 * <m2m:mgc xmlns:m2m="http://www.onem2m.org/xml/protocols" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
 *     <ty>12</ty>
 *     <ri>MC00000000000000011703</ri>
 *     <rn>00000004702c1ffffe1d79da_DevReset</rn>
 *     <pi>CB00000000000000000014</pi>
 *     <ct>2017-08-28T13:59:01+09:00</ct>
 *     <lt>2017-08-28T13:59:01+09:00</lt>
 *     <cmt>DevReset</cmt>
 *     <exe>false</exe>
 *     <ext>ND00000000000000004968</ext>
 *     <m2m:exin>
 *         <ty>8</ty>
 *         <ri>EI00000000000000130139</ri>
 *         <rn>EI00000000000000130139</rn>
 *         <pi>MC00000000000000011703</pi>
 *         <ct>2017-12-19T09:17:00+09:00</ct>
 *         <lt>2017-12-19T09:17:00+09:00</lt>
 *         <et>2017-12-20T09:17:00+09:00</et>
 *         <exs>2</exs>
 *         <ext>ND00000000000000004968</ext>
 *     </m2m:exin>
 * </m2m:mgc>
 * </code></pre>
 *
 * @author Elex
 */
@Slf4j
public final class DeviceResetRequest extends BasePutRequestBuilder<DeviceResetResponse> {
	public DeviceResetRequest(Ltid ltid, @NotNull UserKey userKey) {
		super();
		uri(path(ltid))
				.header("Accept", "application/xml")
				.header("X-M2M-RI", ltid.toString() + "_" + Random.nextInt(9999))
				.header("X-M2M-Origin", ltid.toString())
				//setRequestHeaderProperty("Content-Type", "application/vnd.onem2m-res+xml;ty=23");
				.header("uKey", userKey.toString());
		//setUserAgent(USER_AGENT);

		body(contentBody())
				.header("Content-Type", "application/xml");
	}

	@NotNull
	private static String path(@NotNull Ltid ltid) {
		return serverHost() + "/" + ltid.getAppEui() + "/v1_0/mgmtCmd-"
				+ ltid.toString() + "_DevReset";
	}

	@NotNull
	private static String contentBody() {
		return new StringBuilder()
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<m2m:mgc xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">")
				.append("<exe>true</exe>")
				//sb.append("<exra>").append(extra).append("</exra>");
				.append("</m2m:mgc>")
				.toString();
	}

	@NotNull
	@Contract("_ -> new")
	@Override
	public DeviceResetResponse conv(HttpResponse<String> response) {
		return new DeviceResetResponse(response);
	}
}
