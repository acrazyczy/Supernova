package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMSingleValueType;

abstract public class constant extends entity {
	public constant(LLVMSingleValueType type) {super(type);}

	@Override abstract public String toString();
}
