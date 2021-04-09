package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.function;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class call extends statement {
	public final function callee;
	public final ArrayList<entity> parameters;

	public call(function callee, ArrayList<entity> parameters) {
		super();
		this.callee = callee;
		this.parameters = parameters;
	}

	public call(function callee, ArrayList<entity> parameters, entity dest) {
		super(dest);
		this.callee = callee;
		this.parameters = parameters;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = parameters.stream().filter(p -> p instanceof register)
			.map(p -> (register) p).collect(Collectors.toSet());
		if (dest != null) ret.add((register) dest);
		return ret;
	}

	@Override
	public Set<register> uses() {
		return parameters.stream().filter(p -> p instanceof register)
			.map(p -> (register) p).collect(Collectors.toSet());
	}

	@Override
	public void replaceUse(register oldReg, register newReg) {
		for (int i = 0;i < parameters.size();++ i)
			if (parameters.get(i) == oldReg)
				parameters.set(i, newReg);
	}

	@Override public String toString() {
		String ret = "";
		if (this.dest != null) ret += this.dest + " = ";
		ret += "call " + callee.functionToString(parameters);
		return ret;
	}
}
