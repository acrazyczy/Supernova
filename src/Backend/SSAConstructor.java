package Backend;

import LLVMIR.IREntry;
import LLVMIR.basicBlock;
import LLVMIR.function;

import java.util.*;

public class SSAConstructor implements pass {
	private final IREntry programIREntry;
	private Map<basicBlock, List<basicBlock>> predecessors;
	private Map<basicBlock, Set<basicBlock>> dom;
	private Map<basicBlock, basicBlock> idom;
	private Map<basicBlock, Set<basicBlock>> DF;

	public SSAConstructor(IREntry programIREntry) {
		this.programIREntry = programIREntry;
	}

	@Override
	public void run() {
		dom = new HashMap<>();
		predecessors = new HashMap<>();
		programIREntry.functions.forEach(func -> {
			initialization(func);
			dominanceAnalysis(func);
			DFComputation(func);
			phiInsertion(func);
			variableRenaming(func);
		});
	}

	private void initialization(function func) {
		func.blocks.forEach(blk -> {
			dom.put(blk, blk.name.equals("entry") ? new HashSet<>() : new HashSet<>(func.blocks));
			predecessors.put(blk, new ArrayList<>());
		});
		func.blocks.forEach(blk -> blk.successors().forEach(sucBlk -> predecessors.get(sucBlk).add(blk)));
	}

	private void dominanceAnalysis(function func) {
		boolean changed;
		ArrayList<basicBlock> dfsOrder = func.dfsOrderComputation();
		do {
			changed = false;
			for (basicBlock blk: dfsOrder) {
				Set<basicBlock> temp = new HashSet<>(Collections.singleton(blk));
				predecessors.get(blk).forEach(preBlk -> temp.addAll(dom.get(preBlk)));
				if (!temp.equals(dom.get(blk))) {
					dom.put(blk, temp);
					changed = true;
				}
			}
		} while (changed);
		for (basicBlock blk: dfsOrder) {
			if (blk.name.equals("entry")) {
				idom.put(blk, null);
				continue;
			}
			Set<basicBlock> domSet = dom.get(blk), visited = new HashSet<>(Collections.singleton(blk));
			Queue<basicBlock> queue = new ArrayDeque<>(Collections.singletonList(blk));
			while (!queue.isEmpty()) {
				basicBlock top = queue.remove();
				if (domSet.contains(top)) {
					idom.put(blk, top);
					break;
				}
				predecessors.get(top).stream().filter(pred -> !visited.contains(pred)).forEach(pred -> {
					queue.add(pred); visited.add(pred);
				});
			}
		}
	}

	private void DFComputation(function func) {
		func.blocks.forEach(blk -> DF.put(blk, new HashSet<>()));
		func.blocks.forEach(blk -> {
			List<basicBlock> predecessor = predecessors.get(blk);
			if (predecessor.size() > 1) {
				predecessor.forEach(p -> {
					for (basicBlock runner = p;runner != idom.get(blk);runner = idom.get(runner))
						DF.get(runner).add(blk);
				});
			}
		});
	}

	private void phiInsertion(function func) {

	}

	private void variableRenaming(function func) {
	}
}
