package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.register;

public class LOAD extends inst {
	public enum loadType {
		LBU, LW
	}

	private final register rd, rs1;
	private final Imm imm;
	private final loadType type;

	public LOAD(loadType type, register rd, register rs1, Imm imm) {
		super();
		this.type = type;
		this.rd = rd;
		this.rs1 = rs1;
		this.imm = imm;
	}

	@Override public String toString() {return type + " " + rd + ", " + imm + "(" + rs1 + ")";}
}
