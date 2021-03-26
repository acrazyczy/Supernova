package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.register;

public class STORE extends inst {
	public enum storeType {
		SB, SW
	}

	private final register rs1, rs2;
	private final Imm offset;
	private final storeType type;

	public STORE(storeType type, register rs2, Imm offset, register rs1) {
		super();
		this.type = type;
		this.rs2 = rs2;
		this.offset = offset;
		this.rs1 = rs1;
	}

	@Override public String toString() {return type + " " + rs2 + ", " + offset + "(" + rs1 + ")";}
}
