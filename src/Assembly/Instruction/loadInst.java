package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.physicalReg;
import Assembly.Operand.reg;
import Assembly.Operand.virtualReg;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class loadInst extends inst {
	public enum loadType {
		lbu, lw
	}

	private reg rd, rs1;
	private final Imm imm;
	private final loadType type;

	public loadInst(loadType type, reg rd, reg rs1, Imm imm) {
		super();
		this.type = type;
		this.rd = rd;
		this.rs1 = rs1;
		this.imm = imm;
	}

	@Override
	public void replaceVirtualRegister(ArrayList<inst> insts, BiFunction<virtualReg, ArrayList<inst>, physicalReg> action) {
		if (rs1 instanceof virtualReg) rs1 = action.apply((virtualReg) rs1, insts);
		if (rd instanceof virtualReg) rd = action.apply((virtualReg) rd, insts);
	}

	@Override public String toString() {return type + " " + rd + ", " + imm + "(" + rs1 + ")";}
}
