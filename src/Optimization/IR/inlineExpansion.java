package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;

public class inlineExpansion implements pass {
	private final IREntry programIREntry;

	public inlineExpansion(IREntry programIREntry) {this.programIREntry = programIREntry;}

	@Override
	public boolean run() {
		return false;
	}
}
