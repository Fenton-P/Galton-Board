package display;

import java.awt.*;
import javax.swing.*;

public class LinePlot extends JPanel {
	private static final long serialVersionUID = 4823746695441541436L;
	
	private static final String WINDOW_MSG = "Please Expand Window, Currently Too Small";
	
	private int min, max, padding, markCnt, barPad;
	private int[] currData;
	
	public LinePlot(int min, int max) {
		this.min = min;
		this.max = max;
		
		padding = 40;
		markCnt = 10;
		barPad = 10;
		
		setPreferredSize(new Dimension(800, 450));
		setOpaque(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D paint = (Graphics2D) g;
		
		int diff = max - min;
		if (currData == null || diff == 0) return;
		
		int width = getWidth();
		int height = getHeight();
		
		if (width < 400 || height < 275) {
			paint.drawString(WINDOW_MSG, width / 2 - 117, height / 2 + 5);
			
			return;
		}
		
		int sectionWidth = width / (diff + 1);
		int jump = (height - padding * 2) / markCnt;
		int markJmp = 100 / markCnt;
		
		paint.drawRect(padding, padding, width - padding * 2, height - padding * 2);
		
		for (int i = 0; i < markCnt + 1; i++) {
			paint.drawString(i * markJmp + "%", padding - 35, height - padding - jump * i + 5);
		}
		
		int sum = 0;
		for (int cnt : currData) {
			sum += cnt;
		}
		
		paint.drawString("Bin #:", padding - 10, height - padding + 15);
		for (int i = 0; i < diff; i++) {
			paint.drawString("#" + (i + min), padding + (int) (sectionWidth * (i + .5)) - 10, height - padding + 15);
		
			double percentage = currData[i + min] / (double) sum;
			int barHeight = (int) (percentage * (height - padding * 2));
			
			System.out.println(currData[i + min]);
			paint.fillRect(padding + i * sectionWidth + barPad, height - barHeight - padding, sectionWidth - barPad * 2, barHeight);
		}
	}
	
	public boolean updateComponent(int[] bins) {
		if (!validate(bins) || min >= max) return false;
		
		currData = bins;
		
		SwingUtilities.invokeLater(this::repaint);
		return true;
	}
	
	public boolean validate(int[] bins) {
		return bins != null && bins.length >= max;
	}
}
