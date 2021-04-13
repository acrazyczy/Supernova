package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;

public class aliasOptimizer implements pass {
	private final IREntry programIREntry;

	public aliasOptimizer(IREntry programIREntry) {this.programIREntry = programIREntry;}

	@Override
	public boolean run() {
		return false;
	}
}
