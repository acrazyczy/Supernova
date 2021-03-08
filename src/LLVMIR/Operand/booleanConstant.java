package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMIntegerType;

public class booleanConstant extends constant {
	private int val;

	public booleanConstant(int val) {
		super(new LLVMIntegerType(1));
		this.val = val;
	}
}