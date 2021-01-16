/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Random;
import java.util.StringJoiner;

/**
 * @author Elex
 */
@Slf4j
public final class Utils {
	public static final String EMPTY_STRING = "";
	public static final LocalDateTime DEFAULT_DATETIME
			= LocalDate.of(1, 1, 1).atStartOfDay();
	public static final Random Random = new Random();
	// 2019-01-27T14:07:27+09:00
	static final DateTimeFormatter OFFSET_DATETIME_FORMAT
			= DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
	// 2019-January-14th
	static final DateTimeFormatter EN_DATE_FORMAT
			= DateTimeFormatter.ofPattern("yyyy-MMMM-d['st']['nd']['rd']['th']", Locale.ENGLISH);
	private final static char[] HEX_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f'};//"0123456789abcdef".toCharArray();

	private Utils() {
	}

	/**
	 * 2019-01-27T14:07:27+09:00
	 *
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	@NotNull
	public static LocalDateTime toLocalDateTime(final String str) throws DateTimeParseException {
		//return LocalDateTime.ofInstant(simpleDateTimeFormat.parse(str).toInstant(),
		//		ZoneId.systemDefault());
		return OffsetDateTime.parse(str).toLocalDateTime();
	}

	/**
	 * 2019-January-14th
	 *
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	@NotNull
	public static LocalDate toLocalDate(final String str) throws DateTimeParseException {
		//return LocalDate.ofInstant(simpleDateFormat.parse(str).toInstant(),
		//		ZoneId.systemDefault());
		return LocalDate.parse(str, EN_DATE_FORMAT);
	}

	@NotNull
	public static String byteArrayToHex(@Nullable String separator, @Nullable final byte... bytes) {
		if (null == bytes || bytes.length == 0) {
			return EMPTY_STRING;
		}

		final int byteLength = bytes.length;
		char[] buffer = new char[2];
		StringJoiner joiner = new StringJoiner((null == separator) ? EMPTY_STRING : separator);
		for (int i = 0; i < byteLength; i++) {
			final int byteHex = bytes[i] & 0xFF;
			buffer[0] = HEX_ARRAY[byteHex >>> 4];
			buffer[1] = HEX_ARRAY[byteHex & 0x0F];
			joiner.add(String.valueOf(buffer));
		}
		return joiner.toString();
	}

	public static Document buildXML(final String xml)
			throws ParserConfigurationException, IOException, SAXException {
		return DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new InputSource(new StringReader(xml)));
	}
}
