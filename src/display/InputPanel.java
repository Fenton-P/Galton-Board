package display;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.*;

import galton_board.Request;
import galton_board.GaltonBoard;

public class InputPanel extends JPanel {
	private static final long serialVersionUID = 4084662184640270100L;
	
	private SimulationPanel sim;
	
	private final String[] inputs = {"Enter Batch Size: ",
									 "Enter Bin Count: ",
									 "Enter Bias Jump: ",
									 "Enter Bias Adjustment: ",
									 "Enter General Drift: ",
									 "Enter Bias Clamp: ",
									 "Enter Graph Start: ",
									 "Enter Graph End: ",
									 "Enter Batch Count: ",
									 "Enter Mode: ",
									 "Enter CPU Delay: "};
	private String[] inputInfo;
	private TextInputPanel[] inputPanels;
	
	public InputPanel(SimulationPanel sim) {
		this.sim = sim;
		inputInfo = new String[inputs.length];
		
		setPreferredSize(new Dimension(300, 450));
		setMinimumSize(new Dimension(150, 200));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void initInputPanel() {
		String[] info = sim.getBoardInfo();
		System.arraycopy(info, 0, inputInfo, 0, info.length);
		inputInfo[9] = "CPU";
		
		addPanelStructure();
	}
	
	private void addPanelStructure() {
		inputPanels = new TextInputPanel[inputs.length];
		
		for(int i = 0;i < inputs.length;i++) {
			inputPanels[i] = new TextInputPanel(inputs[i], inputInfo[i]);
			add(inputPanels[i]);
		}
		
		JButton update = new JButton("Update");
		update.setSize(new Dimension(150, 75));
		add(update);
		
		update.addActionListener(this::handleClick);
	}
	
	private void updateInputInfo() {
		for(int i = 0; i < inputInfo.length; i++) {
			inputInfo[i] = inputPanels[i].getText();
		}
	}
	
	private void handleClick(ActionEvent e) {
		updateInputInfo();
		
		Request req = Request.parseRequest(inputInfo);
		
		sim.runBatch(req);
		
//		sim.setBoard(new GaltonBoard(jmp, adj, dft, cmp));
//		sim.setGraph(new LinePlot(beg, end));
//		sim.setBins(bin);
//		
//		switch(mde) {
//		case "CPU":
//			sim.runBatches(sze, cnt, 100);
//			break;
//		case "AWS":
//			sim.runAWSBatches(sze, cnt);
//		}
	}
}
