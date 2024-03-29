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

public class bitcast extends statement {
	public entity value;

	public bitcast(entity value, entity dest) {
		super(dest);
		this.value = value;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = new LinkedHashSet<>();
		if (value instanceof register) ret.add((register) value);
		ret.add((register) dest);
		return ret;
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

	@Override public statement clone() {return new bitcast(value, dest);}

	@Override
	public void replaceAllRegister(Function<register, register> replacer) {
		dest = replacer.apply((register) dest);
		if (value instanceof register) value = replacer.apply((register) value);
	}

	@Override public String toString() {return dest + " = bitcast " + value.type + " " + value + " to " + dest.type;}
}