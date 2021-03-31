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
		this.offset = offset;
		this.uses.add(this.rs2 = rs2);
		this.uses.add(this.rs1 = rs1);
		rs2.uses.add(this);
		rs1.uses.add(this);
	}

	@Override public String toString() {return type + " " + rs2 + ", " + offset + "(" + rs1 + ")";}
}
