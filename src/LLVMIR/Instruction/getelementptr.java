package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMType;

import java.util.ArrayList;

public class getelementptr extends statement {
	private LLVMType type;
	private LLVMPointerType pointerType;
	private entity pointer;
	private ArrayList<LLVMType> types;
	private ArrayList<entity> idxes;

	public getelementptr(LLVMType type, LLVMPointerType pointerType, entity pointer, ArrayList<LLVMType> types, ArrayList<entity> idxes) {
		super();
		this.type = type;
		this.pointerType = pointerType;
		this.pointer = pointer;
		this.types = types;
		this.idxes = idxes;
	}
}
