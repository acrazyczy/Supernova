package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

public class binary extends statement {
	public enum instCode {
		add, sub, mul, sdiv, srem, shl, ashr, and, or, xor
	}

	private final instCode inst;
	private final entity op1, op2;

	public binary(instCode inst, entity op1, entity op2, entity dest) {
		super(dest);
		this.inst = inst;
		this.op1 = op1;
		this.op2 = op2;
		assert op1.type == op2.type;
	}

	@Override public String toString() {return dest + " = " + inst + " " + op1.type + ", " + op2.type;}
}
