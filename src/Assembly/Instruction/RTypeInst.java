package Assembly.Instruction;

import Assembly.Operand.physicalReg;
import Assembly.Operand.reg;
import Assembly.Operand.virtualReg;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class RTypeInst extends inst {
	public enum opType {
		add, sub, mul, div, rem, sll, sra, xor, slt, sltu, or, and
	}

	public final opType type;
	public reg rd, rs1, rs2;

	public RTypeInst(opType type, reg rd, reg rs1, reg rs2) {
		super();
		this.type = type;
		this.rd = rd;
		this.rs1 = rs1;
		this.rs2 = rs2;
	}

	public boolean testMergeability(reg rd) {return this.rd == rd && (type == opType.slt || type == opType.sltu);}

	@Override
	public void replaceVirtualRegister(ArrayList<inst> insts, BiFunction<virtualReg, ArrayList<inst>, physicalReg> action) {
		if (rd instanceof virtualReg) rd = action.apply((virtualReg) rd, insts);
		if (rs1 instanceof virtualReg) rs1 = action.apply((virtualReg) rs1, insts);
		if (rs2 instanceof virtualReg) rs2 = action.apply((virtualReg) rs2, insts);
	}

	@Override public String toString() {return type + " " + rd + ", " + rs1 + ", " + rs2;}
}
