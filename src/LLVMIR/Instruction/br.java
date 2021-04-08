package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class br extends terminalStmt {
	public final entity cond;
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

	@Override
	public Set<register> variables() {
		return cond instanceof register ? new HashSet<>(Collections.singleton((register) cond)) : new HashSet<>();
	}

	@Override
	public Set<register> uses() {
		return cond instanceof register ? new HashSet<>(Collections.singleton((register) cond)) : new HashSet<>();
	}

	@Override public String toString() {
		if (this.cond == null) return "br label " + trueBranch;
		String ret = "br " + cond.type + " " + cond + ", label " + trueBranch;
		if (this.falseBranch != null) ret += ", label " + falseBranch;
		return ret;
	}
}
