package galton_board;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
		
		ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor();
		ArrayList<Future<int[]>> futures = new ArrayList<>();
		
		for(int i = 0;i<req.cnt;i++) {
			futures.add(exec.submit(() -> {
		        InvokeResponse res = client.invoke(lambdaRequest);
		        String responseJson = res.payload().asUtf8String();
		        return mapper.readValue(responseJson, int[].class);
		    }));
		}
		
		int[] bins = new int[req.bin];
		
		for(Future<int[]> f : futures) {
			try {
				int[] bin =  f.get();
				for(int i = 0;i<bins.length;i++) {
					bins[i] += bin[i];
				}
			} catch (InterruptedException | ExecutionException e) {
				return;
			}
		}
		
		onComplete.update(bins);
	}
}
