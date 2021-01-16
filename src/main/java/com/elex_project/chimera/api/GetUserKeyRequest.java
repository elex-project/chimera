/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;

/**
 * 1. ThingPlug API로 사용자 인증키(uKey) 조회하기
 * ThingPlug 포탈의 ID와 Password를 사용해 uKey를 조회할 수 있습니다.
 * 이 값은 변경되지 않는 고유의 인증 키이며, 이 키는 LoRa 디바이스의 데이터 조회와 제어 권한을 가지고 있기 때문에 유출되지 않도록 관리해야 합니다.
 * <p>
 * Description	ThingPlug의 사용자 인증키(ukey)를 조회하기 위한 API
 * Requirements	user_id: 포탈 로그인 ID
 * password: 포탈 로그인 비밀번호
 * <p>
 * 2. 요청이 잘못 되었을 때
 * 만약 포탈의 ID와 Password가 잘못 입력했을 경우 아래와 같은 응답을 받게 됩니다.
 *
 * @author Elex
 */
public final class GetUserKeyRequest extends BasePutRequestBuilder<UserKeyResponse> implements IRequest {
	private static final String URL = serverHost() + "/ThingPlug?division=user&function=login";

	public GetUserKeyRequest(String userId, String password) {
		super();
		uri(URL)
				.header("user_id", userId)
				.header("password", password);
		//setUserAgent(USER_AGENT);
		body("");
	}


	@NotNull
	@Contract("_ -> new")
	@Override
	public UserKeyResponse conv(HttpResponse<String> response) {
		return new UserKeyResponse(response);
	}

}
