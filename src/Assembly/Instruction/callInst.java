package Assembly.Instruction;

import Assembly.asmFunction;

public class callInst extends inst {
	private final asmFunction func;

	public callInst(asmFunction func) {
		super();
		this.func = func;
	}

	@Override public String toString() {return "call " + func;}
}
