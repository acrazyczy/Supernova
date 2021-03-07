package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

public class ret extends terminalStmt {
	private entity value;

	public ret(entity value) {
		super();
		this.value = value;
	}
}
