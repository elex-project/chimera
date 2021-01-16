package com.elex_project.chimera;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class UtilsTest {
	@Test //@Ignore
	public void parseLtid() {
		String sr = "/0000000000000004/v1_0/remoteCSE-00000004702c1ffffe1d79da/container-LoRa/subscription-test_subscription";
		Pattern p = Pattern.compile("([0-9A-Fa-f]{24})");
		Matcher m = p.matcher(sr);
		if (m.find()) {
			log.info("{}", m.group(1));
			assertEquals("00000004702c1ffffe1d79da", m.group(1));
		} else {
			log.info("Not Found.");
		}
	}

	@Test // @Ignore
	public void toDate() throws ParseException {
		LocalDateTime date = Utils.toLocalDateTime("2019-01-27T14:07:27+09:00");

		//Calendar calendar = Calendar.getInstance(Locale.KOREA);
		//calendar.setTimeInMillis(date);

		log.info("{}", date.toString());
		assertEquals(LocalDateTime
				.of(2019, 1, 27, 14, 7, 27), date);

		LocalDate date2 = Utils.toLocalDate("2019-January-14th");
		//calendar.setTimeInMillis(date2);

		log.info("{}", date2.toString());
		assertEquals(LocalDate.of(2019, 1, 14), date2);

		//LocalDate d = LocalDate.of(2019,1,14);
		//L.i(d.format(DateTimeFormatter.ofPattern("yyyy-MMMM-d['st']['nd']['rd']['th']", Locale.ENGLISH)));
	}
}