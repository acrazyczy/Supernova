package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class bitcast extends statement {
	public entity value;

	public bitcast(entity value, entity dest) {
		super(dest);
		this.value = value;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = new HashSet<>();
		if (value instanceof register) ret.add((register) value);
		ret.add((register) dest);
		return ret;
	}

	@Override
	public Set<register> uses() {
		return value instanceof register ? new HashSet<>(Collections.singleton((register) value)) : new HashSet<>();
	}

	@Override public void replaceUse(register oldReg, register newReg) {if (value == oldReg) value = newReg;}

	@Override public String toString() {return dest + " = bitcast " + value.type + " " + value + " to " + dest.type;}
}