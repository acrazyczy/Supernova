package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.TypeSystem.LLVMPointerType;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class getelementptr extends statement {
	public final entity pointer;
	public final ArrayList<entity> idxes;

	public getelementptr(entity pointer, ArrayList<entity> idxes, entity dest) {
		super(dest);
		this.pointer = pointer;
		this.idxes = idxes;
	}

	@Override
	public Set<register> variables() {
		Set<register> ret = idxes.stream().filter(idx -> idx instanceof register)
			.map(idx -> (register) idx).collect(Collectors.toSet());
		if (pointer instanceof register) ret.add((register) pointer);
		ret.add((register) dest);
		return ret;
	}

	@Override
	public Set<register> uses() {
		Set<register> ret = idxes.stream().filter(idx -> idx instanceof register)
			.map(idx -> (register) idx).collect(Collectors.toSet());
		if (pointer instanceof register) ret.add((register) pointer);
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder(dest + " = getelementptr " + ((LLVMPointerType) pointer.type).pointeeType + ", " + pointer.type + " " + pointer);
		for (entity idx: idxes) ret.append(", ").append(idx.type).append(" ").append(idx);
		return ret.toString();
	}
}
