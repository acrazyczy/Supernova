package Backend;

import Assembly.Operand.virtualReg;
import Assembly.asmBlock;
import Assembly.asmEntry;
import Assembly.asmFunction;

import java.util.*;

public class livenessAnalyser implements asmVisitor {
	private final asmEntry programEntry;

	public livenessAnalyser(asmEntry programEntry) {
		this.programEntry = programEntry;
	}

	private void livenessComputation(asmFunction asmFunc) {
		ArrayList<asmBlock> dfsOrder = asmFunc.dfsOrderComputation();
		Map<asmBlock, Set<virtualReg>> blkUses = new LinkedHashMap<>(), blkDefs = new LinkedHashMap<>();
		dfsOrder.forEach(blk -> {
			blk.liveIn = new LinkedHashSet<>();blk.liveOut = new LinkedHashSet<>();
			blkUses.put(blk, blk.uses());
			blkDefs.put(blk, blk.defs());
		});
		boolean changed;
		do {
			changed = false;
			for (int i = dfsOrder.size() - 1; i >= 0; --i) {
				asmBlock blk = dfsOrder.get(i);
				Set<virtualReg> oldLiveIn = new LinkedHashSet<>(blk.liveIn), oldLiveOut = new LinkedHashSet<>(blk.liveOut);
				blk.liveIn = blkUses.get(blk);
				blk.liveOut.removeAll(blkDefs.get(blk));
				blk.liveIn.addAll(blk.liveOut);
				blk.liveOut = new LinkedHashSet<>();
				blk.successors.forEach(s -> blk.liveOut.addAll(s.liveIn));
				changed |= !oldLiveIn.equals(blk.liveIn) || !oldLiveOut.equals(blk.liveOut);
			}
		} while (changed);
	}

	@Override
	public boolean run() {
		programEntry.asmFunctions.values().stream().filter(asmFunc -> asmFunc.asmBlocks != null).forEach(this::livenessComputation);
		return true;
	}
}
