package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMSingleValueType;

public abstract class constant extends entity {
	public constant(LLVMSingleValueType type) {super(type);}

	@Override public abstract String toString();
}
