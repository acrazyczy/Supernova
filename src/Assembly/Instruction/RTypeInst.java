package Assembly.Instruction;

import Assembly.Operand.reg;

public class RTypeInst extends inst {
	public enum opType {
		add, sub, mul, div, rem, sll, sra, xor, slt, sltu, or, and
	}

	private final opType type;
	private final reg rd, rs1, rs2;

	public RTypeInst(opType type, reg rd, reg rs1, reg rs2) {
		super();
		this.type = type;
		this.rd = rd;
		this.rs1 = rs1;
		this.rs2 = rs2;
	}

	@Override public String toString() {return type + " " + rd + ", " + rs1 + ", " + rs2;}
}
