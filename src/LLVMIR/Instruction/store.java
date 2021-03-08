package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMType;

public class store extends statement {
	private entity value, pointer;

	public store(entity value, entity pointer) {
		super();
		this.value = value;
		this.pointer = pointer;
	}
}
