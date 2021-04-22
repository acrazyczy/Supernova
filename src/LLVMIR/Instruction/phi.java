package LLVMIR.Instruction;

import LLVMIR.Operand.entity;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import Optimization.IR.OSR;
import Util.TriFunction;
import Util.TriPredicate;

import javax.imageio.ImageTranscoder;
import java.util.*;
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
		for (ListIterator<entity> valItr = values.listIterator();valItr.hasNext();)
			if (valItr.next() == oldReg) valItr.set(newReg);
	}

	public void replaceUse(entity oldReg, entity newReg, basicBlock blk) {
		Iterator<basicBlock> blkItr = blocks.iterator();
		for (ListIterator<entity> valItr = values.listIterator();valItr.hasNext();) {
			entity v = valItr.next();
			if (blkItr.next() == blk) {
				if (v == oldReg) valItr.set(newReg);
				break;
			}
		}
	}

	@Override
	public void replaceOperand(TriFunction<OSR.exprType, statement, entity, entity> replacer, OSR.exprType expr, statement newDef) {
		for (ListIterator<entity> valItr = values.listIterator();valItr.hasNext();)
			valItr.set(replacer.apply(expr, newDef, valItr.next()));
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
