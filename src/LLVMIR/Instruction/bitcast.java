package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

public class bitcast extends statement {
	private entity value, dest;

	public bitcast(entity value, entity dest) {
		super();
		this.value = value;
		this.dest = dest;
	}
}
