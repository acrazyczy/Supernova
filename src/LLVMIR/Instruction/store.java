package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;

import java.util.HashSet;
import java.util.Set;

public class store extends statement {
	public entity value, pointer;

	public store(entity value, entity pointer) {
		super();
		this.value = value;
		this.pointer = pointer;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = new HashSet<>();
		if (value instanceof register) ret.add((register) value);
		if (pointer instanceof register) ret.add((register) pointer);
		return ret;
	}

	@Override
	public Set<register> uses() {
		Set<register> ret = new HashSet<>();
		if (value instanceof register) ret.add((register) value);
		if (pointer instanceof register) ret.add((register) pointer);
		return ret;
	}

	@Override
	public void replaceUse(register oldReg, register newReg) {
		if (value == oldReg) value = newReg;
		if (pointer == oldReg) pointer = newReg;
	}

	@Override public String toString() {return "store " + value.type + " " + value + ", " + pointer.type + " " + pointer;}
}
