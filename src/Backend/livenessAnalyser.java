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
		Map<asmBlock, Set<virtualReg>> blkUses = new HashMap<>(), blkDefs = new HashMap<>();
		dfsOrder.forEach(blk -> {
			blk.liveIn = new HashSet<>();blk.liveOut = new HashSet<>();
			blkUses.put(blk, blk.uses());
			blkDefs.put(blk, blk.defs());
		});
		boolean changed;
		do {
			changed = false;
			for (int i = dfsOrder.size() - 1; i >= 0; --i) {
				asmBlock blk = dfsOrder.get(i);
				Set<virtualReg> oldLiveIn = new HashSet<>(blk.liveIn), oldLiveOut = new HashSet<>(blk.liveOut);
				blk.liveIn = blkUses.get(blk);
				blk.liveOut.removeAll(blkDefs.get(blk));
				blk.liveIn.addAll(blk.liveOut);
				blk.liveOut = new HashSet<>();
				blk.successors.forEach(s -> blk.liveOut.addAll(s.liveIn));
				changed |= !oldLiveIn.equals(blk.liveIn) || !oldLiveOut.equals(blk.liveOut);
			}
		} while (changed);
	}

	@Override public void run() {programEntry.asmFunctions.values().stream().filter(asmFunc -> asmFunc.asmBlocks != null).forEach(this::livenessComputation);}
}
