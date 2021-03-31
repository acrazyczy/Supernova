package Backend;

import Assembly.Operand.virtualReg;
import Assembly.asmBlock;
import Assembly.asmEntry;
import Assembly.asmFunction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class livenessAnalyser implements asmVisitor {
	private final asmEntry programEntry;

	public livenessAnalyser(asmEntry programEntry) {
		this.programEntry = programEntry;
	}

	private void livenessComputation(asmFunction asmFunc) {
		ArrayList<asmBlock> dfsOrder = asmFunc.dfsOrderComputation();
		dfsOrder.forEach(blk -> {blk.liveIn = new HashSet<>();blk.liveOut = new HashSet<>();});
		boolean changed = false;
		do {
			for (int i = dfsOrder.size() - 1; i >= 0; --i) {
				asmBlock blk = dfsOrder.get(i);
				Set<virtualReg> oldLiveIn = new HashSet<>(blk.liveIn), oldLiveOut = new HashSet<>(blk.liveOut);
				blk.liveIn = blk.uses();
				blk.liveOut.removeAll(blk.defs());
				blk.liveIn.addAll(blk.liveOut);
				blk.liveOut = new HashSet<>();
				blk.successors.forEach(s -> blk.liveOut.addAll(s.liveIn));
				changed |= !oldLiveIn.equals(blk.liveIn) || !oldLiveOut.equals(blk.liveOut);
			}
		} while (changed);
	}

	@Override public void run() {programEntry.asmFunctions.values().forEach(this::livenessComputation);}
}
