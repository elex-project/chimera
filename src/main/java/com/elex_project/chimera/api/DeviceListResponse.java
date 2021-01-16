/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import com.elex_project.chimera.exception.InvalidValueException;
import com.elex_project.chimera.exception.InvalidXMLFormatException;
import com.elex_project.chimera.pojo.Device;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static com.elex_project.chimera.Utils.EMPTY_STRING;

/**
 * @author Elex
 */
@Slf4j
@Getter
@ToString
public final class DeviceListResponse extends BaseResponse {
	public static final int STATUS_OK = 200;
	@JsonProperty("resultCode")
	private int resultCode;
	@JsonProperty("resultMessage")
	private String resultMessage;
	@JsonProperty("totalListCount")
	private long totalListCount;
	@JsonProperty("devices")
	private List<Device> devices;

	DeviceListResponse(@NotNull HttpResponse<String> response) {
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

			el = (Element) root.getElementsByTagName("total_list_count").item(0);
			if (null == el) throw new InvalidXMLFormatException("No 'total_list_count' tag.");
			try {
				this.totalListCount = Long.parseLong(el.getTextContent());
			} catch (NumberFormatException e) {
				throw new InvalidXMLFormatException("Number? " + el.getTextContent());
			}

			try {
				NodeList nodeList = root.getElementsByTagName("devices").item(0)
						.getChildNodes();
				devices = new ArrayList<>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element devElem = (Element) nodeList.item(i);
					try {
						devices.add(new Device(devElem));
					} catch (InvalidValueException e) {
						log.error("Invalid value.", e);
					}
				}
			} catch (NullPointerException e) {
				throw new InvalidXMLFormatException("No 'devices' tag.");
			}
		} catch (InvalidXMLFormatException e) {
			log.error("Invalid XML format.", e);
		}
	}

}
