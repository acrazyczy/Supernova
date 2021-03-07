package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

public abstract class statement {
	public entity dest;

	public statement() {}

	public statement(entity dest) {this.dest = dest;}
}
