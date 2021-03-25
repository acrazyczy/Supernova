package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMSingleValueType;

public class globalVariable extends entity {
	private final String name;

	public globalVariable(LLVMSingleValueType type, String name) {
		super(type);
		this.name = name;
	}

	@Override
	public String toString() {
		return "@" + name;
	}
}
