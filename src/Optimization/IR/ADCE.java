package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;

public class ADCE implements pass {
	private final IREntry programIREntry;

	public ADCE(IREntry programIREntry) {this.programIREntry = programIREntry;}

	@Override
	public boolean run() {
		return false;
	}
}
