package Assembly.Instruction;

import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

public class mvInst extends inst {

	public mvInst(asmBlock belongTo, virtualReg rd, virtualReg rs1) {
		super(belongTo);
		this.defs.add(this.rd = rd);
		this.uses.add(this.rs1 = rs1);
		rd.defs.add(this);
		rs1.uses.add(this);
	}

	@Override public String toString() {return "mv " + rd + ", " + rs1;}
}
