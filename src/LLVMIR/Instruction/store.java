package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMType;

public class store extends statement {
	private LLVMType type;
	private entity value;
	private LLVMPointerType pointerType;
	private entity pointer;

	public store(LLVMType type, entity value, LLVMPointerType pointerType, entity pointer) {
		super();
		this.type = type;
		this.value = value;
		this.pointerType = pointerType;
		this.pointer = pointer;
	}
}
