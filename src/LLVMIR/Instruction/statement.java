package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

abstract public class statement {
	public entity dest;

	public statement() {this.dest = null;}

	public statement(entity dest) {this.dest = dest;}

	@Override abstract public String toString();
}
