package LLVMIR.Operand;

import LLVMIR.Instruction.statement;
import LLVMIR.TypeSystem.LLVMSingleValueType;
import LLVMIR.basicBlock;
import LLVMIR.function;

public class register extends entity {
	public String name;
	public register reachingDef; // can only be used in SSA Construction
	public statement def = null;
	public boolean isTemporaryVariable = false;
	public basicBlock phiBlock = null;

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

	public register(LLVMSingleValueType type, String name, function currentFunction, boolean isTemporaryVariable) {
		super(type);
		this.name = name + "." + currentFunction.getRegisterNameIndex(name);
		this.isTemporaryVariable = isTemporaryVariable;
	}

	public register(LLVMSingleValueType type, String name, function currentFunction, basicBlock phiBlock) {
		super(type);
		this.name = name + "." + currentFunction.getRegisterNameIndex(name);
		this.phiBlock = phiBlock;
	}

	@Override public String toString() {return name == null ? "" : "%" + name;}
}
