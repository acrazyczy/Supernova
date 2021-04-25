package Optimization.asm;

import Assembly.asmBlock;
import Assembly.asmEntry;
import Assembly.asmFunction;
import Backend.asmVisitor;

import java.util.ArrayList;
import java.util.List;

public class CFGSimplifier implements asmVisitor {
	private asmEntry programAsmEntry;

	public CFGSimplifier(asmEntry programAsmEntry) {this.programAsmEntry = programAsmEntry;}

	private boolean run(asmFunction asmFunc) {
		boolean changed = false;
		List<asmBlock> blocks = new ArrayList<>(asmFunc.asmBlocks);
		blocks.add(asmFunc.initBlock);
		for (asmBlock blk: blocks) {
			if (blk.headInst == null) continue;
			while (blk.successors.size() == 1 && blk.successors.iterator().next().predecessors.size() == 1) {
				asmBlock sucBlk = blk.successors.iterator().next();
				blk.mergeBlock(sucBlk);
				if (sucBlk == asmFunc.retBlock) asmFunc.asmBlocks.remove(asmFunc.retBlock = blk);
				else asmFunc.asmBlocks.remove(sucBlk);
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean run() {
		return programAsmEntry.asmFunctions.values().stream().filter(asmFunc -> asmFunc.asmBlocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
