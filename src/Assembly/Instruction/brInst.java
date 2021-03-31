package Assembly.Instruction;

import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

public class brInst extends inst {
	public enum opType {
		beqz, bnez, bltu, blt, bge, bgeu
	}

	private final opType type;
	private final asmBlock label;

	public brInst(asmBlock belongTo, opType type, virtualReg rs1, virtualReg rs2, asmBlock label) {
		super(belongTo);
		this.type = type;
		this.uses.add(this.rs1 = rs1);
		rs1.uses.add(this);
		if (type == opType.beqz || type == opType.bnez) assert rs2 == null;
		else {
			assert rs2 != null;
			this.uses.add(this.rs2 = rs2);
			rs2.uses.add(this);
		}
		this.label = label;
	}

	@Override public String toString() {return type + " " + rs1 + ", " + (rs2 == null ? "" : rs2 + ", ") + label;}
}
