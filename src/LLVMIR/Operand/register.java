package LLVMIR.Operand;

import LLVMIR.Instruction.statement;
import LLVMIR.TypeSystem.LLVMSingleValueType;
import LLVMIR.function;

public class register extends entity {
	public String name;
	public register reachingDef; // can only be used in SSA Construction
	public statement def = null;
	public boolean isLocalVariable = false;

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
		this.name = name + "." + currentFunction.getRegisterNameIndex(name);
	}

	public register(LLVMSingleValueType type, String name, function currentFunction, boolean isLocalVariable) {
		super(type);
		this.name = name + "." + currentFunction.getRegisterNameIndex(name);
		this.isLocalVariable = isLocalVariable;
	}

	@Override public String toString() {return name == null ? "" : "%" + name;}
}
