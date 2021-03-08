package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMIntegerType;

public class integerConstant extends constant {
	private int val;

	public integerConstant(int width, int val) {
		super(new LLVMIntegerType(width));
		this.val = val;
	}
}
