package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

public class icmp extends statement {
	public enum condCode {
		eq, ne, ugt, uge, ult, ule, sgt, sge, slt, sle
	}

	public final condCode cond;
	public final entity op1, op2;

	public icmp(condCode cond, entity op1, entity op2, entity dest) {
		super(dest);
		this.cond = cond;
		this.op1 = op1;
		this.op2 = op2;
		assert op1.type == op2.type;
	}

	@Override public String toString() {return dest + " = icmp " + cond + " " + op1.type + " " + op1 + ", " + op2;}
}
