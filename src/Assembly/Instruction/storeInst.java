package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.reg;

public class storeInst extends inst {
	public enum storeType {
		sb, sw
	}

	private final reg rs1, rs2;
	private final Imm offset;
	private final storeType type;

	public storeInst(storeType type, reg rs2, Imm offset, reg rs1) {
		super();
		this.type = type;
		this.rs2 = rs2;
		this.offset = offset;
		this.rs1 = rs1;
	}

	@Override public String toString() {return type + " " + rs2 + ", " + offset + "(" + rs1 + ")";}
}
