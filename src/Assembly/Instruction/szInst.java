package Assembly.Instruction;

import Assembly.Operand.reg;
import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

public class szInst extends inst {
	public enum opType {
		seqz, snez
	}

	public final opType type;

	public szInst(asmBlock belongTo, opType type, virtualReg rd, virtualReg rs1) {
		super(belongTo);
		this.type = type;
		this.def.add(this.rd = rd);
		this.use.add(this.rs1 = rs1);
	}

	public boolean testMergeability(reg rd) {return rd == this.rd;}

	@Override public String toString() {return type + " " + rd + ", " + rs1;}
}
