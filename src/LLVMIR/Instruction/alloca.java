package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMType;

public class alloca extends statement {
	private LLVMType type;

	public alloca(LLVMType type, entity dest) {
		super(dest);
		this.type = type;
	}
}
