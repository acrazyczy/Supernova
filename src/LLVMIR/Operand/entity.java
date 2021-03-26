package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMSingleValueType;

abstract public class entity {
	public final LLVMSingleValueType type;

	public entity(LLVMSingleValueType type) {this.type = type;}

	@Override abstract public String toString();
}
