package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

public class _move extends statement {
	entity src, dest;

	public _move(entity src, entity dest) {
		this.src = src;
		this.dest = dest;
	}
}
