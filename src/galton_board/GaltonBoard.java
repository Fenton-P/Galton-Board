package galton_board;

import java.util.Random;

public class GaltonBoard {
	private enum Direction { LEFT, RIGHT, NONE };
	
	private final int HEIGHT;
	private final double NORMAL = .5;
	private Random rng;
	private double biasJump, adj, drift, clamp;
	
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
	
	/*
	 * Simulates a Ball Going Through a Galton Board
	 * 
	 * The ball chooses to go left or right depending on a random value
	 * and a bias. If the ball switches directions, the likelihood of
	 * the ball continuing in that same direction is set to NORMAL +
	 * biasJump. Otherwise, the likelihood of switching directions
	 * increases.
	 * 
	 * The drift variable alters the general odds of the ball going left or
	 * right. A positive drift value means higher chance of going right.
	 * 
	 * */
	private int simulateBall(int height) {
		Direction prev = Direction.NONE;
		int pos = 0;
		double bias = 0, base = NORMAL - drift;
		
		
		for (int i = 0; i < height; i++) {
			Direction dir = (rng.nextDouble() + bias < base) ? Direction.LEFT : Direction.RIGHT;
			int mult = dir == Direction.RIGHT ? 1 : -1;
			bias -= adj * mult;
			
			if (prev != dir) {
				bias = NORMAL + biasJump * mult;
				prev = dir;
			}
			
			bias = Math.clamp(bias, -clamp, clamp);
			pos += (dir == Direction.LEFT) ? 0 : 1;
		}
		
		return pos;
	}
}
