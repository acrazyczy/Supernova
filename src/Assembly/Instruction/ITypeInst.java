package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.asmBlock;

public class ITypeInst extends inst {
	public enum opType {
		addi, slti, sltiu, andi, ori, xori
	}

	private final opType type;
	private final Imm imm;

	public ITypeInst(asmBlock belongTo, opType type, virtualReg rd, virtualReg rs1, Imm imm) {
		super(belongTo);
		this.type = type;
		this.defs.add(this.rd = rd);
		this.uses.add(this.rs1 = rs1);
		rd.defs.add(this);
		rs1.uses.add(this);
		this.imm = imm;
	}

	public boolean testMergeability(reg rd) {
		return this.rd == rd &&
			type == opType.xori &&
			imm instanceof intImm &&
			((intImm) imm).val == 1;
	}

	@Override public String toString() {return type + " " + rd + ", " + rs1 + ", " + imm;}
}
