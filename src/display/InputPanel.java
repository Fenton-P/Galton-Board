package display;

import java.awt.Dimension;

import javax.swing.*;

import galton_board.GaltonBoard;

public class InputPanel extends JPanel {
	private static final long serialVersionUID = 4084662184640270100L;

	private GaltonBoard board;
	private LinePlot graph;
	
	public InputPanel(GaltonBoard board, LinePlot graph) {
		this.board = board;
		this.graph = graph;
		
		setPreferredSize(new Dimension(300, 450));
		
		JButton update = new JButton("Update");
		update.setSize(new Dimension(150, 75));
		this.add(update);
	}
}
