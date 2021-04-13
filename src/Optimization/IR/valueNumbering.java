package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;

public class valueNumbering implements pass {
	private final IREntry programIREntry;

	public valueNumbering(IREntry programIREntry) {this.programIREntry = programIREntry;}

	@Override
	public boolean run() {
		return false;
	}
}
