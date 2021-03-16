package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMType;

import java.util.ArrayList;

public class getelementptr extends statement {
	private entity pointer;
	private ArrayList<entity> idxes;

	public getelementptr(entity pointer, ArrayList<entity> idxes, entity dest) {
		super(dest);
		this.pointer = pointer;
		this.idxes = idxes;
	}
}
