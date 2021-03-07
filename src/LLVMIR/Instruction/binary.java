package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

public class binary extends statement {
	public enum instCode {
		add, sub, mul, sdiv, srem, shl, ashr, and, or, xor
	}

	private instCode inst;
	private entity op1, op2;

	public binary(instCode inst, entity op1, entity op2, entity dest) {
		super(dest);
		this.inst = inst;
		this.op1 = op1;
		this.op2 = op2;
	}
}
