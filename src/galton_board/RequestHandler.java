package galton_board;

import display.*;

public class RequestHandler {
	private SimulationPanel simulation;
	private Thread requestThread;
	private Request currRequest, nextRequest;
	private RequestResponse onComplete, onCompleteNext;
	
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
	public void handleRequest(Request req, RequestResponse onComplete) {
		nextRequest = req;
		onCompleteNext = onComplete;
		
		if (requestThread == null) {
			requestThread = new Thread(this::handleRequests);
			requestThread.start();
		}
	}
	
	private void handleRequests() {
		while (nextRequest != null) {
			currRequest = nextRequest;
			onComplete = onCompleteNext;
			nextRequest = null;
			onCompleteNext = null;
			
			
			synchronized (simulation) {
				simulation.setBoard(new GaltonBoard(currRequest.jmp, currRequest.adj,
						 							currRequest.dft, currRequest.cmp));
				simulation.setGraph(new LinePlot(currRequest.beg, currRequest.end));
				simulation.setBins(currRequest.bin);
				
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
			
			bins = simulation.runBatch(req.sze, bins);
			onComplete.response(bins);
			
			try {
				Thread.sleep(req.dur);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleAWSReq(Request req) {
		System.out.println("Make AWS Request");
	}
}
