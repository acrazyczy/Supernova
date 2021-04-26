package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.basicBlock;
import Optimization.IR.OSR;
import Util.TriFunction;
import Util.TriPredicate;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class getelementptr extends statement {
	public entity pointer;
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
	public void replaceUse(entity oldReg, entity newReg) {
		if (pointer == oldReg) pointer = newReg;
		for (int i = 0;i < idxes.size();++ i)
			if (idxes.get(i) == oldReg)
				idxes.set(i, newReg);
	}

	@Override
	public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef) {
		assert false; // Oops!
	}

	@Override
	public boolean testOperand(TriPredicate<Set<register>, basicBlock, entity> tester, Set<register> SCC, basicBlock hdr) {
		assert false; // Oops!
		return false;
	}

	@Override public statement clone() {return new getelementptr(pointer, new ArrayList<>(idxes), dest);}

	@Override
	public void replaceAllRegister(Function<register, register> replacer) {
		dest = replacer.apply((register) dest);
		if (pointer instanceof register) pointer = replacer.apply((register) pointer);
		for (ListIterator<entity> idxItr = idxes.listIterator(); idxItr.hasNext();) {
			entity idx = idxItr.next();
			if (idx instanceof register) idxItr.set(replacer.apply((register) idx));
		}
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder(dest + " = getelementptr " + ((LLVMPointerType) pointer.type).pointeeType + ", " + pointer.type + " " + pointer);
		for (entity idx: idxes) ret.append(", ").append(idx.type).append(" ").append(idx);
		return ret.toString();
	}
}
