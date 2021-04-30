package Optimization.IR.Analyser;

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
		this.adj = new LinkedHashMap<>();
		this.V.forEach(v -> this.adj.put(v, new LinkedHashSet<>()));
		this.radj = new LinkedHashMap<>();
		this.V.forEach(v -> this.radj.put(v, new LinkedHashSet<>()));
	}

	private ArrayList<basicBlock> order;

	private void getPostOrderOfGraph(basicBlock blk, Set<basicBlock> isVisited, int depth) {
		isVisited.add(blk);
		for (basicBlock adjBlk: adj.get(blk))
			if (!isVisited.contains(adjBlk)) getPostOrderOfGraph(adjBlk, isVisited, depth + 1);
		order.add(blk);
	}

	public ArrayList<basicBlock> getPostOrderOfGraph() {
		order = new ArrayList<>();
		Set<basicBlock> isVisited = new LinkedHashSet<>();
		getPostOrderOfGraph(root, isVisited, 0);
		return order;
	}

	public ArrayList<basicBlock> getReversePostOrderOfGraph() {
		ArrayList<basicBlock> order = getPostOrderOfGraph();
		Collections.reverse(order);
		return order;
	}

	private void domComputation(List<basicBlock> RPO) {
		dom = new LinkedHashMap<>();
		RPO.forEach(v -> dom.put(v, v == root ? new LinkedHashSet<>(Collections.singleton(v)) : new LinkedHashSet<>(RPO)));
		boolean changed;
		do {
			changed = false;
			for (basicBlock v: RPO) {
				if (v == root) continue;
				Set<basicBlock> temp = new LinkedHashSet<>(RPO);
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
		idom = new LinkedHashMap<>();
		for (basicBlock blk: RPO) {
			if (blk == root) {
				idom.put(blk, null);
				continue;
			}
			Set<basicBlock> domSet = new LinkedHashSet<>(dom.get(blk)), isVisited = new LinkedHashSet<>(Collections.singleton(blk));
			domSet.remove(blk);
			Queue<basicBlock> queue = new ArrayDeque<>(Collections.singletonList(blk));
			while (!queue.isEmpty()) {
				basicBlock top = queue.remove();
				if (domSet.contains(top)) {
					idom.put(blk, top);
					break;
				}
				radj.get(top).stream().filter(pred -> !isVisited.contains(pred)).forEach(pred -> {
					queue.offer(pred); isVisited.add(pred);
				});
			}
		}
	}

	private void dominatorTreeConstruction(List<basicBlock> RPO) {
		children = new LinkedHashMap<>();
		RPO.forEach(v -> children.put(v, new LinkedHashSet<>()));
		RPO.stream().filter(v -> v != root).forEach(v -> children.get(idom.get(v)).add(v));
	}

	private void DFComputation(List<basicBlock> RPO) {
		DF = new LinkedHashMap<>();
		RPO.forEach(v -> DF.put(v, new LinkedHashSet<>()));
		RPO.stream().filter(v -> radj.get(v).size() > 1).forEach(v -> radj.get(v).forEach(u -> {
			for (basicBlock runner = u;runner != idom.get(v);runner = idom.get(runner)) DF.get(runner).add(v);
		}));
	}

	public boolean dominanceAnalysis(boolean DFComputationFlag) {
		boolean ret = true;
		ArrayList<basicBlock> RPO = getReversePostOrderOfGraph();
		if (RPO.size() != V.size()) {
			Set<basicBlock> unreachable = new LinkedHashSet<>(V);
			RPO.forEach(unreachable::remove);
			unreachable.forEach(v -> {adj.get(v).forEach(u -> radj.get(u).remove(v)); adj.get(v).clear();});
			ret = false;
		}
		domComputation(RPO);
		idomComputation(RPO);
		dominatorTreeConstruction(RPO);
		if (DFComputationFlag) DFComputation(RPO);
		return ret;
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

	public boolean isDominatedBy(basicBlock u, basicBlock v) {return dom.get(u).contains(v);}

	public boolean isStrictlyDominatedBy(basicBlock u, basicBlock v) {return u != v && isDominatedBy(u, v);}
}
