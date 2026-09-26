package galton_board;

import java.util.Random;

public class GaltonBoard {
	private final int HEIGHT;
	private Random rng;
	
	public GaltonBoard(int height) {
		HEIGHT = height;
	}
	
	public void runBatch(int batchSize) {
		simulateBoard(batchSize, HEIGHT);
	}
	
	private int[] simulateBoard(int balls, int height) {
		int[] bins = new int[height + 1];
		rng = new Random();
		
		return bins;
	}
	
	private int simulateBall(int height) {
		int pos = 0, bias = 0;
		
		
		for (int i = 0; i < height; i++) {
			
		}
	}
}
