package Assembly.Instruction;

import Assembly.Operand.physicalReg;
import Assembly.Operand.reg;
import Assembly.Operand.virtualReg;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class szInst extends inst {
	public enum opType {
		seqz, snez
	}

	public final opType type;
	public reg rd, rs;

	public szInst(opType type, reg rd, reg rs) {
		super();
		this.type = type;
		this.rd = rd;
		this.rs = rs;
	}

	public boolean testMergeability(reg rd) {return rd == this.rd;}

	@Override
	public void replaceVirtualRegister(ArrayList<inst> insts, BiFunction<virtualReg, ArrayList<inst>, physicalReg> action) {
		if (rd instanceof virtualReg) rd = action.apply((virtualReg) rd, insts);
		if (rs instanceof virtualReg) rs = action.apply((virtualReg) rs, insts);
	}

	@Override public String toString() {return type + " " + rd + ", " + rs;}
}
