package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class br extends terminalStmt {
	public entity cond;
	public basicBlock trueBranch, falseBranch;

	public br(basicBlock nextBlock) {
		super();
		this.cond = null;
		this.trueBranch = nextBlock;
		this.falseBranch = null;
	}

	public br(entity cond, basicBlock trueBranch, basicBlock falseBranch) {
		super();
		assert trueBranch != null && falseBranch != null;
		this.cond = cond;
		this.trueBranch = trueBranch;
		this.falseBranch = falseBranch;
	}

	@Override
	public Set<register> variables() {
		return cond instanceof register ? new HashSet<>(Collections.singleton((register) cond)) : new HashSet<>();
	}

	@Override
	public Set<register> uses() {
		return cond instanceof register ? new HashSet<>(Collections.singleton((register) cond)) : new HashSet<>();
	}

	@Override public void replaceUse(entity oldReg, entity newReg) {if (cond == oldReg) cond = newReg;}

	@Override public String toString() {
		if (this.cond == null) return "br label " + trueBranch;
		return "br " + cond.type + " " + cond + ", label " + trueBranch + ", label " + falseBranch;
	}
}
