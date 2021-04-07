package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;

import java.util.Set;

abstract public class statement {
	public entity dest;

	public statement() {this.dest = null;}

	public statement(entity dest) {this.dest = dest;}

	abstract public Set<register> variables ();

	@Override abstract public String toString();
}
