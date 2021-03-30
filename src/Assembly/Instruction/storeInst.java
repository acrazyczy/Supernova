package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

public class storeInst extends inst {
	public enum storeType {
		sb, sw
	}

	private final Imm offset;
	private final storeType type;

	public storeInst(asmBlock belongTo, storeType type, virtualReg rs2, Imm offset, virtualReg rs1) {
		super(belongTo);
		this.type = type;
		this.use.add(this.rs2 = rs2);
		this.offset = offset;
		this.use.add(this.rs1 = rs1);
	}

	@Override public String toString() {return type + " " + rs2 + ", " + offset + "(" + rs1 + ")";}
}
