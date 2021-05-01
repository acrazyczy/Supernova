package Backend;

import Assembly.Operand.virtualReg;
import Assembly.asmBlock;
import Assembly.asmEntry;
import Assembly.asmFunction;

import java.util.*;

public class livenessAnalyser {
	private final asmFunction asmFunc;

	public livenessAnalyser(asmFunction asmFunc) {
		this.asmFunc = asmFunc;
	}

	public void run() {
		Set<asmBlock> blocks = new LinkedHashSet<>(asmFunc.asmBlocks);
		blocks.add(asmFunc.initBlock);
		blocks.add(asmFunc.retBlock);
		Map<asmBlock, Set<virtualReg>> blkUses = new LinkedHashMap<>(), blkDefs = new LinkedHashMap<>();
		Set<asmBlock> isVisited = new LinkedHashSet<>();
		Queue<asmBlock> workList = new ArrayDeque<>();
		blocks.forEach(blk -> {
			blk.liveIn = new LinkedHashSet<>();
			blk.liveOut = new LinkedHashSet<>();
			blkUses.put(blk, blk.uses());
			blkDefs.put(blk, blk.defs());
			if (blk.successors.isEmpty()) {
				blk.liveIn = new LinkedHashSet<>(blkUses.get(blk));
				isVisited.add(blk);
				workList.addAll(blk.predecessors);
			}
		});
		while (!workList.isEmpty()) {
			asmBlock blk = workList.poll();
			if (!isVisited.contains(blk)) {
				isVisited.add(blk);
				Set<virtualReg> conj = new LinkedHashSet<>();
				blk.successors.forEach(sucBlk -> conj.addAll(sucBlk.liveIn));
				blk.liveOut = conj;
				Set<virtualReg> newLiveIn = new LinkedHashSet<>(conj);
				newLiveIn.removeAll(blkDefs.get(blk));
				newLiveIn.addAll(blkUses.get(blk));
				if (!blk.liveIn.equals(newLiveIn)) {
					blk.liveIn = newLiveIn;
					isVisited.removeAll(blk.predecessors);
				}
				workList.addAll(blk.predecessors);
			}
		}
	}
}
