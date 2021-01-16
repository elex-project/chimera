/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.StringJoiner;

/**
 *
 * @param <T>
 *     @author Elex
 */
@Slf4j
abstract class BaseGetRequestBuilder<T extends IResponse> extends BaseRequestBuilder<T> {

	public BaseGetRequestBuilder() {
		super();
		builder.GET();
	}

	protected static String join(@NotNull Map<String, String> params) {
		StringJoiner joiner = new StringJoiner("&");
		for (String key : params.keySet()) {
			joiner.add(key + "=" + params.get(key));
		}
		return joiner.toString();
	}

}
