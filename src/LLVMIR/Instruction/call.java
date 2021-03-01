package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMFunctionType;

import java.util.ArrayList;

public class call extends statement {
	private LLVMFunctionType callee;
	private ArrayList<entity> parameters;

	public call(LLVMFunctionType callee, ArrayList<entity> parameters) {
		super();
		this.callee = callee;
		this.parameters = parameters;
	}
}
