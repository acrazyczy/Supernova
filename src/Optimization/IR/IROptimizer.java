package Optimization.IR;

import LLVMIR.IREntry;

public class IROptimizer {
	private final IREntry programIREntry;

	public IROptimizer(IREntry programIREntry) {this.programIREntry = programIREntry;}

	public void run(boolean isSSAForm, boolean inlineFlag) {
		boolean flag;
		do {
			flag = isSSAForm && new SCCP(programIREntry).run();
			flag |= isSSAForm && new ADCE(programIREntry).run();
			flag |= isSSAForm && new OSR(programIREntry).run();
			flag |= new CFGSimplifier(programIREntry).run();
		} while (flag);
		if (inlineFlag) {
			new inlineExpansion(programIREntry).run();
			do {
//				flag = isSSAForm && new SCCP(programIREntry).run();
//				flag = isSSAForm && new ADCE(programIREntry).run();
//				flag |= isSSAForm && new OSR(programIREntry).run();
//				flag |= new CFGSimplifier(programIREntry).run();
			} while (flag);
		}
	}
}
