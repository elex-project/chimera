/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;

/**
 * @author Elex
 */
@Slf4j
public final class UpdateSubscriptionResponse extends CreateSubscriptionResponse {
	public static final int STATUS_OK = 200;
	public static final int STATUS_UNREGISTERED_SUBSCRIPTION = 404;

	UpdateSubscriptionResponse(HttpResponse<String> response) {
		super(response);
	}
}
