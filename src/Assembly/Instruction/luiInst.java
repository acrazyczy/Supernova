package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.physicalReg;
import Assembly.Operand.reg;
import Assembly.Operand.virtualReg;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class luiInst extends inst {
	private reg rd;
	private final Imm imm;

	public luiInst(reg rd, Imm imm) {
		super();
		this.rd = rd;
		this.imm = imm;
	}

	@Override
	public void replaceVirtualRegister(ArrayList<inst> insts, BiFunction<virtualReg, ArrayList<inst>, physicalReg> action) {
		if (rd instanceof virtualReg) rd = action.apply((virtualReg) rd, insts);
	}

	@Override public String toString() {return "lui " + rd + ", " + imm;}
}
