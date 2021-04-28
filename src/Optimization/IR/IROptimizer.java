package Optimization.IR;

import LLVMIR.IREntry;

public class IROptimizer {
	private final IREntry programIREntry;

	public IROptimizer(IREntry programIREntry) {this.programIREntry = programIREntry;}

	public void normalOptimization(boolean isSSAForm) {
		boolean flag;
		do {
			flag = isSSAForm && new SCCP(programIREntry).run();
			flag |= isSSAForm && new ADCE(programIREntry).run();
			flag |= new CSE(programIREntry).run();
			flag |= isSSAForm && new TCO(programIREntry).run();
			flag |= isSSAForm && new OSR(programIREntry).run();
			flag |= new CFGSimplifier(programIREntry).run();
			flag |= isSSAForm && new inlineExpansion(programIREntry, false).run();
		} while (flag);
	}

	public void run(boolean isSSAForm) {
		normalOptimization(isSSAForm);
		if (isSSAForm) new inlineExpansion(programIREntry, true).run();
		normalOptimization(isSSAForm);
	}
}
