package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

public class liInst extends inst {
	private final Imm imm;

	public liInst(asmBlock belongTo, virtualReg rd, Imm imm) {
		super(belongTo);
		this.defs.add(this.rd = rd);
		rd.defs.add(this);
		this.imm = imm;
	}

	@Override public String toString() {return "li " + rd + ", " + imm;}
}
