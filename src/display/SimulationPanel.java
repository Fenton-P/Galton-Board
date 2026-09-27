package display;

import java.awt.*;

import javax.swing.*;

import galton_board.*;

public class SimulationPanel extends JPanel {
	private static final long serialVersionUID = -2346382374587904033L;
	
	private GaltonBoard board;
	private LinePlot visualization;
	private InputPanel inputPanel;
	private int[] bins;
	
	private RequestHandler requestHandler;
	private int tempBatchSize, batchCount;

	public SimulationPanel(GaltonBoard board, LinePlot visualization) {
		this.board = board;
		this.visualization = visualization;
		
		inputPanel = new InputPanel(this);
		requestHandler = new RequestHandler(this);
		
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
	
	public void runBatch(Request batchReq) {
		requestHandler.handleRequest(batchReq, this::initNewView, this::updateView);
	}
	
	private void initNewView(Request req) {
		setBoard(new GaltonBoard(req.jmp, req.adj,
								 req.dft, req.cmp));
		setGraph(new LinePlot(req.beg, req.end));
		setBins(req.bin);
	}
	
	private void updateView(int[] bins) {
		visualization.updateComponent(bins);
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
	
	public GaltonBoard getBoard() {
		return board;
	}
	
	public void setBins(int cnt) {
		bins = new int[cnt];
	}
}
