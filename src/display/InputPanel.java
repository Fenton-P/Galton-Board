package display;

import java.awt.Dimension;

import javax.swing.*;

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
									 "Enter Mode: "};
	private String[] inputInfo;
	
	public InputPanel(SimulationPanel sim) {
		this.sim = sim;
		inputInfo = new String[inputs.length];
		
		int[] boardInfo = sim.getBoardInfo();
		
		setPreferredSize(new Dimension(300, 450));
		setMinimumSize(new Dimension(200, 200));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		addPanelStructure();
	}
	
	private void addPanelStructure() {
		TextInputPanel[] inputPanels = new TextInputPanel[inputs.length];
		
		for(int i = 0;i < inputs.length;i++) {
			inputPanels[i] = new TextInputPanel(inputs[i], inputInfo[i]);
			add(inputPanels[i]);
		}
		
		JButton update = new JButton("Update");
		update.setSize(new Dimension(150, 75));
		add(update);
		
		update.addActionListener(e -> {
			for(int i = 0;i<inputs.length;i++) {
				inputInfo[i] = inputPanels[i].getText();
			}
			
			double jmp = Double.parseDouble(inputInfo[2]),
				   adj = Double.parseDouble(inputInfo[3]),
				   dft = Double.parseDouble(inputInfo[4]),
				   cmp = Double.parseDouble(inputInfo[5]);
			int    beg = Integer.parseInt(inputInfo[6]),
				   end = Integer.parseInt(inputInfo[7]),
				   cnt = Integer.parseInt(inputInfo[8]),
				   sze = Integer.parseInt(inputInfo[0]);
			String mde = inputInfo[9];
			
			sim.setBoard(new GaltonBoard(jmp, adj, dft, cmp));
			sim.setGraph(new LinePlot(beg, end));
			
			sim.runBatches(sze, cnt, 100);
		});
	}
}
