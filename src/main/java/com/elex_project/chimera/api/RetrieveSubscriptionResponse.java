/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import java.net.http.HttpResponse;

/**
 * @author Elex
 */
public final class RetrieveSubscriptionResponse extends CreateSubscriptionResponse {
	public static final int STATUS_OK = 200;
	public static final int STATUS_ALREADY_REGISTERED_SUBSCRIPTION = 409;

	RetrieveSubscriptionResponse(HttpResponse<String> response) {
		super(response);
	}
}
