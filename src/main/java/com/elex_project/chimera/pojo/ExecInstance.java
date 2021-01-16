/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2019. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.pojo;

import com.elex_project.chimera.exception.InvalidXMLFormatException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static com.elex_project.chimera.Utils.*;


/**
 * 장치 제어에서 사용된다.
 *
 * @author Elex
 */
@Slf4j
@Getter
public final class ExecInstance {
	@JsonProperty("resId")
	private final String resId;
	@JsonProperty("extra")
	private final String extra;
	@JsonProperty("createdTime")
	private LocalDateTime createdTime;
	@JsonProperty("lastModifiedTime")
	private LocalDateTime lastModifiedTime;
	@JsonProperty("expirationTime")
	private LocalDateTime expirationTime;
	@JsonProperty("exs")
	private int exs;
	@JsonProperty("exr")
	private int exr;
	@JsonProperty("ext")
	private String ext; // ext

	public ExecInstance(final @NotNull Element exinElem) throws InvalidXMLFormatException {
		//Element root = xml.getDocumentElement();

		Element el = (Element) exinElem.getElementsByTagName("ri").item(0);
		if (null == el) throw new InvalidXMLFormatException("No 'ri' tag.");
		this.resId = el.getTextContent();

		el = (Element) exinElem.getElementsByTagName("ct").item(0);
		if (null != el) {
			try {
				this.createdTime = toLocalDateTime(el.getTextContent());
			} catch (DateTimeParseException e) {
				this.createdTime = DEFAULT_DATETIME;
			}
		} else {
			this.createdTime = DEFAULT_DATETIME;
		}

		el = (Element) exinElem.getElementsByTagName("lt").item(0);
		if (null != el) {
			try {
				this.lastModifiedTime = toLocalDateTime(el.getTextContent());
			} catch (DateTimeParseException e) {
				this.lastModifiedTime = DEFAULT_DATETIME;
			}
		} else {
			this.lastModifiedTime = DEFAULT_DATETIME;
		}

		el = (Element) exinElem.getElementsByTagName("et").item(0);
		if (null != el) {
			try {
				this.expirationTime = toLocalDateTime(el.getTextContent());
			} catch (DateTimeParseException e) {
				this.expirationTime = DEFAULT_DATETIME;
			}
		} else {
			this.expirationTime = DEFAULT_DATETIME;
		}

		el = (Element) exinElem.getElementsByTagName("exs").item(0);
		if (null == el) throw new InvalidXMLFormatException("No 'exs' tag.");
		try {
			this.exs = Integer.parseInt(el.getTextContent());
		} catch (NumberFormatException e) {
			this.exs = Integer.MIN_VALUE;
		}

		el = (Element) exinElem.getElementsByTagName("exr").item(0);
		if (null != el) {
			try {
				this.exr = Integer.parseInt(el.getTextContent());
			} catch (NumberFormatException e) {
				this.exr = Integer.MIN_VALUE;
			}
		} else {
			this.exr = Integer.MIN_VALUE;
		}


		el = (Element) exinElem.getElementsByTagName("ext").item(0);
		if (null != el) {
			this.ext = el.getTextContent();
		} else {
			this.ext = EMPTY_STRING;
		}

		el = (Element) exinElem.getElementsByTagName("ext").item(0);
		if (null != el) {
			this.ext = el.getTextContent();
		} else {
			this.ext = EMPTY_STRING;
		}

		el = (Element) exinElem.getElementsByTagName("exra").item(0);
		if (null != el) {
			this.extra = el.getTextContent();
		} else {
			this.extra = EMPTY_STRING;
		}
	}

}
