package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class load extends statement {
	public entity pointer;

	public load(entity pointer, entity dest) {
		super(dest);
		this.pointer = pointer;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = new HashSet<>(Collections.singleton((register) dest));
		if (pointer instanceof register) ret.add((register) pointer);
		return ret;
	}

	@Override
	public Set<register> uses() {
		return pointer instanceof register ? new HashSet<>(Collections.singleton((register) pointer)) : new HashSet<>();
	}

	@Override public void replaceUse(register oldReg, register newReg) {if (pointer == oldReg) pointer = newReg;}

	@Override public String toString() {return dest + " = load " + dest.type + ", " + pointer.type + " " + pointer;}
}
