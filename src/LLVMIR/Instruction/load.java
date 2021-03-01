package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMType;

public class load extends statement {
	private LLVMType type;
	private LLVMPointerType pointerType;
	private entity pointer;

	public load(LLVMType type, LLVMPointerType pointerType, entity pointer) {
		super();
		this.type = type;
		this.pointerType = pointerType;
		this.pointer = pointer;
	}
}
