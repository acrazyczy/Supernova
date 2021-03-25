package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.function;

import java.util.ArrayList;

public class call extends statement {
	private final function callee;
	private final ArrayList<entity> parameters;

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

	@Override public String toString() {
		String ret = "";
		if (this.dest != null) ret += this.dest + " = ";
		ret += "call " + callee.functionToString(parameters);
		return ret;
	}
}
