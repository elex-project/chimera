package com.elex_project.chimera;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
public class HttpTest {
	@Test
	@Disabled
	public void get() throws URISyntaxException, IOException, InterruptedException {
		HttpClient httpClient = HttpClient
				.newBuilder()
				.version(HttpClient.Version.HTTP_2)
				//.proxy(ProxySelector.of(new InetSocketAddress("www.elex-project.com", 80)))
				.followRedirects(HttpClient.Redirect.ALWAYS)
				.build();
		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(new URI("https://www.elex-project.com/"))
				.version(HttpClient.Version.HTTP_2)
				.GET()
				//.method("HEAD", HttpRequest.BodyPublishers.ofString("", StandardCharsets.UTF_8))
				.header("User-Agent", "Elex")
				.timeout(Duration.of(10, SECONDS))
				.build();
		HttpResponse<String> httpResponse =
				httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		log.info(httpResponse.toString());
		log.info(httpResponse.version().toString());
		log.info(httpResponse.body().toString());

	}

	@Test
	@Disabled
	public void getAsync() throws URISyntaxException, IOException, InterruptedException, TimeoutException, ExecutionException {
		HttpClient httpClient = HttpClient
				.newBuilder()
				.version(HttpClient.Version.HTTP_2)
				.followRedirects(HttpClient.Redirect.ALWAYS)
				.build();
		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(new URI("https://www.elex-project.com/"))
				.version(HttpClient.Version.HTTP_2)
				.GET()
				.header("User-Agent", "Elex")
				.timeout(Duration.of(10, SECONDS))
				.build();
		CompletableFuture<HttpResponse<String>> httpResponse =
				httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		String s = httpResponse.
				thenApply(HttpResponse::body)
				.get(5, TimeUnit.SECONDS);
		log.info(s);

	}

	@Test
	@Disabled
	public void post() throws URISyntaxException, IOException, InterruptedException {
		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(new URI("https://www.elex-project.com/"))
				.version(HttpClient.Version.HTTP_2)
				.header("User-Agent", "Elex")
				.POST(HttpRequest.BodyPublishers.ofString("", StandardCharsets.UTF_8))
				.timeout(Duration.of(10, SECONDS))
				.build();
		HttpResponse<String> httpResponse = HttpClient
				.newBuilder()
				//.proxy(ProxySelector.getDefault())
				.build()
				.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		log.info(httpResponse.toString());
		log.info(httpResponse.version().toString());
		log.info(httpResponse.body());
	}
}