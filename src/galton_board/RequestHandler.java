package galton_board;

import display.*;

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
	 * the key associated with the board class. When it has that
	 * key it will then set the re
	 * 
	 * */
	public void handleRequest(Request req, RequestStart onStart, RequestResponse runBatch) {
		nextRequest = req;
		onCompleteNext = runBatch;
		onStartNext = onStart;
		
		if (requestThread == null) {
			requestThread = new Thread(this::handleRequests);
			requestThread.start();
		} else {
			requestThread.interrupt();
		}
	}
	
	private void handleRequests() {
		while (nextRequest != null) {
			currRequest = nextRequest;
			onComplete = onCompleteNext;
			onStart = onStartNext;
			nextRequest = null;
			onCompleteNext = null;
			onStartNext = null;
			
			
			synchronized (simulation) {
				onStart.onStart(currRequest);
				
				makeRequest();
			}
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
			
			bins = onComplete.runBatch(req.sze, bins);
			
			try {
				Thread.sleep(req.dur);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	private void handleAWSReq(Request req) {
		System.out.println("Make AWS Request");
	}
}
