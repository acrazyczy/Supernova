package Assembly.Instruction;

import Assembly.Operand.Imm;
import Assembly.Operand.reg;

public class liInst extends inst {
	private final reg rd;
	private Imm imm;

	public liInst(reg rd, Imm imm) {
		super();
		this.rd = rd;
		this.imm = imm;
	}

	@Override public String toString() {return "li " + rd + ", " + imm;}
}
