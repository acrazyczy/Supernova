package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMType;

public class icmp extends statement {
	public enum condCode {
		eq, ne, ugt, uge, ult, ule, sgt, sge, slt, sle
	}

	private condCode cond;
	private entity op1, op2;

	public icmp(condCode cond, entity op1, entity op2, entity dest) {
		super(dest);
		this.cond = cond;
		this.op1 = op1;
		this.op2 = op2;
	}
}
