package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.basicBlock;

public class br extends terminalStmt {
	private final entity cond;
	public basicBlock trueBranch, falseBranch;

	public br(basicBlock nextBlock) {
		super();
		this.cond = null;
		this.trueBranch = nextBlock;
		this.falseBranch = null;
	}

	public br(entity cond, basicBlock trueBranch, basicBlock falseBranch) {
		super();
		this.cond = cond;
		this.trueBranch = trueBranch;
		this.falseBranch = falseBranch;
	}

	@Override public String toString() {
		if (this.cond == null) return "br label " + trueBranch;
		String ret = "br " + cond.type + " " + cond + ", label " + trueBranch;
		if (this.falseBranch != null) ret += ", label " + falseBranch;
		return ret;
	}
}
