package com.elex_project.chimera;

import com.elex_project.chimera.api.SubscriptionData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;
@Slf4j
public class SubscriptionDataTest {
	@Test
	@Disabled
	public void test(){
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(SubscriptionDataTest.class
				.getResourceAsStream("/subs_sample.xml")))){
			StringBuilder sb = new StringBuilder();
			reader.lines().forEachOrdered(new Consumer<String>() {
				@Override
				public void accept(final String s) {
					sb.append(s);
				}
			});

			Document document = Utils.buildXML(sb.toString());
			SubscriptionData subscriptionData = new SubscriptionData(document);
			log.info(new ObjectMapper().writeValueAsString(subscriptionData));

		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}

	}
}
