package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
	public Set<register> variables() {
		Set<register> ret = values.stream().filter(val -> val instanceof register)
			.map(val -> (register) val).collect(Collectors.toSet());
		ret.add((register) dest);
		return ret;
	}

	@Override public Set<register> uses() {
		return values.stream().filter(val -> val instanceof register)
			.map(val -> (register) val).collect(Collectors.toSet());
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder(dest + " = phi " + dest.type + " [ " + values.get(0) + ", " + blocks.get(0) + " ]");
		for (int i = 1;i < blocks.size();++ i)
			ret.append(", [ ").append(values.get(i)).append(", ").append(blocks.get(i)).append(" ]");
		return ret.toString();
	}

	// only replace uses in a specified block
	@Override public void replaceUse(register oldReg, register newReg) {}

	public void replaceUse(register oldReg, register newReg, basicBlock blk) {
		for (int i = 0;i < blocks.size();++ i)
			if (blk == blocks.get(i)) {
				if (values.get(i) == oldReg) values.set(i, newReg);
				break;
			}
	}
}
