package Optimization.asm;

import Assembly.asmEntry;

public class asmOptimizer {
	private final asmEntry programAsmEntry;

	public asmOptimizer(asmEntry programAsmEntry) {this.programAsmEntry = programAsmEntry;}

	public void run() {
		boolean flag;
		do {
			flag = new CFGSimplifier(programAsmEntry).run();
		} while (flag);
	}
}
