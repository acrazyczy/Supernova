package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMType;

public class zext extends statement {
	LLVMType type1, type2;
	entity value;

	public zext(LLVMType type1, entity value, LLVMType type2) {
		super();
		this.type1 = type1;
		this.value = value;
		this.type2 = type2;
	}
}
