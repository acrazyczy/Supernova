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

public class ret extends terminalStmt {
	public entity value;

	public ret(entity value) {
		super();
		this.value = value;
	}

	@Override
	public Set<register> variables() {
		return value instanceof register ? new LinkedHashSet<>(Collections.singleton((register) value)) : new LinkedHashSet<>();
	}

	@Override
	public Set<register> uses() {
		return value instanceof register ? new LinkedHashSet<>(Collections.singleton((register) value)) : new LinkedHashSet<>();
	}

	@Override public void replaceUse(entity oldReg, entity newReg) {if (value == oldReg) value = newReg;}

	@Override
	public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef) {
		assert false; // Oops!
	}

	@Override
	public boolean testOperand(TriPredicate<Set<register>, basicBlock, entity> tester, Set<register> SCC, basicBlock hdr) {
		assert false; // Oops!
		return false;
	}

	@Override public statement clone() {return new ret(value);}

	@Override
	public void replaceAllRegister(Function<register, register> replacer) {
		if (value instanceof register) value = replacer.apply((register) value);
	}

	@Override public String toString() {return value == null ? "ret void" : "ret " + value.type + " " + value;}
}
