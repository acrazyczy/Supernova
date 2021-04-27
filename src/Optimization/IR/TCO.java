package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;

public class TCO implements pass {
	private final IREntry programIREntry;

	public TCO(IREntry programIREntry) {this.programIREntry = programIREntry;}

	@Override
	public boolean run() {
		return false;
	}
}
