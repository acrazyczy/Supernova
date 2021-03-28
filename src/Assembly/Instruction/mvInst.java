package Assembly.Instruction;

import Assembly.Operand.physicalReg;
import Assembly.Operand.reg;
import Assembly.Operand.virtualReg;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class mvInst extends inst {
	private reg rd, rs;

	public mvInst(reg rd, reg rs) {
		super();
		this.rd = rd;
		this.rs = rs;
	}

	@Override
	public void replaceVirtualRegister(ArrayList<inst> insts, BiFunction<virtualReg, ArrayList<inst>, physicalReg> action) {
		if (rd instanceof virtualReg) rd = action.apply((virtualReg) rd, insts);
		if (rs instanceof virtualReg) rs = action.apply((virtualReg) rs, insts);
	}

	@Override public String toString() {return "mv " + rd + ", " + rs;}
}
