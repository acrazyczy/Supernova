package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import Optimization.IR.OSR;
import Util.TriFunction;
import Util.TriPredicate;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class binary extends statement {
	public enum instCode {
		add, sub, mul, sdiv, srem, shl, ashr, and, or, xor
	}

	public final instCode inst;
	public entity op1, op2;

	public binary(instCode inst, entity op1, entity op2, entity dest) {
		super(dest);
		this.inst = inst;
		this.op1 = op1;
		this.op2 = op2;
		assert op1.type.equals(op2.type);
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = new HashSet<>();
		if (op1 instanceof register) ret.add((register) op1);
		if (op2 instanceof register) ret.add((register) op2);
		ret.add((register) dest);
		return ret;
	}

	@Override
	public Set<register> uses() {
		Set<register> ret = new HashSet<>();
		if (op1 instanceof register) ret.add((register) op1);
		if (op2 instanceof register) ret.add((register) op2);
		return ret;
	}

	@Override
	public void replaceUse(entity oldReg, entity newReg) {
		if (op1 == oldReg) op1 = newReg;
		if (op2 == oldReg) op2 = newReg;
	}

	@Override
	public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef) {
		op1 = replacer.apply(expr, newDef, op1);
		op2 = replacer.apply(expr, newDef, op2);
	}

	@Override
	public boolean testOperand(TriPredicate<Set<register>, basicBlock, entity> tester, Set<register> SCC, basicBlock hdr) {
		return tester.test(SCC, hdr, op1) && tester.test(SCC, hdr, op2);
	}

	@Override public statement clone() {return new binary(inst, op1, op2, dest);}

	@Override
	public void replaceAllRegister(Function<register, register> replacer) {
		dest = replacer.apply((register) dest);
		if (op1 instanceof register) op1 = replacer.apply((register) op1);
		if (op2 instanceof register) op2 = replacer.apply((register) op2);
	}

	@Override public String toString() {return dest + " = " + inst + " " + op1.type + " " + op1 + ", " + op2;}
}
