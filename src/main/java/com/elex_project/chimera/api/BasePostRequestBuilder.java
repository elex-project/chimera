/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

/**
 *
 * @param <T>
 *     @author Elex
 */
@Slf4j
abstract class BasePostRequestBuilder<T extends IResponse> extends BaseRequestBuilder<T> {

	public BasePostRequestBuilder() {
		super();
	}

	public BasePostRequestBuilder<T> body(String content) {
		builder.POST(HttpRequest.BodyPublishers.ofString(content, StandardCharsets.UTF_8));
		return this;
	}

}
