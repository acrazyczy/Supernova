package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

public class loadInst extends inst {
	public enum loadType {
		lbu, lw
	}

	private final Imm imm;
	private final loadType type;

	public loadInst(asmBlock belongTo, loadType type, virtualReg rd, virtualReg rs1, Imm imm) {
		super(belongTo);
		this.type = type;
		this.def.add(this.rd = rd);
		this.use.add(this.rs1 = rs1);
		this.imm = imm;
	}

	@Override public String toString() {return type + " " + rd + ", " + imm + "(" + rs1 + ")";}
}
