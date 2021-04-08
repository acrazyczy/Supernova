package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;

import java.util.HashSet;
import java.util.Set;

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

	@Override public String toString() {return dest + " = icmp " + cond + " " + op1.type + " " + op1 + ", " + op2;}
}
