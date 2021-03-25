package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.basicBlock;

import java.util.ArrayList;

public class phi extends statement {
	private final ArrayList<basicBlock> blocks;
	private final ArrayList<entity> values;

	public phi(ArrayList<basicBlock> blocks, ArrayList<entity> values, entity dest) {
		super(dest);
		this.blocks = blocks;
		this.values = values;
		assert blocks.size() == values.size();
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder(dest + " = phi " + dest.type + " [ " + values.get(0) + ", " + blocks.get(0) + " ]");
		for (int i = 1;i < blocks.size();++ i)
			ret.append(", [ ").append(values.get(i)).append(", ").append(blocks.get(i)).append(" ]");
		return ret.toString();
	}
}
