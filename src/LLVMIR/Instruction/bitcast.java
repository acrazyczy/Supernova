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

public class bitcast extends statement {
	public entity value;

	public bitcast(entity value, entity dest) {
		super(dest);
		this.value = value;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = new HashSet<>();
		if (value instanceof register) ret.add((register) value);
		ret.add((register) dest);
		return ret;
	}

	@Override
	public Set<register> uses() {
		return value instanceof register ? new HashSet<>(Collections.singleton((register) value)) : new HashSet<>();
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

	@Override public String toString() {return dest + " = bitcast " + value.type + " " + value + " to " + dest.type;}
}