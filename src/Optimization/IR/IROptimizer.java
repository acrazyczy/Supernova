package Optimization.IR;

import LLVMIR.IREntry;

public class IROptimizer {
	private final IREntry programIREntry;

	public IROptimizer(IREntry programIREntry) {this.programIREntry = programIREntry;}

	public void run() {
		boolean flag;
		do {
			flag = new SCCP(programIREntry).run();
			flag |= new ADCE(programIREntry).run();
			flag |= new OSR(programIREntry).run();
//			break;
		} while (flag);
	}
}
