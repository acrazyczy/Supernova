package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;

import java.util.HashSet;
import java.util.Set;

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
		assert op1.type == op2.type;
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

	@Override public String toString() {return dest + " = " + inst + " " + op1.type + " " + op1 + ", " + op2;}
}
