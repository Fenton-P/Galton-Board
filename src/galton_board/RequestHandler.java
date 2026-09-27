package galton_board;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicIntegerArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import aws.Input;
import display.*;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

public class RequestHandler {
	private SimulationPanel simulation;
	private Thread requestThread;
	private Request currRequest, nextRequest;
	private RequestResponse onComplete, onCompleteNext;
	private RequestStart onStart, onStartNext;
	
	public RequestHandler(SimulationPanel sim) {
		simulation = sim;
		
		if (simulation == null) {
			throw new NullPointerException();
		}
	}
	
	/*
	 * Will create a new thread that will wait until it gets
	 * the key associated with this class. When it has that
	 * key it will then set the next request to the request that was sent.
	 * If the requestThread is null, it makes a new one and looks for 
	 * the next Request in order to start handling it. To handle a request
	 * it simply follows the protocols laid out in the helper functions.
	 * 
	 * */
	public void handleRequest(Request req, RequestStart onStart, RequestResponse runBatch) {
		new Thread(() -> {
			synchronized (this) {
				nextRequest = req;
				onCompleteNext = runBatch;
				onStartNext = onStart;
			}
			
			if (requestThread == null) {
				requestThread = new Thread(this::handleRequests);
				requestThread.start();
			} else {
				requestThread.interrupt();
			}
		}).start();
	}
	
	private void handleRequests() {
		while (nextRequest != null) {
			synchronized (this) {
				currRequest = nextRequest;
				onComplete = onCompleteNext;
				onStart = onStartNext;
				nextRequest = null;
				onCompleteNext = null;
				onStartNext = null;
			}
			
			onStart.onStart(currRequest);
			
			makeRequest();
		}
		
		requestThread = null;
	}
	
	private void makeRequest() {	
		switch (currRequest.mde) {
			case "CPU" -> handleCPUReq(currRequest);
			case "AWS" -> handleAWSReq(currRequest);
		}
	}
	
	private void handleCPUReq(Request req) {
		int[] bins = new int[req.bin];
		
		for (int i = 0; i < req.cnt; i++) {
			if (Thread.interrupted()) {
				return;
			}
			
			bins = simulation.getBoard().runBatch(req.sze, bins);
			onComplete.update(bins);
			
			try {
				Thread.sleep(req.dur);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	private void handleAWSReq(Request req) {
		Input in = req.getInput();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		
		try {
			json = mapper.writeValueAsString(in);
		} catch (JsonProcessingException e) {
			System.out.println("Could not convert to JSON");
			return;
		}
		
		LambdaClient client = LambdaClient.builder()
			    .region(Region.US_EAST_2)
			    .httpClientBuilder(UrlConnectionHttpClient.builder())
			    .build();
		
		InvokeRequest lambdaRequest = InvokeRequest.builder()
			    .functionName("runGaltonBoardBatch")
			    .payload(SdkBytes.fromUtf8String(json))
			    .build();
		
		ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();
		
		AtomicIntegerArray bins = new AtomicIntegerArray(req.bin);
		
		for(int i = 0;i<req.cnt;i++) {
			futures.add(CompletableFuture.runAsync(() -> {
		        InvokeResponse res = client.invoke(lambdaRequest);
		        String responseJson = res.payload().asUtf8String();
		        int[] bin;
				try {
					bin = mapper.readValue(responseJson, int[].class);
				} catch (JsonProcessingException e) {
					return;
				}

		        for (int j = 0; j < bin.length; j++) {
		            bins.addAndGet(j, bin[j]);
		        }
		    }));
		}
		
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
		
		int[] finalBins = new int[bins.length()];
		
		for (int i = 0; i < bins.length(); i++) {
		    finalBins[i] = bins.get(i);
		}

		onComplete.update(finalBins);
	}
}
