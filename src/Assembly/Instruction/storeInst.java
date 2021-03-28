package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.physicalReg;
import Assembly.Operand.reg;
import Assembly.Operand.virtualReg;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class storeInst extends inst {
	public enum storeType {
		sb, sw
	}

	private reg rs1, rs2;
	private final Imm offset;
	private final storeType type;

	public storeInst(storeType type, reg rs2, Imm offset, reg rs1) {
		super();
		this.type = type;
		this.rs2 = rs2;
		this.offset = offset;
		this.rs1 = rs1;
	}

	@Override
	public void replaceVirtualRegister(ArrayList<inst> insts, BiFunction<virtualReg, ArrayList<inst>, physicalReg> action) {
		if (rs1 instanceof virtualReg) rs1 = action.apply((virtualReg) rs1, insts);
		if (rs2 instanceof virtualReg) rs2 = action.apply((virtualReg) rs2, insts);
	}

	@Override public String toString() {return type + " " + rs2 + ", " + offset + "(" + rs1 + ")";}
}
