package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import Optimization.IR.OSR;
import Util.TriFunction;
import Util.TriPredicate;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

public class store extends statement {
	public entity value, pointer;

	public store(entity value, entity pointer) {
		super();
		this.value = value;
		this.pointer = pointer;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = new LinkedHashSet<>();
		if (value instanceof register) ret.add((register) value);
		if (pointer instanceof register) ret.add((register) pointer);
		return ret;
	}

	@Override
	public Set<register> uses() {
		Set<register> ret = new LinkedHashSet<>();
		if (value instanceof register) ret.add((register) value);
		if (pointer instanceof register) ret.add((register) pointer);
		return ret;
	}

	@Override
	public void replaceUse(entity oldReg, entity newReg) {
		if (value == oldReg) value = newReg;
		if (pointer == oldReg) pointer = newReg;
	}

	@Override
	public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef) {
		assert false; // Oops!
	}

	@Override
	public boolean testOperand(TriPredicate<Set<register>, basicBlock, entity> tester, Set<register> SCC, basicBlock hdr) {
		assert false; // Oops!
		return false;
	}

	@Override public statement clone() {return new store(value, pointer);}

	@Override
	public void replaceAllRegister(Function<register, register> replacer) {
		if (value instanceof register) value = replacer.apply((register) value);
		if (pointer instanceof register) pointer = replacer.apply((register) pointer);
	}

	@Override public String toString() {return "store " + value.type + " " + value + ", " + pointer.type + " " + pointer;}
}
