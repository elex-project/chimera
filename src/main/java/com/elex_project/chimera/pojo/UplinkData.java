/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.pojo;

import com.elex_project.chimera.exception.InvalidXMLFormatException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static com.elex_project.chimera.Utils.*;

/**
 * 최근 수신 데이터 및 구독 데이터
 *
 * @author Elex
 */
@Slf4j
@Getter
@ToString
public final class UplinkData {
	private static final String TAG = UplinkData.class.getSimpleName();
	@JsonProperty("createdTime")
	private LocalDateTime createdTime;//ct
	@JsonProperty("lastModifiedTime")
	private LocalDateTime lastModifiedTime;//lt
	@JsonProperty("expirationTime")
	private LocalDateTime expirationTime;//et
	@JsonProperty("creator")
	private String creator;//cr
	@JsonProperty("content")
	private String content;//con
	@JsonProperty("contentSize")
	private int contentSize;//cs

	public UplinkData(final @Nullable Document xml) {
		if (null != xml) {
			parse(xml);
		}
	}

	private void parse(final @NotNull Document xml) {
		try {
			Element root = xml.getDocumentElement();

			Element el = (Element) root.getElementsByTagName("ct").item(0);
			if (null == el) throw new InvalidXMLFormatException("No 'ct' tag.");
			this.createdTime = toLocalDateTime(el.getTextContent());

			el = (Element) root.getElementsByTagName("lt").item(0);
			if (null != el) {
				this.lastModifiedTime = toLocalDateTime(el.getTextContent());
			} else {
				this.lastModifiedTime = DEFAULT_DATETIME;
			}

			el = (Element) root.getElementsByTagName("et").item(0);
			if (null != el) {
				this.expirationTime = toLocalDateTime(el.getTextContent());
			} else {
				this.expirationTime = DEFAULT_DATETIME;
			}

			el = (Element) root.getElementsByTagName("cr").item(0);
			if (null != el) {
				this.creator = el.getTextContent();
			} else {
				this.creator = EMPTY_STRING;
			}

			el = (Element) root.getElementsByTagName("cs").item(0);
			if (null != el) {
				try {
					this.contentSize = Integer.parseInt(el.getTextContent());
				} catch (NumberFormatException e) {
					this.contentSize = Integer.MIN_VALUE;
				}
			} else {
				this.contentSize = Integer.MIN_VALUE;
			}

			el = (Element) root.getElementsByTagName("con").item(0);
			if (null == el) throw new InvalidXMLFormatException("No 'con' tag.");
			this.content = el.getTextContent();
		} catch (InvalidXMLFormatException | DateTimeParseException e) {
			log.error("Invalid XML format.", e);
		}
	}


}
