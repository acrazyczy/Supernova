package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;

public class LICM implements pass {
	private final IREntry programIREntry;

	public LICM(IREntry programIREntry) {this.programIREntry = programIREntry;}

	@Override
	public boolean run() {
		return false;
	}
}
