package LLVMIR.Instruction;

import LLVMIR.Operand.entity;

import java.util.ArrayList;

public class getelementptr extends statement {
	private final entity pointer;
	private final ArrayList<entity> idxes;

	public getelementptr(entity pointer, ArrayList<entity> idxes, entity dest) {
		super(dest);
		this.pointer = pointer;
		this.idxes = idxes;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder(dest + " = getelementptr " + dest.type + ", " + pointer.type);
		for (entity idx: idxes) ret.append(", ").append(idx.type).append(" ").append(idx);
		return ret.toString();
	}
}
