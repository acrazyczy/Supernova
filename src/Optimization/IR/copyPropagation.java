package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;

public class copyPropagation implements pass {
	private final IREntry programIREntry;

	public copyPropagation(IREntry programIREntry) {this.programIREntry = programIREntry;}

	@Override
	public boolean run() {
		return false;
	}
}
