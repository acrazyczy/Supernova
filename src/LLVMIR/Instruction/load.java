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

public class load extends statement {
	public entity pointer;

	public load(entity pointer, entity dest) {
		super(dest);
		this.pointer = pointer;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = new HashSet<>(Collections.singleton((register) dest));
		if (pointer instanceof register) ret.add((register) pointer);
		return ret;
	}

	@Override
	public Set<register> uses() {
		return pointer instanceof register ? new HashSet<>(Collections.singleton((register) pointer)) : new HashSet<>();
	}

	@Override public void replaceUse(entity oldReg, entity newReg) {if (pointer == oldReg) pointer = newReg;}

	@Override
	public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef) {
		assert false; // Oops!
	}

	@Override
	public boolean testOperand(TriPredicate<Set<register>, basicBlock, entity> tester, Set<register> SCC, basicBlock hdr) {
		assert false; // Oops!
		return false;
	}

	@Override public statement clone() {return new load(pointer, dest);}

	@Override
	public void replaceAllRegister(Function<register, register> replacer) {
		dest = replacer.apply((register) dest);
		if (pointer instanceof register) pointer = replacer.apply((register) pointer);
	}

	@Override public String toString() {return dest + " = load " + dest.type + ", " + pointer.type + " " + pointer;}
}
