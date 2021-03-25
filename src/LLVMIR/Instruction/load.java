package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

public class load extends statement {
	private final entity pointer;

	public load(entity pointer, entity dest) {
		super(dest);
		this.pointer = pointer;
	}

	@Override public String toString() {return dest + " = load " + dest.type + " " + pointer.type + " " + pointer;}
}
