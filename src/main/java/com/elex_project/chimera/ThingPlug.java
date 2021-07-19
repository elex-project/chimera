/******************************************************************************
 * Project Chimera                                                            *
 * for ThingPlug API                                                          *
 *                                                                            *
 * Copyright (c) 2019. Elex. All Rights Reserved.                             *
 * http://www.elex-project.com/                                               *
 ******************************************************************************/

package com.elex_project.chimera;

import com.elex_project.chimera.api.*;
import com.elex_project.chimera.pojo.Device;
import com.elex_project.chimera.pojo.Ltid;
import com.elex_project.chimera.pojo.Subscription;
import com.elex_project.chimera.pojo.UserKey;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * @since Java 11
 * @author Elex
 */
@Slf4j
public final class ThingPlug {
	private static final String SERVER_ADDR = "https://thingplugpf.sktiot.com:9443";
	private static final String SERVER_ADDR_OPEN = "https://onem2m.sktiot.com:9443";

	private static boolean USE_PRODUCTION_SERVER = true;

	public static String serverHost(){
		return USE_PRODUCTION_SERVER ? SERVER_ADDR : SERVER_ADDR_OPEN;
	}

	public static void useProductionServer(final boolean use){
		USE_PRODUCTION_SERVER = use;
	}

	private static HttpClient HTTP_CLIENT;
	private static int TIME_OUT = 5;

	private ThingPlug() {

	}

	private static HttpClient httpClient() {
		if (null == HTTP_CLIENT) {
			HTTP_CLIENT = HttpClient.newBuilder()
					.connectTimeout(Duration.ofSeconds(10))
					.executor(Executors.newSingleThreadExecutor())
					.build();
		}
		return HTTP_CLIENT;
	}

	/**
	 * 응답 타임아웃
	 *
	 * @param sec
	 */
	public static void setTimeout(int sec) {
		TIME_OUT = sec;
	}

	/**
	 * 클라이언트를 지정. Executor 등등을 변경할 떄 사용.
	 *
	 * @param client
	 */
	public static void setHttpClient(@NotNull HttpClient client) {
		HTTP_CLIENT = client;
	}

	/**
	 * ThingPlug 포탈의 ID와 Password를 사용해 uKey를 조회할 수 있습니다.
	 * 이 값은 변경되지 않는 고유의 인증 키이며, 이 키는 LoRa 디바이스의 데이터 조회와 제어 권한을 가지고 있기 때문에 유출되지 않도록 관리해야 합니다.
	 * <p>
	 * 1. 요청
	 * <pre><code>
	 * PUT /ThingPlug?division=user&function=login HTTP/1.1
	 * Host: thingplugtest.sktiot.com:9443
	 * user_id: {Portal_ID}
	 * password: {Portal_PWD}
	 * </code></pre>
	 * <p>
	 * 2. 응답
	 * <pre><code>
	 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	 * <ThingPlug>
	 *     <result_code>200</result_code>
	 *     <result_msg></result_msg>
	 *     <user>
	 *         <admin_yn>N</admin_yn>
	 *         <password></password>
	 *         <user_id>iot_openhouse</user_id>
	 *         <uKey>dG5janFndjNibzZ**************ERjd296V0poMFY0TTJ4UQ==</uKey>
	 *     </user>
	 * </ThingPlug>
	 * </code></pre>
	 * <p>
	 * <p>
	 * 3. 요청이 잘못되었을 경우
	 * <pre><code>
	 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	 * <ThingPlug>
	 *     <result_code>610</result_code>
	 *     <result_msg>사용자 아이디가 잘못되었습니다.</result_msg>
	 * </ThingPlug>
	 * </code></pre>
	 * <pre><code>
	 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	 * <ThingPlug>
	 *     <result_code>610</result_code>
	 *     <result_msg>비밀번호가 잘못되었습니다.</result_msg>
	 * </ThingPlug>
	 * </code></pre>
	 * <p>
	 * https://www.sktiot.com/iot/developer/guide/guide/loRa/menu_04/page_02
	 *
	 * @param userId   포탈 로그인 ID
	 * @param password 포탈 로그인 비밀번호
	 * @throws
	 */
	public static @NotNull UserKeyResponse getUserKey(@NotNull String userId, @NotNull String password)
			throws IOException, InterruptedException {
		GetUserKeyRequest api = new GetUserKeyRequest(userId, password);
		HttpResponse<String> response
				= httpClient().send(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
		return api.conv(response);
	}

	public static void getUserKey(@NotNull String userId, @NotNull String password,
	                              @NotNull Listener<UserKeyResponse> listener) {
		GetUserKeyRequest api = new GetUserKeyRequest(userId, password);
		CompletableFuture<HttpResponse<String>> future
				= httpClient().sendAsync(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		future.thenApply(api::conv).thenAccept(listener::onCompleted);
		//listener.onCompleted(future.thenApply(api::conv).get(TIME_OUT, TimeUnit.SECONDS));
	}

	/**
	 * ThingPlug를 통해서 계정에 등록된 LoRa 디바이스의 리스트를 조회할 수 있습니다.
	 * LoRa 디바이스가 ThingPlug에 등록되면 등록된 순서에 따라 인덱스가 1부터 할당됩니다.
	 * 디바이스 리스트를 조회할 때, 원하는 인덱스(startIndex) 조회하려는 디바이스의 개수(countPerPage)를 입력해 원하는 디바이스의 정보를 조회할 수 있습니다.
	 * <p>
	 * 조회된 디바이스의 정보를 살펴보면, <device_Id>는 LoRa 디바이스의 고유 ID로 사용되고 있는 LTID입니다. <device_Name>, <device_type>, <category_Id>, <location_Addr> 등 입력되어 있는 정보는 LoRa 디바이스를 ThingPlug에 등록할 때 포탈에 등록한 정보입니다. 따라서 LoRa 디바이스의 실제 응용 분야, 위치 등과 다를 수 있습니다.
	 * <p>
	 * # 조회 범위를 초과한 경우의 응답
	 * <pre><code>
	 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	 * <ThingPlug>
	 *     <result_code>200</result_code>
	 *     <result_msg>조회된 정보가 없습니다.</result_msg>
	 * </ThingPlug>
	 * </code></pre>
	 * <p>
	 * # 계정에 등록된 모든 디바이스 조회하기
	 * LoRa 디바이스의 개통이 많아지면서 ThingPlug에 등록되어 있는 디바이스의 수를 파악하기 어려울 때에는 startIndex와 countPerPage를 조정하며 모든 디바이스를 조회할 수 있습니다.
	 * 예를 들어 1010개의 LoRa 디바이스가 등록되어 있다고 하면, 아래와 같이 3회의 API 요청을 통해 모든 디바이스를 조회할 수 있습니다. 최초에 startIndex=1, countPerPage=1000 으로 조회합니다. 응답으로 인덱스 번호 1~1000 까지 1000개의 디바이스 정보를 확인할 수 있습니다.
	 * 그 후 startIndex=1001, countPerPage=1000으로 조회하게 되면 인덱스 번호로 1001번부터 2000까지의 디바이스를 조회 요청합니다. 하지만 남아있는 디바이스가 10개(1001~1010)이기 때문에 나머지 10개의 디바이스 정보만 확인할 수 있습니다.
	 * 마지막으로 startIndex=2001, countPerPage=1000으로 조회를 요청 했을 때에는 조회 가능한 디바이스 인덱스가 없기 때문에 “조회된 정보가 없습니다” 메시지를 응답하게 됩니다.
	 * <p>
	 * https://www.sktiot.com/iot/developer/guide/guide/loRa/menu_04/page_03
	 *
	 * @param startIndex   디바이스의 시작 인덱스
	 * @param countPerPage 디바이스의 개수
	 * @param uKey         사용자 인증키
	 * @throws IOException
	 */
	public static @NotNull DeviceListResponse getDeviceList(int startIndex, int countPerPage, @NotNull UserKey uKey)
			throws IOException, InterruptedException {
		GetDeviceListRequest api = new GetDeviceListRequest(startIndex, countPerPage, uKey);
		HttpResponse<String> response
				= httpClient().send(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
		return api.conv(response);
	}

	public static void getDeviceList(int startIndex, int countPerPage, @NotNull UserKey uKey,
	                                 @NotNull Listener<DeviceListResponse> listener) {
		GetDeviceListRequest api = new GetDeviceListRequest(startIndex, countPerPage, uKey);
		CompletableFuture<HttpResponse<String>> future
				= httpClient().sendAsync(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		future.thenApply(api::conv).thenAccept(listener::onCompleted);
		//listener.onCompleted(future.thenApply(api::conv).get(TIME_OUT, TimeUnit.SECONDS));
	}

	public static @NotNull CompletableFuture<List<Device>> getDeviceListAsync(final @NotNull UserKey uKey) {
		return CompletableFuture
				.supplyAsync(() -> getDeviceList(uKey));
	}

	public static @NotNull List<Device> getDeviceList(final @NotNull UserKey uKey) {
		final List<Device> deviceList = new ArrayList<>();

		int startIndex = 1;
		final int countPerPage = 500;
		long totalCount;

		GetDeviceListRequest api;
		DeviceListResponse response;
		try {
			do {
				api = new GetDeviceListRequest(startIndex, countPerPage, uKey);
				response = api.send();
				totalCount = response.getTotalListCount();
				//L.v("GD", "from " + startIndex);
				//L.v("GD", "Size: " + response.getTotalListCount());
				deviceList.addAll(response.getDevices());
				startIndex += countPerPage;

			} while (deviceList.size() < totalCount &&
					response.getDevices().size() >= countPerPage);
		} catch (Exception e) {
			log.error("Something's wrong.", e);
		}

		return deviceList;
	}

	/**
	 * ThingPlug의 주요 기능 중 하나는 LoRa 디바이스가 전송한 센서 정보 또는 액츄에이터의 동작 상태 등의 데이터를 저장하고 해당 데이터를 사용자의 App/Web 서버가 조회할 수 있도록 하는 것입니다.
	 * 이러한 데이터는 ThingPlug에 ‘container’와 ‘contentInstance’라는 Resource 타입으로 관리되고 있습니다. ‘container’의 역할은 데이터를 저장하는 역할을 하고, 하위에 ‘contentInstance’ Resource 타입으로 실제 데이터를 저장하고 있습니다.
	 * <p>
	 * 사용자의 App/Web 서버는 ThingPlug를 통해 LoRa 디바이스의 가장 최신(latest) 데이터 하나를 조회할 수 있습니다. 원활한 데이터 수집을 위해 Subscription 방식을 통해 데이터를 수집하는 것을 추천합니다.
	 * <p>
	 * https://www.sktiot.com/iot/developer/guide/guide/loRa/menu_04/page_04
	 *
	 * <pre><code>
	 * GET /0000000000000004/v1_0/remoteCSE-00000004702c1ffffe1d79da/container-LoRa/latest HTTP/1.1
	 * Host: thingplugtest.sktiot.com:9443
	 * X-M2M-RI: 00000004702c1ffffe1d79da_0002
	 * X-M2M-Origin: 00000004702c1ffffe1d79da
	 * ukey: dG5janFndjNibzZ**************ERjd296V0poMFY0TTJ4UQ==
	 * </code></pre>
	 *
	 * <pre><code>
	 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	 * <m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	 *     <ty>4</ty>
	 *     <ri>CI00000000000099029737</ri>
	 *     <rn>CI00000000000099029737</rn>
	 *     <pi>CT00000000000000006585</pi>
	 *     <ct>2017-12-14T15:18:27+09:00</ct>
	 *     <lt>2017-12-14T15:18:27+09:00</lt>
	 *     <et>2017-12-15T15:18:27+09:00</et>
	 *     <st>957</st>
	 *     <cr>RC00000000000000383679</cr>
	 *     <cnf>LoRa/Sensor</cnf>
	 *     <cs>20</cs>
	 *     <con>31323334353637383930</con>
	 * </m2m:cin>
	 * </code></pre>
	 * <p>
	 * 수신한 Response 중 <con> 필드에 포함된 데이터가 실제 LoRa 디바이스가 전송한 Uplink 데이터입니다.
	 * LoRa 디바이스가 데이터를 보낸 시간을 <ct> 필드로 확인할 수 있으며, <et> 필드의 시간이 되면 해당 데이터는 ThingPlug에서 삭제됩니다.
	 *
	 * @param ltid
	 * @param userKey 사용자 인증키
	 * @throws IOException
	 */
	public static @NotNull LatestDataResponse getLatestData(@NotNull Ltid ltid, @NotNull UserKey userKey)
			throws IOException, InterruptedException {
		GetLatestDataRequest api = new GetLatestDataRequest(ltid, userKey);
		HttpResponse<String> response
				= httpClient().send(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		return api.conv(response);
	}

	public static void getLatestData(@NotNull Ltid ltid, @NotNull UserKey userKey,
	                                 @NotNull Listener<LatestDataResponse> listener) {
		GetLatestDataRequest api = new GetLatestDataRequest(ltid, userKey);
		CompletableFuture<HttpResponse<String>> future
				= httpClient().sendAsync(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		future.thenApply(api::conv).thenAccept(listener::onCompleted);
		//listener.onCompleted(future.thenApply(api::conv).get(TIME_OUT, TimeUnit.SECONDS));
	}

	/**
	 * LoRa 디바이스마다 각각 한번씩 Subscription 요청을 해두면 LoRa 디바이스가 Uplink를 수행할 때마다 ThingPlug가 사용자의 App/Web 서버로 Uplink 데이터를 Push 해주도록 설정할 수 있습니다.
	 *
	 * @param subscription
	 * @param ltid
	 * @param userKey      사용자 인증키
	 * @throws IOException
	 */
	public static @NotNull CreateSubscriptionResponse createSubscription(@NotNull Subscription subscription, @NotNull Ltid ltid, @NotNull UserKey userKey)
			throws IOException, InterruptedException {
		CreateSubscriptionRequest api = new CreateSubscriptionRequest(subscription, ltid, userKey);
		HttpResponse<String> response
				= httpClient().send(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
		return api.conv(response);
	}

	public static void createSubscription(@NotNull Subscription subscription, @NotNull Ltid ltid, @NotNull UserKey userKey,
	                                      @NotNull Listener<CreateSubscriptionResponse> listener) {
		CreateSubscriptionRequest api = new CreateSubscriptionRequest(subscription, ltid, userKey);
		CompletableFuture<HttpResponse<String>> future
				= httpClient().sendAsync(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		future.thenApply(api::conv).thenAccept(listener::onCompleted);
		//listener.onCompleted(future.thenApply(api::conv).get(TIME_OUT, TimeUnit.SECONDS));
	}

	public static @NotNull RetrieveSubscriptionResponse retrieveSubscription(@NotNull Subscription subscription, @NotNull Ltid ltid, @NotNull UserKey userKey)
			throws IOException, InterruptedException {
		RetrieveSubscriptionRequest api = new RetrieveSubscriptionRequest(subscription, ltid, userKey);
		HttpResponse<String> response
				= httpClient().send(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		return api.conv(response);
	}

	public static void retrieveSubscription(@NotNull Subscription subscription, @NotNull Ltid ltid, @NotNull UserKey userKey,
	                                        @NotNull Listener<RetrieveSubscriptionResponse> listener) {
		RetrieveSubscriptionRequest api = new RetrieveSubscriptionRequest(subscription, ltid, userKey);
		CompletableFuture<HttpResponse<String>> future
				= httpClient().sendAsync(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		future.thenApply(api::conv).thenAccept(listener::onCompleted);
		//listener.onCompleted(future.thenApply(api::conv).get(TIME_OUT, TimeUnit.SECONDS));
	}

	public static @NotNull UpdateSubscriptionResponse updateSubscription(Subscription oldSub, Subscription newSub, Ltid ltid, UserKey userKey)
			throws IOException, InterruptedException {
		UpdateSubscriptionRequest api = new UpdateSubscriptionRequest(oldSub, newSub, ltid, userKey);
		HttpResponse<String> response
				= httpClient().send(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		return api.conv(response);
	}

	public static void updateSubscription(Subscription oldSub, Subscription newSub, Ltid ltid, UserKey userKey,
	                                      @NotNull Listener<UpdateSubscriptionResponse> listener) {
		UpdateSubscriptionRequest api = new UpdateSubscriptionRequest(oldSub, newSub, ltid, userKey);
		CompletableFuture<HttpResponse<String>> future
				= httpClient().sendAsync(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		future.thenApply(api::conv).thenAccept(listener::onCompleted);
		//listener.onCompleted(future.thenApply(api::conv).get(TIME_OUT, TimeUnit.SECONDS));
	}

	public static @NotNull DeleteSubscriptionResponse deleteSubscription(@NotNull Subscription subscription, @NotNull Ltid ltid, @NotNull UserKey userKey) throws IOException, InterruptedException {
		DeleteSubscriptionRequest api = new DeleteSubscriptionRequest(subscription, ltid, userKey);
		HttpResponse<String> response = httpClient().send(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
		return api.conv(response);
	}

	public static void deleteSubscription(@NotNull Subscription subscription, @NotNull Ltid ltid, @NotNull UserKey userKey,
	                                      @NotNull Listener<DeleteSubscriptionResponse> listener) {
		DeleteSubscriptionRequest api = new DeleteSubscriptionRequest(subscription, ltid, userKey);
		CompletableFuture<HttpResponse<String>> future
				= httpClient().sendAsync(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		future.thenApply(api::conv).thenAccept(listener::onCompleted);
		//listener.onCompleted(future.thenApply(api::conv).get(TIME_OUT, TimeUnit.SECONDS));
	}

	/**
	 * LoRa 디바이스를 리셋하기 위한 명령입니다. 이 명령은 오직 디바이스 리셋을 위해 사용하기 때문에 별도의 payload를 함께 보낼 수 없습니다.
	 *
	 * @param ltid
	 * @param userKey
	 * @throws IOException
	 */
	public static @NotNull DeviceResetResponse deviceReset(@NotNull Ltid ltid, @NotNull UserKey userKey)
			throws IOException, InterruptedException {
		DeviceResetRequest api = new DeviceResetRequest(ltid, userKey);
		HttpResponse<String> response
				= httpClient().send(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		return api.conv(response);
	}

	public static void deviceReset(@NotNull Ltid ltid, @NotNull UserKey userKey,
	                               @NotNull Listener<DeviceResetResponse> listener) {
		DeviceResetRequest api = new DeviceResetRequest(ltid, userKey);
		CompletableFuture<HttpResponse<String>> future
				= httpClient().sendAsync(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		future.thenApply(api::conv).thenAccept(listener::onCompleted);
		//listener.onCompleted(future.thenApply(api::conv).get(TIME_OUT, TimeUnit.SECONDS));
	}

	public static @NotNull DeviceResetResultResponse getDeviceResetResult(@NotNull Ltid ltid, @NotNull String resId, @NotNull UserKey userKey)
			throws IOException, InterruptedException {
		GetDeviceResetResultRequest api = new GetDeviceResetResultRequest(ltid, resId, userKey);
		HttpResponse<String> response
				= httpClient().send(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		return api.conv(response);
	}

	public static void getDeviceResetResult(@NotNull Ltid ltid, @NotNull String resId, @NotNull UserKey userKey,
	                                        @NotNull Listener<DeviceResetResultResponse> listener) {
		GetDeviceResetResultRequest api = new GetDeviceResetResultRequest(ltid, resId, userKey);
		CompletableFuture<HttpResponse<String>> future
				= httpClient().sendAsync(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		future.thenApply(api::conv).thenAccept(listener::onCompleted);
		//listener.onCompleted(future.thenApply(api::conv).get(TIME_OUT, TimeUnit.SECONDS));
	}

	public static @NotNull DeviceControlResponse deviceControl(@NotNull Ltid ltid, @NotNull UserKey userKey, @NotNull String hexStr)
			throws IOException, InterruptedException {
		DeviceControlRequest api = new DeviceControlRequest(ltid, userKey, hexStr);
		HttpResponse<String> response
				= httpClient().send(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		return api.conv(response);
	}

	public static void deviceControl(@NotNull Ltid ltid, @NotNull UserKey userKey, @NotNull String hexStr,
	                                 @NotNull Listener<DeviceControlResponse> listener) {
		DeviceControlRequest api = new DeviceControlRequest(ltid, userKey, hexStr);
		CompletableFuture<HttpResponse<String>> future
				= httpClient().sendAsync(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		future.thenApply(api::conv).thenAccept(listener::onCompleted);
		//listener.onCompleted(future.thenApply(api::conv).get(TIME_OUT, TimeUnit.SECONDS));
	}

	public static @NotNull DeviceControlResultResponse getDeviceControlResult(@NotNull Ltid ltid, @NotNull String resId, @NotNull UserKey userKey)
			throws IOException, InterruptedException {
		GetDeviceControlResultRequest api = new GetDeviceControlResultRequest(ltid, resId, userKey);
		HttpResponse<String> response
				= httpClient().send(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		return api.conv(response);
	}

	public static void getDeviceControlResult(@NotNull Ltid ltid, @NotNull String resId, @NotNull UserKey userKey,
	                                          @NotNull Listener<DeviceControlResultResponse> listener) {
		GetDeviceControlResultRequest api = new GetDeviceControlResultRequest(ltid, resId, userKey);
		CompletableFuture<HttpResponse<String>> future
				= httpClient().sendAsync(api.build(),
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		future.thenApply(api::conv).thenAccept(listener::onCompleted);
		//listener.onCompleted(future.thenApply(api::conv).get(TIME_OUT, TimeUnit.SECONDS));
	}

	public interface Listener<T extends IResponse> {
		void onCompleted(T response);
	}
}
