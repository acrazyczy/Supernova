package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;

public class GVN implements pass {
	private final IREntry programIREntry;

	public GVN(IREntry programIREntry) {this.programIREntry = programIREntry;}

	@Override
	public boolean run() {
		return false;
	}
}