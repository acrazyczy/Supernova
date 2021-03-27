package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMIntegerType;

public class integerConstant extends constant {
	public final int val;

	public integerConstant(int width, int val) {
		super(new LLVMIntegerType(width));
		this.val = val;
	}

	@Override public String toString() {return String.valueOf(val);}
}
