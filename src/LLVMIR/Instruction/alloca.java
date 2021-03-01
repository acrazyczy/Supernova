package LLVMIR.Instruction;

import LLVMIR.TypeSystem.LLVMType;

public class alloca extends statement {
	private LLVMType type;

	public alloca(LLVMType type) {
		super();
		this.type = type;
	}
}
