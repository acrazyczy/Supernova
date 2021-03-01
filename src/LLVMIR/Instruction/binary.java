package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMType;

public class binary extends statement {
	public enum instCode {
		add, sub, mul, div, rem
	}

	private instCode inst;
	private LLVMType type;
	private entity op1, op2;

	public binary(instCode inst, LLVMType type, entity op1, entity op2) {
		super();
		this.inst = inst;
		this.type = type;
		this.op1 = op1;
		this.op2 = op2;
	}
}
