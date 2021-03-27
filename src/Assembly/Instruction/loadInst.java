package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.reg;

public class loadInst extends inst {
	public enum loadType {
		LBU, LW
	}

	private final reg rd, rs1;
	private final Imm imm;
	private final loadType type;

	public loadInst(loadType type, reg rd, reg rs1, Imm imm) {
		super();
		this.type = type;
		this.rd = rd;
		this.rs1 = rs1;
		this.imm = imm;
	}

	@Override public String toString() {return type + " " + rd + ", " + imm + "(" + rs1 + ")";}
}
