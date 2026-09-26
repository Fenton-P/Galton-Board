package display;

import javax.swing.JPanel;

import galton_board.GaltonBoard;

public class SimulationPanel extends JPanel {
	private static final long serialVersionUID = -2346382374587904033L;

	private GaltonBoard board;
	private LinePlot visualization;
	private int[] bins;
	
	private Thread batchThread;
	private int tempBatchSize, batchCount;
	private long batchDelay;
	
	public SimulationPanel(GaltonBoard board, LinePlot visualization) {
		this.board = board;
		this.visualization = visualization;
		
		this.add(visualization);
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
}
