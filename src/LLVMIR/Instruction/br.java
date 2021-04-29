package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import Optimization.IR.OSR;
import Util.TriFunction;
import Util.TriPredicate;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

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
		return cond instanceof register ? new LinkedHashSet<>(Collections.singleton((register) cond)) : new LinkedHashSet<>();
	}

	@Override
	public Set<register> uses() {
		return cond instanceof register ? new LinkedHashSet<>(Collections.singleton((register) cond)) : new LinkedHashSet<>();
	}

	@Override public void replaceUse(entity oldReg, entity newReg) {if (cond == oldReg) cond = newReg;}

	@Override
	public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef) {
		assert false; // Oops!
	}

	@Override
	public boolean testOperand(TriPredicate<Set<register>, basicBlock, entity> tester, Set<register> SCC, basicBlock hdr) {
		assert false; // Oops!
		return false;
	}

	@Override public statement clone() {return cond == null ? new br(trueBranch) : new br(cond, trueBranch, falseBranch);}

	@Override
	public void replaceAllRegister(Function<register, register> replacer) {
		if (cond instanceof register) cond = replacer.apply((register) cond);
	}

	@Override public String toString() {
		if (this.cond == null) return "br label " + trueBranch;
		return "br " + cond.type + " " + cond + ", label " + trueBranch + ", label " + falseBranch;
	}
}
