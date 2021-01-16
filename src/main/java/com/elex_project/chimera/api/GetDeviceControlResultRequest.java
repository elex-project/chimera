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
 * App/Web 서버가 제어 요청을 하게 되면 N/W 서버는 LoRa 디바이스로 제어 명령을 전달(Downlink)하고 성공 여부를 ThingPlug에 저장하게 됩니다. 그 후에 사용자는 해당 제어 명령에 대한 결과를 별도의 요청을 통해 확인할 수 있습니다.
 * <p>
 * 제어 명령의 결과를 조회하기 위해서는 제어 명령의 Resource ID를 우선 확인해야 합니다. 제어 명령의 Resource ID는 제어 요청 시 응답에서 확인할 수 있습니다. <m2m:exin> 하위에 <ri>의 값이 제어 명령에 대한 Resource ID입니다. <m2m:exin> 하위에 <ri>의 값이 제어 명령에 대한 Resource ID입니다.
 *
 * <pre>
 * ukey: 사용자 인증키
 * execInstance: 제어 명령의 Resource ID
 * </pre>
 * <p>
 * 제어 결과에서 <exs>와 <exr>로 결과를 확인할 수 있습니다.
 * <exs>는 execute Status의 약자로 제어의 현재 상태를 의미 합니다.
 * 3은 ‘Finished’를 의미하며 제어 명령의 처리가 끝났음을 의미합니다.
 * <exr>은 execute Result의 약자로 제어 명령의 처리가 끝났을 때의 결과를 의미합니다. 0은 ‘SUCCEED’를 의미합니다.
 *
 * @author Elex
 */
public final class GetDeviceControlResultRequest extends BaseGetRequestBuilder<DeviceControlResultResponse> {

	public GetDeviceControlResultRequest(Ltid ltid, String resId, @NotNull UserKey userKey) {
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
				+ ltid.toString() + "_extDevMgmt/execInstance-" + resId;
	}

	@NotNull
	@Contract("_ -> new")
	@Override
	public DeviceControlResultResponse conv(HttpResponse<String> response) {
		return new DeviceControlResultResponse(response);
	}
}
