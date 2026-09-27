package display;

import java.awt.*;

import javax.swing.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import aws.Input;
import galton_board.GaltonBoard;

public class SimulationPanel extends JPanel {
	private static final long serialVersionUID = -2346382374587904033L;
	
	private GaltonBoard board;
	private LinePlot visualization;
	private InputPanel inputPanel;
	private int[] bins;
	
	private Thread batchThread;
	private int tempBatchSize, batchCount;
	private long batchDelay;

	public SimulationPanel(GaltonBoard board, LinePlot visualization) {
		this.board = board;
		this.visualization = visualization;
		
		inputPanel = new InputPanel(this);
		
		JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, visualization, inputPanel);
		splitPanel.setContinuousLayout(true);
		
		this.setLayout(new BorderLayout());
		this.add(splitPanel, BorderLayout.CENTER);
		
		this.setOpaque(true);
	}
	
	public void initBoardStats(int height) {
		bins = new int[height];
		
		inputPanel.initInputPanel();
	}
	
	public void runBatch(int batchSize) {
		bins = board.runBatch(batchSize, bins);
		visualization.updateComponent(bins);
	}
	
	public void runBatches(int batchSize, int cnt, long delay) {
		tempBatchSize = batchSize;
		batchCount = cnt;
		batchDelay = delay;
		
		if(batchThread != null) return;
		
		batchThread = new Thread(this::batchRunner);
		batchThread.start();
	}
	
	private void batchRunner() {
		for(int i = 0;i < batchCount && batchThread != null;i++) {
			System.out.println("BATCH");
			runBatch(tempBatchSize);
			
			try {
				Thread.sleep(batchDelay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		batchThread = null;
	}
	
	public void setBoard(GaltonBoard b) {
		board = b;
	}
	
	public void setGraph(LinePlot l) {
		visualization.setTo(l);
	}
	
	public String[] getBoardInfo() {
		if(bins == null) return new String[9];
		return new String[] { tempBatchSize + "", 
					          bins.length + "",
					          board.getBiasJump() + "",
					          board.getBiasAdj() + "",
					          board.getDrift() + "",
					          board.getClamp() + "",
					          visualization.getStart() + "",
					          visualization.getEnd() + "",
					          batchCount + "" };
	}
	
	public void setBins(int cnt) {
		bins = new int[cnt];
	}
	
	public void runAWSBatches(int batchSize, int count) {
		Input in = board.getInput(batchSize, bins);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		
		try {
			json = mapper.writeValueAsString(in);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LambdaClient client = LambdaClient.create();
		
		InvokeRequest req = InvokeRequest.builder()
			    .functionName("GaltonBoardLambda")
			    .payload(SdkBytes.fromUtf8String(json))
			    .build();
	}
}
