package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;

public class LICO implements pass {
	private final IREntry programIREntry;

	public LICO(IREntry programIREntry) {this.programIREntry = programIREntry;}

	@Override
	public boolean run() {
		return false;
	}
}
