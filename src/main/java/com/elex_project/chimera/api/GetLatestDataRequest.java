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
 * 최신 데이터를 조회하게 되면, 해당 LoRa 디바이스가 Uplink하고 있는 데이터들 중 가장 최신의 데이터 하나를 조회할 수 있습니다.
 * <p>
 * Description	LoRa 디바이스의 Uplink 데이터 중 가장 최신에 저장된 값을 호출합니다.
 * Requirements	ukey: 사용자 인증키
 * <p>
 * LoRa 디바이스가 데이터를 보낸 시간을 <ct> 필드로 확인할 수 있으며, <et> 필드의 시간이 되면 해당 데이터는 ThingPlug에서 삭제됩니다.
 *
 * @author Elex
 */
public final class GetLatestDataRequest extends BaseGetRequestBuilder<LatestDataResponse> {

	public GetLatestDataRequest(Ltid ltid, @NotNull UserKey uKey) {
		super();
		uri(path(ltid))
				.header("X-M2M-RI", ltid.toString() + "_" + Random.nextInt(9999))
				.header("X-M2M-Origin", ltid.toString())
				.header("uKey", uKey.toString());
		//setUserAgent(USER_AGENT);
	}

	@NotNull
	private static String path(@NotNull Ltid ltid) {
		return serverHost() + "/" + ltid.getAppEui() + "/v1_0/remoteCSE-" + ltid.toString() + "/container-LoRa/latest";
	}

	@NotNull
	@Contract("_ -> new")
	@Override
	public LatestDataResponse conv(HttpResponse<String> response) {
		return new LatestDataResponse(response);
	}
}
