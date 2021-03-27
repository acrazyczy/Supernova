package Assembly.Instruction;

import Assembly.Operand.reg;

public class szInst extends inst {
	public enum opType {
		seqz, snez, sltz, sgtz
	}

	private final opType type;
	private final reg rd, rs;

	public szInst(opType type, reg rd, reg rs) {
		super();
		this.type = type;
		this.rd = rd;
		this.rs = rs;
	}

	@Override public String toString() {return type + " " + rd + ", " + rs;}
}
