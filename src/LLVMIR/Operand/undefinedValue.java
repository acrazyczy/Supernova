package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMSingleValueType;

public class undefinedValue extends entity {
	public undefinedValue(LLVMSingleValueType type) {super(type);}

	@Override public String toString() {return "undef";}
}
