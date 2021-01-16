/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import com.elex_project.chimera.exception.InvalidXMLFormatException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static com.elex_project.chimera.Utils.*;

/**
 * @author Elex
 */
@Slf4j
@Getter
@ToString
public class CreateSubscriptionResponse extends BaseResponse {
	public static final int STATUS_CREATED = 201;
	public static final int STATUS_ALREADY_REGISTERED_SUBSCRIPTION = 409;

	@JsonProperty("resId")
	private String resourceId; // ri
	@JsonProperty("name")
	private String name; // rn
	@JsonProperty("createdTime")
	private LocalDateTime createdTime;// ct
	@JsonProperty("lastModifiedTime")
	private LocalDateTime lastModifiedTime; // lt
	@JsonProperty("rss")
	private int rss = 1; // rss
	@JsonProperty("url")
	private String url; // nu
	@JsonProperty("nct")
	private int nct = 2; // nct

	CreateSubscriptionResponse(HttpResponse<String> response) {
		super(response);

		if (null != xml) {
			parse();
		}

	}

	private void parse() {
		try {
			Element root = xml.getDocumentElement();

			Element el = (Element) root.getElementsByTagName("ri").item(0);
			if (null != el) {
				this.resourceId = el.getTextContent();
			} else {
				this.resourceId = EMPTY_STRING;
			}

			el = (Element) root.getElementsByTagName("rn").item(0);
			if (null == el) throw new InvalidXMLFormatException("No 'rn' tag.");
			this.name = el.getTextContent();

			el = (Element) root.getElementsByTagName("ct").item(0);
			if (null != el) {
				try {
					this.createdTime = toLocalDateTime(el.getTextContent());
				} catch (DateTimeParseException e) {
					throw new InvalidXMLFormatException("Cannot parse: " + el.getTextContent());
				}
			} else {
				this.createdTime = DEFAULT_DATETIME;
			}

			el = (Element) root.getElementsByTagName("lt").item(0);
			if (null != el) {
				try {
					this.lastModifiedTime = toLocalDateTime(el.getTextContent());
				} catch (DateTimeParseException e) {
					throw new InvalidXMLFormatException("Cannot parse: " + el.getTextContent());
				}
			} else {
				this.lastModifiedTime = DEFAULT_DATETIME;
			}

			el = (Element) root.getElementsByTagName("nu").item(0);
			if (null == el) throw new InvalidXMLFormatException("No 'nu' tag.");
			this.url = el.getTextContent();

			el = (Element) root.getElementsByTagName("nct").item(0);
			if (null != el) {
				this.nct = Integer.parseInt(el.getTextContent());
			}

			try {
				el = (Element) ((Element) root.getElementsByTagName("enc").item(0)).getElementsByTagName("rss").item(0);
				if (null != el) {
					this.rss = Integer.parseInt(el.getTextContent());
				}
			} catch (NullPointerException ignore) {

			}
		} catch (InvalidXMLFormatException e) {
			log.error("Check your XML format.", e);
		}
	}

}
