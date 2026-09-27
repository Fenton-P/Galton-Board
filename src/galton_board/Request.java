package galton_board;

import aws.Input;

public class Request {
	public int beg, end, cnt, sze, bin;
	public double jmp, adj, dft, cmp;
	public String mde;
	public long dur;
	
	public static Request parseRequest(String[] info) throws IllegalArgumentException {
		Request req = new Request();
		
		try {
			req.jmp = Double.parseDouble(info[2]);
			req.adj = Double.parseDouble(info[3]);
			req.dft = Double.parseDouble(info[4]);
			req.cmp = Double.parseDouble(info[5]);
			req.beg = Integer.parseInt(info[6]);
			req.end = Integer.parseInt(info[7]);
			req.cnt = Integer.parseInt(info[8]);
			req.sze = Integer.parseInt(info[0]);
			req.bin = Integer.parseInt(info[1]);
			req.dur = Long.parseLong(info[10]);
			req.mde = info[9];
		} catch (NumberFormatException | NullPointerException numExcept) {
			throw new IllegalArgumentException("Could not parse information");
		}
		
		if (!req.validate()) {
			throw new IllegalArgumentException("Illegal Arguement(s)");
		}
		
		return req;
	}
	
	public boolean validate() {
		if (!mde.equals("AWS") && !mde.equals("CPU")) {
			return false;
		}
		
		if (beg < 0 || end <= beg || end > bin) {
			return false;
		}
		
		return true;
	}
	
	public Input getInput() {
		Input in = new Input();
		
		in.biasJump = jmp;
		in.adj = adj;
		in.drift = dft;
		in.clamp = cmp;
		in.bins = bin;
		in.balls = sze;
		
		return in;
	}
}
