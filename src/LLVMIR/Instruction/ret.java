package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ret extends terminalStmt {
	public final entity value;

	public ret(entity value) {
		super();
		this.value = value;
	}

	@Override
	public Set<register> variables() {
		return value instanceof register ? new HashSet<>(Collections.singleton((register) value)) : new HashSet<>();
	}

	@Override
	public Set<register> uses() {
		return value instanceof register ? new HashSet<>(Collections.singleton((register) value)) : new HashSet<>();
	}

	@Override public String toString() {return value == null ? "ret void" : "ret " + value.type + " " + value;}
}
