package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.function;

import java.util.ArrayList;

public class call extends statement {
	private function callee;
	private ArrayList<entity> parameters;

	public call(function callee, ArrayList<entity> parameters) {
		super();
		this.callee = callee;
		this.parameters = parameters;
	}
}
