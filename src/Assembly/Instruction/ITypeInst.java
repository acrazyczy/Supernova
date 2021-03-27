package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.reg;

public class ITypeInst extends inst {
	public enum opType {
		addi, slti, sltiu, andi, ori, xori
	}

	private final opType type;
	private final reg rd, rs1;
	private final Imm imm;

	public ITypeInst(opType type, reg rd, reg rs1, Imm imm) {
		super();
		this.type = type;
		this.rd = rd;
		this.rs1 = rs1;
		this.imm = imm;
	}

	@Override public String toString() {return type + " " + rd + ", " + rs1 + ", " + imm;}
}
