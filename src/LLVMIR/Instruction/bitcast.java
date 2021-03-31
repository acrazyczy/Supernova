package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

public class bitcast extends statement {
	public final entity value, dest;

	public bitcast(entity value, entity dest) {
		super();
		this.value = value;
		this.dest = dest;
	}

	@Override public String toString() {return dest + " = bitcast " + value.type + " " + value + " to " + dest.type;}
}