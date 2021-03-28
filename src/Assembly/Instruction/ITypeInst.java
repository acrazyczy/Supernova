package Assembly.Instruction;

import Assembly.Operand.*;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class ITypeInst extends inst {
	public enum opType {
		addi, slti, sltiu, andi, ori, xori
	}

	private final opType type;
	private reg rd, rs1;
	private final Imm imm;

	public ITypeInst(opType type, reg rd, reg rs1, Imm imm) {
		super();
		this.type = type;
		this.rd = rd;
		this.rs1 = rs1;
		this.imm = imm;
	}

	public boolean testMergeability(reg rd) {
		return this.rd == rd &&
			type == opType.xori &&
			imm instanceof intImm &&
			((intImm) imm).val == 1;
	}

	@Override
	public void replaceVirtualRegister(ArrayList<inst> insts, BiFunction<virtualReg, ArrayList<inst>, physicalReg> action) {
		if (rs1 instanceof virtualReg) rs1 = action.apply((virtualReg) rs1, insts);
		if (rd instanceof virtualReg) rd = action.apply((virtualReg) rd, insts);
	}

	@Override public String toString() {return type + " " + rd + ", " + rs1 + ", " + imm;}
}
