package Assembly.Instruction;

import Assembly.Operand.physicalReg;
import Assembly.Operand.reg;
import Assembly.Operand.virtualReg;
import Assembly.asmBlock;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class brInst extends inst {
	public enum opType {
		beqz, bnez, bltu, blt, bge, bgeu
	}

	private final opType type;
	private reg rs1, rs2;
	private final asmBlock label;

	public brInst(opType type, reg rs1, reg rs2, asmBlock label) {
		super();
		this.type = type;
		this.rs1 = rs1;
		this.rs2 = rs2;
		if (type == opType.beqz || type == opType.bnez) assert rs2 == null;
		else assert rs2 != null;
		this.label = label;
	}

	@Override
	public void replaceVirtualRegister(ArrayList<inst> insts, BiFunction<virtualReg, ArrayList<inst>, physicalReg> action) {
		if (rs1 instanceof virtualReg) rs1 = action.apply((virtualReg) rs1, insts);
		if (rs2 instanceof virtualReg) rs2 = action.apply((virtualReg) rs2, insts);
	}

	@Override public String toString() {return type + " " + rs1 + ", " + (rs2 == null ? "" : rs2 + ", ") + label;}
}
