package iThink.Automation.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v138.network.Network;
import org.openqa.selenium.devtools.v138.network.model.Request;
import org.openqa.selenium.devtools.v138.network.model.Response;

import org.json.JSONObject;

import org.openqa.selenium.devtools.v138.network.model.RequestId;

public class DevToolsManager {

	private DevTools devTools;
	private List<Request> requests = new ArrayList<>();
	private List<Response> responses = new ArrayList<>();
	private final Map<String, RequestId> urlToRequestId = new HashMap<>();
	private final Map<RequestId, String> requestMethods = new HashMap<>();
	private boolean capturing = false;

	private static final Logger logger = LogManager.getLogger(DevTools.class);

	private static final List<String> STATIC_EXTENSIONS = Arrays.asList(".png", ".jpg", ".jpeg", ".gif", ".svg",
			".webp", ".css", ".js", ".ico", ".woff", ".woff2", ".ttf", ".map");

	public DevToolsManager(WebDriver driver) {
		devTools = ((HasDevTools) driver).getDevTools();
		devTools.createSession();
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(false)));

	}

	public void startCapture() {
		if (capturing)
			return;

		clearCaptureData();
		capturing = true;

		// get the request
		devTools.addListener(Network.requestWillBeSent(), e -> {
			if (!capturing)
				return;
			Request req = e.getRequest();
			String url = req.getUrl();

			// skip unnecessary calls
			if (isStaticAsset(url))
				return;

			requests.add(req);
			requestMethods.put(e.getRequestId(), req.getMethod());
			urlToRequestId.put(url, e.getRequestId());

			logger.debug("Request : {}", req.getMethod(), url);
			System.out.println("Request: " + req.getMethod() + " " + req.getUrl());
		});

		// get the response
		devTools.addListener(Network.responseReceived(), e -> {
			if (!capturing)
				return;

			// skip preflight api calls
			String method = requestMethods.getOrDefault(e.getRequestId(), "");
			String url = e.getResponse().getUrl();

			if ("Options".equalsIgnoreCase(method) || isStaticAsset(url))
				return; // skip CORS Preflight.

			Response resp = e.getResponse();
			responses.add(resp);
			urlToRequestId.put(resp.getUrl(), e.getRequestId());
			logger.debug("Response : {} -> {}", url, resp.getStatus());
		});

		logger.info("Network Capturing started...");
	}

	public void stopCapture() {
		capturing = false;
		logger.info("Network captures stopped!");
	}

	/** Check desired API was called **/
	public boolean wasAPICalled(String endpoint) {
		return requests.stream().anyMatch(req -> !req.getMethod().equalsIgnoreCase("OPTIONS")
				&& req.getUrl().contains(endpoint) && !isStaticAsset(req.getUrl()));
	}

	/** Print Response **/
	public void printAPIResponseStatus(String endpoint) {
		responses.stream().filter(res -> res.getUrl().contains(endpoint))
				.forEach(res -> logger.info("API: {} -> {}", res.getUrl(), res.getStatus()));
	}

	/** Get Response Body from Request ID **/
	public Optional<String> getResponseBody(String urlPart) {
		for (Response resp : responses) {
			if (resp.getUrl().contains(urlPart)) {
				RequestId reqId = urlToRequestId.get(resp.getUrl());
				if (reqId != null) {
					try {
						var body = devTools.send(Network.getResponseBody(reqId));
						String responseBody = body.getBody();
						logger.info("Response Body for {} -> {}", resp.getUrl(), responseBody);
						return Optional.ofNullable(responseBody);
					} catch (Exception e) {
						logger.warn("Could not get response body for: {}", resp.getUrl(), e);
					}
				}
			}
		}
		return Optional.empty();
	}

	/** Extract JSON field from API Response **/
	public Optional<String> getJSONFieldFromResponse(String urlPart, String key) {
		return getResponseBody(urlPart).map(body -> {
			try {
				JSONObject json = new JSONObject(body);
				return json.optString(key, null);
			} catch (Exception e) {
				logger.warn("Failed to Parse JSON field '{}' from the response: {}", key, body);
				return null;
			}
		});
	}

	public void clearCaptureData() {
		requests.clear();
		responses.clear();
		urlToRequestId.clear();

	}

	public List<Response> getResponses() {
		return Collections.unmodifiableList(responses);
	}

	/** Utility: check if URL is for a static asset */
	private boolean isStaticAsset(String url) {
		return STATIC_EXTENSIONS.stream().anyMatch(url::endsWith);
	}
}
