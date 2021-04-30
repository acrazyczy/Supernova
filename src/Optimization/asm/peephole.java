package Optimization.asm;

import Assembly.Instruction.jumpInst;
import Assembly.asmBlock;
import Assembly.asmEntry;
import Assembly.asmFunction;
import Backend.asmVisitor;

import java.util.ListIterator;

public class peephole implements asmVisitor {
	private final asmEntry programAsmEntry;

	public peephole(asmEntry programAsmEntry) {this.programAsmEntry = programAsmEntry;}

	private boolean run(asmFunction asmFunc) {
		boolean changed = false;
		ListIterator<asmBlock> blkItr = asmFunc.asmBlocks.listIterator();
		while (blkItr.hasNext()) {
			asmBlock asmBlk = blkItr.next();
			if (asmBlk.tailInst instanceof jumpInst && blkItr.hasNext() && ((jumpInst) asmBlk.tailInst).label == asmFunc.asmBlocks.get(blkItr.nextIndex())) {
				asmBlk.tailInst = asmBlk.tailInst.pre;
				changed = true;
				if (asmBlk.tailInst == null) asmBlk.headInst = null;
				else asmBlk.tailInst.suf = null;
			}
		}
		return changed;
	}

	@Override
	public boolean run() {
		return programAsmEntry.asmFunctions.values().stream().filter(asmFunc -> asmFunc.asmBlocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
