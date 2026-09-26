package aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import galton_board.GaltonBoard;

public class AWSHandler implements RequestHandler<Input, int[]> {
	@Override
	public int[] handleRequest(Input in, Context ctx) {
		return GaltonBoard.runBatchAWS(in);
	}
}
