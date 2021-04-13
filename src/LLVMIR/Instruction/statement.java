package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

abstract public class statement {
	public entity dest;
	public basicBlock belongTo;

	public statement() {
		this.dest = null;
	}

	public statement(entity dest) {
		assert dest instanceof register;
		this.dest = dest;
	}

	abstract public Set<register> variables();
	abstract public Set<register> uses();

	abstract public void replaceUse(entity oldReg, entity newReg);

	public void replaceDef(register oldReg, register newReg) {
		if (dest == oldReg) dest = newReg;
	}

	public Set<register> defs() {
		return dest == null ? new HashSet<>() : Collections.singleton((register) dest);
	}

	@Override abstract public String toString();
}
