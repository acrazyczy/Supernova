package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

abstract public class statement {
	public final entity dest;
	public final basicBlock belongTo;

	public statement(basicBlock belongTo) {
		this.dest = null;
		this.belongTo = belongTo;
	}

	public statement(basicBlock belongTo, entity dest) {
		assert dest instanceof register;
		this.dest = dest;
		this.belongTo = belongTo;
	}

	abstract public Set<register> variables();
	abstract public Set<register> uses();

	public Set<register> defs() {
		return dest == null ? new HashSet<>() : Collections.singleton((register) dest);
	}

	@Override abstract public String toString();
}
