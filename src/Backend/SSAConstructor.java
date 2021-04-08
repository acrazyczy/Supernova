package Backend;

import LLVMIR.IREntry;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import LLVMIR.function;

import java.util.*;

public class SSAConstructor implements pass {
	private final IREntry programIREntry;
	private Map<basicBlock, List<basicBlock>> predecessors;
	private Map<basicBlock, Set<basicBlock>> dom;
	private Map<basicBlock, basicBlock> idom;
	private Map<basicBlock, Set<basicBlock>> children;
	private Map<basicBlock, Set<basicBlock>> DF;
	private ArrayList<basicBlock> dfsPreOrder;

	private Set<register> vars;
	private Map<register, Set<basicBlock>> defs;

	public SSAConstructor(IREntry programIREntry) {
		this.programIREntry = programIREntry;
	}

	@Override
	public void run() {
		dom = new HashMap<>();
		idom = new HashMap<>();
		DF = new HashMap<>();
		children = new HashMap<>();
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

	private void dfsDominatorTree(basicBlock blk) {
		dfsPreOrder.add(blk);
		children.get(blk).forEach(this::dfsDominatorTree);
	}

	private void dominanceAnalysis(function func) {
		boolean changed;
		ArrayList<basicBlock> dfsOrder = func.dfsOrderComputation();
		dfsOrder.forEach(blk -> children.put(blk, new HashSet<>()));
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
					children.put(top, new HashSet<>(Collections.singleton(blk)));
					break;
				}
				predecessors.get(top).stream().filter(pred -> !visited.contains(pred)).forEach(pred -> {
					queue.add(pred); visited.add(pred);
				});
			}
		}
		dfsPreOrder = new ArrayList<>();
		dfsDominatorTree(func.blocks.get(0));
	}

	private void DFComputation(function func) {
		dfsPreOrder.forEach(blk -> DF.put(blk, new HashSet<>()));
		dfsPreOrder.forEach(blk -> {
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
		vars = new HashSet<>();
		defs = new HashMap<>();
		func.variablesAnalysis(vars, null, null, null, defs);
		vars.forEach(v -> {
			Set<basicBlock> F = new HashSet<>(), W = new HashSet<>(defs.get(v));
			while (!W.isEmpty()) {
				basicBlock x = W.iterator().next();
				W.remove(x);
				DF.get(x).stream().filter(y -> !F.contains(y)).forEach(y -> {
					y.addPhiFunction(v, x);
					F.add(y);
					if (!defs.get(v).contains(y)) W.add(y);
				});
			}
		});
	}

	private void variableRenaming(function func) {
		vars.forEach(v -> v.reachingDef = null);
		dfsPreOrder.forEach(blk -> {

		});
	}
}
