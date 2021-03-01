package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.TypeSystem.LLVMType;
import LLVMIR.basicBlock;

import java.util.ArrayList;

public class phi extends statement {
	private LLVMType type;
	private ArrayList<basicBlock> blocks;
	private ArrayList<entity> values;

	public phi(LLVMType type, ArrayList<basicBlock> blocks, ArrayList<entity> values) {
		super();
		this.type = type;
		this.blocks = blocks;
		this.values = values;
	}
}
