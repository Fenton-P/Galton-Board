package galton_board;

@FunctionalInterface
public interface RequestResponse {
	public int[] runBatch(int count, int[] bins);
}
