package LLVMIR.Operand;

import LLVMIR.TypeSystem.LLVMSingleValueType;
import LLVMIR.function;

public class register extends entity {
	public String name;
	public register reachingDef;

	public register(LLVMSingleValueType type) {
		super(type);
		this.name = null;
	}

	public register(LLVMSingleValueType type, String name) {
		super(type);
		this.name = name;
	}

	public register(LLVMSingleValueType type, String name, function currentFunction) {
		super(type);
		this.name = name + currentFunction.getRegisterNameIndex(name);
	}

	@Override public String toString() {return name == null ? "" : "%" + name;}
}
