package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import Optimization.IR.OSR;
import Util.TriFunction;
import Util.TriPredicate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public class phi extends statement {
	public final LinkedList<basicBlock> blocks;
	public final LinkedList<entity> values;

	public phi(LinkedList<basicBlock> blocks, LinkedList<entity> values, entity dest) {
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

	public void replaceUse(entity oldReg, entity newReg) {
		for (int i = 0;i < values.size();++ i)
			if (values.get(i) == oldReg) values.set(i, newReg);
	}

	public void replaceUse(entity oldReg, entity newReg, basicBlock blk) {
		for (int i = 0;i < blocks.size();++ i)
			if (blk == blocks.get(i)) {
				if (values.get(i) == oldReg) values.set(i, newReg);
				break;
			}
	}

	@Override
	public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef) {
		for (int i = 0;i < values.size();++ i)
			values.set(i, replacer.apply(expr, newDef, values.get(i)));
	}

	@Override
	public boolean testOperand(TriPredicate<Set<register>, basicBlock, entity> tester, Set<register> SCC, basicBlock hdr) {
		return values.stream().allMatch(o -> tester.test(SCC, hdr, o));
	}

	@Override public statement clone() {return new phi(new LinkedList<>(blocks), new LinkedList<>(values), dest);}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder(dest + " = phi " + dest.type + " [ " + values.iterator().next() + ", " + blocks.iterator().next() + " ]");
		for (int i = 1;i < blocks.size();++ i)
			ret.append(", [ ").append(values.get(i)).append(", ").append(blocks.get(i)).append(" ]");
		return ret.toString();
	}
}
