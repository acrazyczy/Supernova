package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMSingleValueType;

public abstract class entity {
	public LLVMSingleValueType type;

	public entity(LLVMSingleValueType type) {this.type = type;}
}
