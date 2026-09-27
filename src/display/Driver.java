package display;

import javax.swing.JFrame;

import galton_board.GaltonBoard;

public class Driver {

	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("Galton Board Simulation");
		LinePlot visualization = new LinePlot(0,10);
		GaltonBoard board = new GaltonBoard();
		
		SimulationPanel simulationPanel = new SimulationPanel(board, visualization);
		simulationPanel.initBoardStats(10);
		
		mainFrame.add(simulationPanel);
		mainFrame.pack();
		
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
}
