package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

public class store extends statement {
	public final entity value, pointer;

	public store(entity value, entity pointer) {
		super();
		this.value = value;
		this.pointer = pointer;
	}

	@Override public String toString() {return "store " + value.type + " " + value + ", " + pointer.type + " " + pointer;}
}
