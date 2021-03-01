package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMType;

public class ret extends terminalStmt {
	private LLVMType type;
	private entity value;

	public ret(LLVMType type, entity value) {
		super();
		this.type = type;
		this.value = value;
	}
}
