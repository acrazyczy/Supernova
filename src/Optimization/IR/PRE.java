package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;

public class PRE implements pass {
	private final IREntry programIREntry;

	public PRE(IREntry programIREntry) {this.programIREntry = programIREntry;}

	@Override
	public boolean run() {
		return false;
	}
}
