package iThink.Automation.utils;

import java.util.ArrayList;
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



import org.openqa.selenium.devtools.v138.network.model.RequestId;

public class DevToolsManager {

	private DevTools devTools;
	private List<Request> requests = new ArrayList<>();
	private List<Response> responses = new ArrayList<>();
	private final Map<String, RequestId> urlToRequestId = new HashMap<>();
	private boolean capturing = false;
	private static final Logger logger = LogManager.getLogger(DevTools.class);
	
	public DevToolsManager(WebDriver driver) {
		devTools = ((HasDevTools) driver).getDevTools();
		devTools.createSession();
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(false)));
		
	}
	
	public void startCapture() {
		if(capturing) return;
		
		requests.clear();
		responses.clear();
		urlToRequestId.clear();
		
		capturing = true;
		
		//get the request
		devTools.addListener(Network.requestWillBeSent(), e ->{
			if(!capturing) return;
			Request req = e.getRequest();
			requests.add(req);
			System.out.println("Request : " + req.getUrl());
		});
		
		//get the response
		devTools.addListener(Network.responseReceived(), e -> {
			if(!capturing) return;
			Response resp = e.getResponse();
			responses.add(resp);
			System.out.println("Response : "+ resp.getUrl());
		});
		
		logger.info("Network Capturing started...");
	}
	
	public void stopCapture() {
		capturing = false;
		logger.info("Network captures stops!");
	}
	
	/**Check desired API was called**/
	public boolean wasAPICalled(String endpoint) {
		return requests.stream().anyMatch(req -> req.getUrl().contains(endpoint));
	}
	
	/** Print Response**/
	public void printAPIResponseStatus(String endpoint) {
		responses.stream()
		.filter(res -> res.getUrl().contains(endpoint))
		.forEach(res -> System.out.println(res.getUrl() + " ->" + res.getStatus()));
	}
	
	/**Get Response Body from Request ID**/
	public Optional<String> getResponseBody(String urlPart) {
		for(Response resp : responses) {
			if(resp.getUrl().contains(urlPart)) {
				RequestId reqId = urlToRequestId.get(resp.getUrl());
				if(reqId != null) {
					try {
						var body = devTools.send(Network.getResponseBody(reqId));
						return Optional.of(body.getBody());
					} catch (Exception e) {
						 System.out.println("Could not get response body for: " + resp.getUrl());
					}
				}
			}
		}
		return Optional.empty();
	}
}
