package Optimization.IR;

import LLVMIR.basicBlock;

import java.util.*;

public class dominanceAnalyser {
	private final basicBlock root;
	private final Set<basicBlock> V;
	public final Map<basicBlock, Set<basicBlock>> adj, radj;

	public Map<basicBlock, Set<basicBlock>> DF, dom, children;
	public Map<basicBlock, basicBlock> idom;

	public dominanceAnalyser(basicBlock root, Set<basicBlock> V) {
		this.root = root;
		this.V = V;
		this.adj = new HashMap<>();
		this.V.forEach(v -> this.adj.put(v, new HashSet<>()));
		this.radj = new HashMap<>();
		this.V.forEach(v -> this.radj.put(v, new HashSet<>()));
	}

	private void getPostOrderOfGraph(basicBlock blk, Set<basicBlock> isVisited, List<basicBlock> order) {
		isVisited.add(blk);
		adj.get(blk).stream().filter(adjBlk -> !isVisited.contains(adjBlk)).forEach(adjBlk -> getPostOrderOfGraph(adjBlk, isVisited, order));
		order.add(blk);
	}

	private ArrayList<basicBlock> getPostOrderOfGraph() {
		ArrayList<basicBlock> order = new ArrayList<>();
		Set<basicBlock> isVisited = new HashSet<>();
		getPostOrderOfGraph(root, isVisited, order);
		return order;
	}

	private ArrayList<basicBlock> getReversePostOrderOfGraph() {
		ArrayList<basicBlock> order = getPostOrderOfGraph();
		Collections.reverse(order);
		return order;
	}

	private void domComputation(List<basicBlock> RPO) {
		dom = new HashMap<>();
		RPO.forEach(v -> dom.put(v, v == root ? new HashSet<>(Collections.singleton(v)) : new HashSet<>(RPO)));
		boolean changed;
		do {
			changed = false;
			for (basicBlock v: RPO) {
				if (v == root) continue;
				Set<basicBlock> temp = new HashSet<>(RPO);
				radj.get(v).forEach(u -> temp.retainAll(dom.get(u)));
				temp.add(v);
				if (!temp.equals(dom.get(v))) {
					dom.put(v, temp);
					changed = true;
				}
			}
		} while (changed);
	}

	private void idomComputation(List<basicBlock> RPO) {
		idom = new HashMap<>();
		for (basicBlock blk: RPO) {
			if (blk == root) {
				idom.put(blk, null);
				continue;
			}
			Set<basicBlock> domSet = new HashSet<>(dom.get(blk)), isVisited = new HashSet<>(Collections.singleton(blk));
			domSet.remove(blk);
			Queue<basicBlock> queue = new ArrayDeque<>(Collections.singletonList(blk));
			while (!queue.isEmpty()) {
				basicBlock top = queue.remove();
				if (domSet.contains(top)) {
					idom.put(blk, top);
					break;
				}
				radj.get(top).stream().filter(pred -> !isVisited.contains(pred)).forEach(pred -> {
					queue.add(pred); isVisited.add(pred);
				});
			}
		}
	}

	private void dominatorTreeConstruction(List<basicBlock> RPO) {
		children = new HashMap<>();
		RPO.forEach(v -> children.put(v, new HashSet<>()));
		RPO.stream().filter(v -> v != root).forEach(v -> children.get(idom.get(v)).add(v));
	}

	private void DFComputation(List<basicBlock> RPO) {
		DF = new HashMap<>();
		RPO.forEach(v -> DF.put(v, new HashSet<>()));
		RPO.stream().filter(v -> radj.get(v).size() > 1).forEach(v -> radj.get(v).forEach(u -> {
			for (basicBlock runner = u;runner != idom.get(v);runner = idom.get(runner)) DF.get(runner).add(v);
		}));
	}

	public void dominanceAnalysis(boolean DFComputationFlag) {
		ArrayList<basicBlock> RPO = getReversePostOrderOfGraph();
		domComputation(RPO);
		idomComputation(RPO);
		dominatorTreeConstruction(RPO);
		if (DFComputationFlag) DFComputation(RPO);
	}

	public void addEdge(basicBlock u, basicBlock v) {
		adj.get(u).add(v);
		radj.get(v).add(u);
	}

	private void getPreOrderOfTree(basicBlock blk, List<basicBlock> order) {
		order.add(blk);
		children.get(blk).forEach(adjBlk -> getPreOrderOfTree(adjBlk, order));
	}

	public ArrayList<basicBlock> getPreOrderOfTree() {
		ArrayList<basicBlock> order = new ArrayList<>();
		getPreOrderOfTree(root, order);
		return order;
	}
}
