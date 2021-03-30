package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

public class luiInst extends inst {
	private final Imm imm;

	public luiInst(asmBlock belongTo, virtualReg rd, Imm imm) {
		super(belongTo);
		this.def.add(this.rd = rd);
		this.imm = imm;
	}

	@Override public String toString() {return "lui " + rd + ", " + imm;}
}
