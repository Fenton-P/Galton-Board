package galton_board;

@FunctionalInterface
public interface RequestResponse {
	public void runBatch(int count, int[] bins);
}
