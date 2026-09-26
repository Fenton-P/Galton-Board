package display;

import java.awt.*;

import javax.swing.*;

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
	}
	
	public void runBatch(int batchSize) {
		bins = board.runBatch(batchSize, bins);
		visualization.updateComponent(bins);
	}
	
	public void runBatches(int batchSize, int cnt, long delay) {
		tempBatchSize = batchSize;
		batchCount = cnt;
		batchDelay = delay;
		
		batchThread = new Thread(this::batchRunner);
		batchThread.start();
	}
	
	private void batchRunner() {
		for(int i = 0;i < batchCount && batchThread != null;i++) {
			runBatch(tempBatchSize);
			
			try {
				Thread.sleep(batchDelay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setBoard(GaltonBoard b) {
		board = b;
	}
	
	public void setGraph(LinePlot l) {
		visualization = l;
	}
	/*
	 * "Enter Batch Size: ",
									 "Enter Bin Count: ",
									 "Enter Bias Jump: ",
									 "Enter Bias Adjustment: ",
									 "Enter General Drift: ",
									 "Enter Bias Clamp: ",
									 "Enter Graph Start: ",
									 "Enter Graph End: ",
									 "Enter Batch Count: ",
									 "Enter Mode: "};*/
	public int[] getBoardInfo() {
		return new int[] {};
		//return new int[] {tempBatchSize, bins.length, board.getBiasJump(), board.get)};
	}
}
