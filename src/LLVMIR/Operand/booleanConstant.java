package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMIntegerType;

public class booleanConstant extends constant {
	private final int val;

	public booleanConstant(int val) {
		super(new LLVMIntegerType(8));
		this.val = val;
	}

	@Override public String toString() {return String.valueOf(val);}
}