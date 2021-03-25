package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

public class ret extends terminalStmt {
	private final entity value;

	public ret(entity value) {
		super();
		this.value = value;
	}

	@Override public String toString() {return value == null ? "ret void" : "ret " + value.type + " " + value;}
}
