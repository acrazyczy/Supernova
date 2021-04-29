package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import Optimization.IR.OSR;
import Util.TriFunction;
import Util.TriPredicate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

abstract public class statement {
	public entity dest;
	public basicBlock belongTo;

	public statement() {
		this.dest = null;
	}

	public statement(entity dest) {
		assert dest instanceof register;
		this.dest = dest;
	}

	abstract public Set<register> variables();
	abstract public Set<register> uses();

	abstract public void replaceUse(entity oldReg, entity newReg);

	public void replaceDef(register oldReg, register newReg) {
		if (dest == oldReg) dest = newReg;
	}

	public Set<register> defs() {
		return dest == null ? new HashSet<>() : Collections.singleton((register) dest);
	}

	@Override abstract public String toString();

	abstract public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef);

	abstract public boolean testOperand(TriPredicate<Set<register>, basicBlock, entity> tester, Set<register> SCC, basicBlock hdr);

	abstract public void replaceAllRegister(Function<register, register> replacer);

	abstract public statement clone();
}
