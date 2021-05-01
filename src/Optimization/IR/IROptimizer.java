package Optimization.IR;

import LLVMIR.IREntry;

public class IROptimizer {
	private final IREntry programIREntry;

	public IROptimizer(IREntry programIREntry) {this.programIREntry = programIREntry;}

	public void normalOptimization(boolean isSSAForm, int round) {
		boolean flag;
		int cnt = 0;
		do {
			++ cnt;
			flag = isSSAForm && new TCO(programIREntry).run();
			flag |= isSSAForm && new SCCP(programIREntry).run();
			flag |= isSSAForm && new ADCE(programIREntry).run();
			flag |= isSSAForm && new LICM(programIREntry).run();
			flag |= new CSE(programIREntry).run();
			flag |= isSSAForm && new copyPropagation(programIREntry).run();
			flag |= isSSAForm && new algebraicSimplifier(programIREntry).run();
			flag |= cnt == 0 && isSSAForm && new OSR(programIREntry).run();
			flag |= new CFGSimplifier(programIREntry).run();
			flag |= isSSAForm && new inlineExpansion(programIREntry, false).run();
		} while (flag && cnt < round);
	}

	public void run(boolean isSSAForm) {
		normalOptimization(isSSAForm, 3);
		if (isSSAForm) new inlineExpansion(programIREntry, true).run();
		normalOptimization(isSSAForm, 2);
	}
}
