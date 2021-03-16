package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMType;

public class load extends statement {
	private entity pointer;

	public load(entity pointer, entity dest) {
		super(dest);
		this.pointer = pointer;
	}
}
