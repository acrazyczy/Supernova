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

abstract public class statement implements Cloneable {
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

	abstract public void testAndReplaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef);

	abstract public boolean testOperand(TriPredicate<Set<statement>, basicBlock, entity> tester, Set<statement> SCC, basicBlock hdr);

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
