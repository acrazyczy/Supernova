package Assembly.Instruction;

import Assembly.Operand.reg;
import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

public class RTypeInst extends inst {
	public enum opType {
		add, sub, mul, div, rem, sll, sra, xor, slt, sltu, or, and
	}

	public final opType type;

	public RTypeInst(asmBlock belongTo, opType type, virtualReg rd, virtualReg rs1, virtualReg rs2) {
		super(belongTo);
		this.type = type;
		this.def.add(this.rd = rd);
		this.use.add(this.rs1 = rs1);
		this.use.add(this.rs2 = rs2);
	}

	public boolean testMergeability(reg rd) {return this.rd == rd && (type == opType.slt || type == opType.sltu);}

	@Override public String toString() {return type + " " + rd + ", " + rs1 + ", " + rs2;}
}
