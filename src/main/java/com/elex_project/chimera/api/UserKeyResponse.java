/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import com.elex_project.chimera.exception.InvalidXMLFormatException;
import com.elex_project.chimera.pojo.UserKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.net.http.HttpResponse;

import static com.elex_project.chimera.Utils.EMPTY_STRING;

/**
 * @author Elex
 */
@Slf4j
@Getter
@ToString
public final class UserKeyResponse extends BaseResponse {
	public static final int STATUS_OK = 200;

	@JsonProperty("resultCode")
	private int resultCode;
	@JsonProperty("resultMessage")
	private String resultMessage;
	@JsonProperty("userId")
	private String userId;
	@JsonProperty("password")
	private String password;
	@JsonProperty("isAdmin")
	private boolean isAdmin;
	@JsonProperty("userKey")
	private UserKey userKey;

	UserKeyResponse(@NotNull HttpResponse<String> response) {
		super(response);
		if (null != xml) {
			parse();
		}
	}

	private void parse() {
		try {
			Element root = xml.getDocumentElement();

			Element el = (Element) root.getElementsByTagName("result_code").item(0);
			if (null != el) {
				try {
					this.resultCode = Integer.parseInt(el.getTextContent());
				} catch (NumberFormatException e) {
					this.resultCode = Integer.MIN_VALUE;
				}
			} else {
				this.resultCode = Integer.MIN_VALUE;
			}

			el = (Element) root.getElementsByTagName("result_msg").item(0);
			if (null != el) {
				this.resultMessage = el.getTextContent();
			} else {
				this.resultMessage = EMPTY_STRING;
			}

			Element userEl = (Element) root.getElementsByTagName("user").item(0);
			if (null == userEl) throw new InvalidXMLFormatException("No 'user' tag.");

			el = (Element) userEl.getElementsByTagName("admin_yn").item(0);
			if (null != el) {
				this.isAdmin = el.getTextContent().equalsIgnoreCase("Y");
			} else {
				this.isAdmin = false;
			}

			el = (Element) userEl.getElementsByTagName("password").item(0);
			if (null != el) {
				this.password = el.getTextContent();
			} else {
				this.password = EMPTY_STRING;
			}

			el = (Element) userEl.getElementsByTagName("user_id").item(0);
			if (null != el) {
				this.userId = el.getTextContent();
			} else {
				this.userId = EMPTY_STRING;
			}

			el = (Element) userEl.getElementsByTagName("uKey").item(0);
			if (null == el) throw new InvalidXMLFormatException("No 'uKey' tag.");
			this.userKey = UserKey.of(el.getTextContent());
		} catch (InvalidXMLFormatException e) {
			log.error("Invalid XML format.", e);
		}
	}


}
