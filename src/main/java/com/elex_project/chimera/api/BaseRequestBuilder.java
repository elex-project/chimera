/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera.api;

import com.elex_project.chimera.ThingPlug;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static com.elex_project.chimera.Utils.EMPTY_STRING;

/**
 *
 * @param <T>
 *     @author Elex
 */
@Slf4j
abstract class BaseRequestBuilder<T extends IResponse> {

	private static final String USER_AGENT = "CHIMERA(mk.II) by ELEX/" +
			System.getProperty("java.runtime.name", EMPTY_STRING) + " " +
			System.getProperty("java.version", EMPTY_STRING) + "/" +
			System.getProperty("os.name", EMPTY_STRING) + " " +
			System.getProperty("os.version", EMPTY_STRING);

	private static HttpClient HTTP_CLIENT;
	protected HttpRequest.Builder builder;

	protected static String serverHost(){
		return ThingPlug.serverHost();
	}

	BaseRequestBuilder() {
		builder = HttpRequest.newBuilder()
				.version(HttpClient.Version.HTTP_2)
				.timeout(Duration.ofSeconds(5))
				.header("User-Agent", USER_AGENT);
	}

	public static void setHttpClient(@NotNull HttpClient client) {
		HTTP_CLIENT = client;
	}

	protected static HttpClient httpClient() {
		if (null == HTTP_CLIENT) {
			HTTP_CLIENT = HttpClient.newBuilder()
					.connectTimeout(Duration.ofSeconds(10)).build();
		}
		return HTTP_CLIENT;
	}

	public BaseRequestBuilder<T> uri(String uri) {
		try {
			builder.uri(new URI(uri));
		} catch (URISyntaxException e) {
			log.error("Check your URI syntax.", e);
		}
		return this;
	}

	public BaseRequestBuilder<T> header(String key, String val) {
		builder.header(key, val);
		return this;
	}

	public HttpRequest build() {
		return builder.build();
	}

	public T send() throws IOException, InterruptedException {
		HttpResponse<String> httpResponse
				= httpClient().send(build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
		return conv(httpResponse);
	}

	public abstract T conv(HttpResponse<String> response);
}
