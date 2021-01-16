/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;


import com.elex_project.chimera.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Slf4j
@ToString
public abstract class BaseResponse implements IResponse {

	private final HttpResponse<String> httpResponse;
	protected Document xml;

	BaseResponse(@NotNull HttpResponse<String> httpResponse) {
		this.httpResponse = httpResponse;

		try {
			if (httpResponse.statusCode() <= 400 && !(this instanceof IEmptyBodyResponse)) {
				this.xml = Utils.buildXML(httpResponse.body());

			} else {
				this.xml = null;
			}
		} catch (ParserConfigurationException | IOException | SAXException e) {
			log.error("Unable to parse the response XML.", e);
		}
	}

	/**
	 * Http status code
	 *
	 * @return
	 */
	@JsonProperty("httpStatus")
	public int getStatus() {
		return httpResponse.statusCode();
	}

	/**
	 * Http Respons Headers
	 *
	 * @return
	 */
	@JsonIgnore
	public Map<String, List<String>> getHeaders() {
		return httpResponse.headers().map();
	}

	/**
	 * Http Response body
	 *
	 * @return
	 */
	@JsonIgnore
	public String getBody() {
		return httpResponse.body();
	}

	/**
	 * Http Response body as XML
	 *
	 * @return
	 */
	@JsonIgnore
	public Document getXml() {
		return xml;
	}


}
