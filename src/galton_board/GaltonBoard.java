package galton_board;

import java.util.Random;

public class GaltonBoard {
	private enum Direction { LEFT, RIGHT, NONE };
	
	private final double NORMAL = .5;
	private Random rng;
	private double biasJump, adj, drift, clamp;
	
	public GaltonBoard() {
		this(0.0, 0.00, 0, .5);
	}
	
	public GaltonBoard(double biasJump, double adjustment, double drift, double clamp) {
		this.biasJump = biasJump;
		adj = adjustment;
		this.drift = drift;
		this.clamp = clamp;
	}
		
	public int[] runBatch(int batchSize, int[] bins) {
		return simulateBoard(batchSize, bins);
	}
	
	private int[] simulateBoard(int balls, int[] bins) {
		rng = new Random();
		
		for(int i = 0; i < balls; i++) {
			int pos = simulateBall(bins.length - 1);
			bins[pos]++;
		}
		
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
				bias = base - biasJump * mult;
				prev = dir;
			}
			
			bias = Math.clamp(bias, -clamp, clamp);
			pos += (dir == Direction.LEFT) ? 0 : 1;
		}
		
		return pos;
	}
}
