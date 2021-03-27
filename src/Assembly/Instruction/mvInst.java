package Assembly.Instruction;

import Assembly.Operand.reg;

public class mvInst extends inst {
	private final reg rd, rs;

	public mvInst(reg rd, reg rs) {
		super();
		this.rd = rd;
		this.rs = rs;
	}

	@Override public String toString() {return "mv " + rd + ", " + rs;}
}
