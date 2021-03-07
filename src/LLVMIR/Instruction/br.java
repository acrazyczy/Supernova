package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.basicBlock;

public class br extends terminalStmt {
	private entity cond;
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
}
