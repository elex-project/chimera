/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import com.elex_project.chimera.pojo.UserKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;

/**
 * 1. ThingPlug API로 API로 디바이스 리스트 조회하기
 * 예를 들어 1000개의 LoRa 디바이스를 조회하는 ThingPlug API는 아래와 같습니다.
 * <p>
 * Description	계정에 등록된 LoRa 디바이스 목록을 조회합니다.
 * Requirements	ukey: 사용자 인증키
 * startIndex: 디바이스의 시작 인덱스
 * countPerPage: 디바이스의 개수
 * <p>
 * <p>
 * 2. 디바이스 정보 확인
 * 응답 메시지 중 <device> 부터 </device>까지가 하나의 LoRa 디바이스에 대한 정보로 볼 수 있습니다. 총 1000개의 디바이스를 조회했지만,
 * 실제 등록된 디바이스가 2개뿐이기 때문에 위와 같이 2개의 디바이스만 조회가 되었습니다. 등록된 전체 디바이스의 수는 <total_list_count>로 확인할 수 있습니다.
 * <p>
 * <p>
 * 3. 조회 범위를 초과한 경우의 응답
 * 만약, ThingPlug에 디바이스가 등록되어 있지 않거나, startIndex가 조회 범위를 초과한 경우, 아래와 같이 “조회된 정보가 없습니다.”라는 응답을 확인할 수 있습니다.
 * <p>
 * 4. 계정에 등록된 모든 디바이스 조회하기
 * LoRa 디바이스의 개통이 많아지면서 ThingPlug에 등록되어 있는 디바이스의 수를 파악하기 어려울 때에는 startIndex와 countPerPage를 조정하며
 * 모든 디바이스를 조회할 수 있습니다.
 * 예를 들어 1010개의 LoRa 디바이스가 등록되어 있다고 하면, 아래와 같이 3회의 API 요청을 통해 모든 디바이스를 조회할 수 있습니다.
 * 최초에 startIndex=1, countPerPage=1000 으로 조회합니다. 응답으로 인덱스 번호 1~1000 까지 1000개의 디바이스 정보를 확인할 수 있습니다.
 * 그 후 startIndex=1001, countPerPage=1000으로 조회하게 되면 인덱스 번호로 1001번부터 2000까지의 디바이스를 조회 요청합니다.
 * 하지만 남아있는 디바이스가 10개(1001~1010)이기 때문에 나머지 10개의 디바이스 정보만 확인할 수 있습니다.
 * 마지막으로 startIndex=2001, countPerPage=1000으로 조회를 요청 했을 때에는 조회 가능한 디바이스 인덱스가 없기 때문에
 * “조회된 정보가 없습니다” 메시지를 응답하게 됩니다.
 * <p>
 * https://www.sktiot.com/iot/developer/guide/guide/loRa/menu_04/page_03
 *
 * @author Elex
 */
public final class GetDeviceListRequest extends BaseGetRequestBuilder<DeviceListResponse> {

	/**
	 * @param startIndex   1~
	 * @param countPerPage
	 * @param uKey
	 * @throws IOException
	 */
	public GetDeviceListRequest(int startIndex, int countPerPage, @NotNull UserKey uKey) {
		super();
		//this.ltid = ltid;

		uri(path(startIndex, countPerPage))
				//header("X-M2M-RI", ltid.toString() + "_" + Random.getInt(0,9999));
				//header("X-M2M-Origin", ltid.toString());
				.header("uKey", uKey.toString());
		//setUserAgent(USER_AGENT);
	}

	@NotNull
	private static String path(int startIndex, int countPerPage) {
		HashMap<String, String> param = new HashMap<>();
		param.put("division", "searchDevice");
		param.put("function", "myDevice");
		param.put("startIndex", String.valueOf(startIndex));
		param.put("countPerPage", String.valueOf(countPerPage));

		return serverHost() + "/ThingPlug?" + join(param);
	}

	@NotNull
	@Contract("_ -> new")
	@Override
	public DeviceListResponse conv(HttpResponse<String> response) {
		return new DeviceListResponse(response);
	}
}
