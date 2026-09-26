package display;

import javax.swing.JFrame;

import galton_board.GaltonBoard;

public class Driver {

	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("Galton Board Simulation");
		LinePlot visualization = new LinePlot(0,10);
		
		mainFrame.add(visualization);
		mainFrame.pack();
		
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		
		GaltonBoard board = new GaltonBoard();
		int[] bins = board.runBatch(10000, new int[10]);
		
		for(int b : bins) {
			System.out.print(b + ", ");
		}
		System.out.print("\n");
		
		visualization.updateComponent(bins);
	}

}
