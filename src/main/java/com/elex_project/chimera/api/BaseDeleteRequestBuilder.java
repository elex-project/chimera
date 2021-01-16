/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import lombok.extern.slf4j.Slf4j;

/**
 * @param <T>
 * @author Elex
 */
@Slf4j
abstract class BaseDeleteRequestBuilder<T extends IResponse> extends BaseRequestBuilder<T> {

	public BaseDeleteRequestBuilder() {
		super();
		builder.DELETE();
	}

}
